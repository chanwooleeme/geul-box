package com.bestbranch.geulboxapi.user.profile.geul.service;

import com.bestbranch.geulboxapi.geul.search.service.GeulSearchService;
import com.bestbranch.geulboxapi.geul.search.service.dto.GeulSearchRequest;
import com.bestbranch.geulboxapi.geul.search.service.dto.GeulView;
import com.bestbranch.geulboxapi.user.profile.geul.service.dto.ProfileGeulsView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileGeulService {
    private final GeulSearchService geulSearchService;

    @Transactional(readOnly = true)
    public ProfileGeulsView getProfileGeuls(GeulSearchRequest param) {
        List<GeulView> geuls = geulSearchService.getNoOffsetGeulsOfUser(param)
                .stream()
                .map(GeulView::from)
                .collect(Collectors.toList());
        param.setDateToNull();
        long geulCount = geulSearchService.getGeulCountOfUser(param);
        return ProfileGeulsView.of(geuls, geulCount);
    }

}
