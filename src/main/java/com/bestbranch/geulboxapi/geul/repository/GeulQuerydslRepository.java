package com.bestbranch.geulboxapi.geul.repository;

import com.bestbranch.geulboxapi.common.exception.model.ResourceNotExistsException;
import com.bestbranch.geulboxapi.common.exception.model.UnauthorizedException;
import com.bestbranch.geulboxapi.common.infrastructure.DefaultQuerydslRepositorySupport;
import com.bestbranch.geulboxapi.common.model.OrderType;
import com.bestbranch.geulboxapi.geul.domain.Geul;
import com.bestbranch.geulboxapi.geul.search.service.dto.GeulSearchRequest;
import com.bestbranch.geulboxapi.geul.service.dto.GeulUpdateRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPADeleteClause;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.bestbranch.geulboxapi.geul.domain.QGeul.geul;
import static com.bestbranch.geulboxapi.geul.report.domain.QGeulReport.geulReport;


@Repository
public class GeulQuerydslRepository extends DefaultQuerydslRepositorySupport {
    @PersistenceContext
    private EntityManager entityManager;

    public GeulQuerydslRepository() {
        super(Geul.class);
    }

    public void saveGeul(Geul geul) {
        entityManager.persist(geul);
    }

    public Geul getGeul(Long geulNo) {
        return entityManager.find(Geul.class, geulNo);
    }

    public List<Geul> getNoOffsetGeulsByCategory(GeulSearchRequest param) {
        param.setOrderType(OrderType.DESC);
        return from(geul)
                .leftJoin(geul.geulReports, geulReport)
                .fetchJoin()
                .where(likeContent(param.getKeyword()), eqRegisterDate(param.getDate()), inequalGeulNo(param.getExclusiveStandardGeulNo()),
                        eqGeulCategory(param.getCategory()), geul.isPrivate.isFalse())
                .orderBy(getOrderSpecifierRegisterDateTimeBy(param.getOrderType()), getOrderSpecifierGeulNoBy(param.getOrderType()))
                .limit(param.getPaging().getItemCount())
                .fetch();
    }

    public List<Geul> getGeulsOfUser(GeulSearchRequest param) {
        return from(geul)
                .where(eqUserNo(param.getUserNo()), eqRegisterDate(param.getDate()))
                .orderBy(getOrderSpecifierRegisterDateTimeBy(param.getOrderType()), getOrderSpecifierGeulNoBy(param.getOrderType()))
                .fetch();
    }

    public List<Geul> getGeulsWithoutOrder(GeulSearchRequest param) {
        return from(geul)
                .where(eqUserNo(param.getUserNo()))
                .fetch();
    }

    public List<Geul> getNoOffsetGeulsOfUser(GeulSearchRequest param) {
        param.setOrderType(OrderType.DESC);
        return from(geul)
                .leftJoin(geul.geulReports, geulReport)
                .fetchJoin()
                .where(eqUserNo(param.getUserNo()), eqRegisterDate(param.getDate()), eqIsPrivate(param.getIncludePrivateGeuls()),
                        inequalGeulNo(param.getExclusiveStandardGeulNo()))
                .orderBy(getOrderSpecifierRegisterDateTimeBy(param.getOrderType()), getOrderSpecifierGeulNoBy(param.getOrderType()))
                .limit(param.getPaging().getItemCount())
                .fetch();
    }

    public List<Geul> getPagedGeulsOfUser(GeulSearchRequest param) {
        return from(geul)
                .where(eqUserNo(param.getUserNo()), eqRegisterDate(param.getDate()))
                .orderBy(getOrderSpecifierRegisterDateTimeBy(param.getOrderType()), getOrderSpecifierGeulNoBy(param.getOrderType()))
                .offset(param.getPaging().getOffset())
                .limit(param.getPaging().getItemCount())
                .fetch();
    }

    public long getProfileGeulCount(GeulSearchRequest param) {
        return from(geul)
                .where(eqUserNo(param.getUserNo()), eqRegisterDate(param.getDate()), eqIsPrivate(param.getIncludePrivateGeuls()))
                .fetchCount();
    }

    public void updateGeul(Long geulNo, GeulUpdateRequest geulRequest, Long userNo) throws UnauthorizedException, ResourceNotExistsException {
        Geul geul = Optional.ofNullable(entityManager.find(Geul.class, geulNo))
                .orElseThrow(() -> new ResourceNotExistsException("존재하지 않는 글입니다."));

        if (userNo.equals(geul.getUser().getUserNo())) {
            geul.updateGeulInfo(geulRequest);
        } else {
            throw new UnauthorizedException("글을 수정할 수 있는 권한이 없습니다.");
        }
    }

    public void removeGeul(Long geulNo, Long userNo) throws UnauthorizedException, ResourceNotExistsException {
        Geul geul = Optional.ofNullable(entityManager.find(Geul.class, geulNo))
                .orElseThrow(() -> new ResourceNotExistsException("존재하지 않는 글입니다."));

        if (userNo.equals(geul.getUser().getUserNo())) {
            entityManager.remove(geul);
        } else {
            throw new UnauthorizedException("글을 삭제할 수 있는 권한이 없습니다.");
        }
    }

    public void deleteByUserNo(Long userNo) {
        JPADeleteClause deleteClause = new JPADeleteClause(entityManager, geul);
        deleteClause.where(geul.user.userNo.eq(userNo)).execute();
    }

    private OrderSpecifier getOrderSpecifierRegisterDateTimeBy(OrderType orderType) {
        if (orderType == null) {
            return null;
        }
        switch (orderType) {
            case ASC:
                return geul.registerDateTime.asc();
            case DESC:
                return geul.registerDateTime.desc();
            default:
                return null;
        }
    }

    private OrderSpecifier getOrderSpecifierGeulNoBy(OrderType orderType) {
        if (orderType == null) {
            return null;
        }
        switch (orderType) {
            case ASC:
                return geul.geulNo.asc();
            case DESC:
                return geul.geulNo.desc();
            default:
                return null;
        }
    }

    private BooleanExpression eqUserNo(Long userNo) {
        return userNo != null ? geul.user.userNo.eq(userNo) : null;
    }

    //TODO 추후 검색엔진 도입
    private BooleanExpression likeContent(String keyword) {
        return keyword != null ? geul.geulContent.contains(keyword) : null;
    }

    private BooleanExpression eqRegisterDate(LocalDate date) {
        return date != null ? geul.registerDateTime.between(
                LocalDateTime.of(date, LocalTime.of(0, 0, 0)),
                LocalDateTime.of(date, LocalTime.of(23, 59, 59))) : null;
    }

    private BooleanExpression inequalGeulNo(Long geulNo) {
        return geulNo != null ? geul.geulNo.lt(geulNo) : null;
    }

    private BooleanExpression eqGeulCategory(Geul.Category geulCategory) {
        return geulCategory != null ? geul.geulCategory.eq(geulCategory) : null;
    }

    private BooleanExpression eqIsPrivate(Boolean includePrivateGeuls) {
        if (includePrivateGeuls != null && !includePrivateGeuls) {
            return geul.isPrivate.eq(false);
        }
        return null;
    }
}