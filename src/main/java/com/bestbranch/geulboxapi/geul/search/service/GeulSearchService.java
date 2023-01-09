package com.bestbranch.geulboxapi.geul.search.service;

import com.bestbranch.geulboxapi.common.cache.EhCacheService;
import com.bestbranch.geulboxapi.common.exception.model.ResourceNotExistsException;
import com.bestbranch.geulboxapi.common.model.OrderType;
import com.bestbranch.geulboxapi.geul.domain.Geul;
import com.bestbranch.geulboxapi.geul.repository.GeulQuerydslRepository;
import com.bestbranch.geulboxapi.geul.search.service.dto.CategoryGeulsView;
import com.bestbranch.geulboxapi.geul.search.service.dto.DaysGeulCountOfMonthView;
import com.bestbranch.geulboxapi.geul.search.service.dto.GeulSearchRequest;
import com.bestbranch.geulboxapi.geul.search.service.dto.GeulView;
import com.bestbranch.geulboxapi.reaction.repository.ReactionSpringDataJpaRepository;
import com.bestbranch.geulboxapi.user.block.service.UserBlockService;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.search.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeulSearchService {

    private final GeulQuerydslRepository geulRepository;
    private final ReactionSpringDataJpaRepository reactionRepository;
    private final EhCacheService ehCacheService;
    private final UserBlockService userBlockService;
    private final UserSearchService userSearchService;

    @Transactional
    public GeulView getGeulDetailBy(Long geulNo, Long userNo) throws ResourceNotExistsException {
        //Geul geul = ehCacheService.get(CacheKeyNames.GEUL_DETAIL, String.valueOf(geulNo), Geul.class, () -> geulRepository.getGeul(geulNo));
        //사용자가 많지 않으므로 잠시 캐시 사용 보류

        Geul geul = geulRepository.getGeul(geulNo);
        if (geul != null) {
            GeulView geulView = GeulView.from(geul);
            geulView.setIsApproachedUserReacted(reactionRepository.findByUserNoAndGeulNo(userNo, geulNo).isPresent());
            if (!geul.getUser().getUserNo().equals(userNo)) {
                geul.increaseViewCount();
            }
            return geulView;
        }
        throw new ResourceNotExistsException("존재하지 않는 글입니다.");
    }

    @Transactional(readOnly = true)
    public List<GeulView> getPagedGeulsOfUser(GeulSearchRequest param) {
        return geulRepository.getPagedGeulsOfUser(param)
                .stream()
                .map(GeulView::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Geul> getNoOffsetGeulsOfUser(GeulSearchRequest param) {
        if (param.isReportFilteringSearch()) {
            return getGeulsWithReportFiltering(param, () -> geulRepository.getNoOffsetGeulsOfUser(param));
        }
        return geulRepository.getNoOffsetGeulsOfUser(param);
    }

    @Transactional(readOnly = true)
    public long getGeulCountOfUser(GeulSearchRequest param) {
        return geulRepository.getProfileGeulCount(param);
    }

    @Transactional(readOnly = true)
    public DaysGeulCountOfMonthView getDaysGeulCountOfMonth(Long userNo, Integer searchYear, Integer searchMonth) {
        LocalDateTime firstDateTimeOfMonth = LocalDateTime.of(searchYear, searchMonth, 1, 0, 0);
        LocalDateTime lastDateTimeOfMonth = LocalDateTime.of(YearMonth.of(searchYear, searchMonth).atEndOfMonth(), LocalTime.of(23, 59, 59));


        List<Geul> geuls = geulRepository.getGeulsOfUser(GeulSearchRequest.of(userNo, OrderType.DESC));

        Set<Integer> daysOfMonth = geuls.stream()
                .filter(geul -> geul.isRegisterDateTimeIncludeOf(firstDateTimeOfMonth, lastDateTimeOfMonth))
                .map(Geul::getRegisterDayOfMonth)
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return DaysGeulCountOfMonthView.of(searchYear, searchMonth, daysOfMonth);
    }

    @Transactional(readOnly = true)
    public CategoryGeulsView getGeulsByCategory(GeulSearchRequest param) {
        if (param.isReportFilteringSearch()) {
            return CategoryGeulsView.from(getGeulsWithReportFiltering(param, () -> geulRepository.getNoOffsetGeulsByCategory(param))
                    .stream()
                    .map(CategoryGeulsView.GeulView::from)
                    .collect(Collectors.toList()));
        }
        return CategoryGeulsView.from(geulRepository.getNoOffsetGeulsByCategory(param)
                .stream()
                .map(CategoryGeulsView.GeulView::from)
                .collect(Collectors.toList()));
    }

    private List<Geul> getGeulsWithReportFiltering(GeulSearchRequest param, Supplier<List<Geul>> geulSupplier) {
        List<Geul> geuls = new ArrayList<>();
        Boolean isFirstSearch = true;
        Integer requestItemCount = param.getPaging().getItemCount();
        Long lastSearchGeulNo = 0L;

        do {
            if (!isFirstSearch) { //추가 조회시 이전에 조회했던 마지막 아이템을 기준, 부족한 아이템 수 만큼 조회하도록 세팅.
                param.setExclusiveStandardGeulNo(lastSearchGeulNo);
                param.getPaging().setItemCount(requestItemCount - geuls.size());
            }

            List<Geul> noFilteredGeuls = geulSupplier.get();
            if (noFilteredGeuls.isEmpty()) { //신고 글 필터링되지 않은 조회결과가 없다면 더 이상 조회할 수 있는 글이 없는것으로 판단.
                break;
            }

            lastSearchGeulNo = noFilteredGeuls.get(noFilteredGeuls.size() - 1).getGeulNo();
            User requestUser = userSearchService.getUser(param.getRequestUserNo());
            List<Geul> filteredGeuls = noFilteredGeuls.stream()
                    .filter(Geul::isShow)
                    .filter(geul -> geul.getGeulReports()
                            .stream()
                            .noneMatch(geulReport -> geulReport.getUserNo().equals(param.getRequestUserNo()))
                    )
                    .filter(
                            geul -> requestUser.isInterectiveBy(geul.getUserNo())
                    )
                    .collect(Collectors.toList());
            
            geuls.addAll(filteredGeuls);

            isFirstSearch = false;
        } while (geuls.size() < requestItemCount);

        return geuls;
    }
}
