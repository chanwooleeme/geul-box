package com.bestbranch.geulboxapi.geul.search.service.dto;

import com.bestbranch.geulboxapi.common.model.OrderType;
import com.bestbranch.geulboxapi.common.model.Paging;
import com.bestbranch.geulboxapi.geul.domain.Geul;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(staticName = "of")
public class GeulSearchRequest {
    private Long userNo;
    private String keyword;
    @Setter
    private OrderType orderType;
    private Paging paging;
    private LocalDate date;
    private Geul.Category category;
    @Setter
    private Long exclusiveStandardGeulNo;
    private Boolean includePrivateGeuls;
    private Long requestUserNo;

    public void setDateToNull() {
        this.date = null;
    }

    public Boolean isReportFilteringSearch() {
        return requestUserNo != null;
    }

    public static GeulSearchRequest of(Long userNo) {
        return of(userNo, null, null, null);
    }

    public static GeulSearchRequest of(Long userNo, Boolean includePrivateGeuls) {
        return of(userNo, null, null, null, includePrivateGeuls);
    }

    public static GeulSearchRequest of(Long userNo, OrderType orderType) {
        return of(userNo, orderType, null, null);
    }

    public static GeulSearchRequest of(OrderType orderType, Paging paging, Geul.Category category, Long exclusiveStandardGeulNo) {
        return of(null, null, orderType, paging, null, category, exclusiveStandardGeulNo, null, null);
    }

    public static GeulSearchRequest of(OrderType orderType, Paging paging, Geul.Category category, Long exclusiveStandardGeulNo, Long requestUserNo) {
        return of(null, null, orderType, paging, null, category, exclusiveStandardGeulNo, null, requestUserNo);
    }

    public static GeulSearchRequest of(String keyword, OrderType orderType, Paging paging) {
        return of(null, keyword, orderType, paging, null, null, null, null, null);
    }

    public static GeulSearchRequest of(Long userNo, OrderType orderType, Paging paging, LocalDate date) {
        return of(userNo, null, orderType, paging, date, null, null, null, null);
    }

    public static GeulSearchRequest of(Long userNo, OrderType orderType, Paging paging, LocalDate date, Boolean includePrivateGeuls) {
        return of(userNo, null, orderType, paging, date, null, null, includePrivateGeuls, null);
    }

    public static GeulSearchRequest of(Long userNo, Paging paging, LocalDate date, Boolean includePrivateGeuls, Long exclusiveStandardGeulNo) {
        return of(userNo, null, null, paging, date, null, exclusiveStandardGeulNo, includePrivateGeuls, null);
    }

    public static GeulSearchRequest of(Long userNo, Paging paging, LocalDate date, Boolean includePrivateGeuls, Long exclusiveStandardGeulNo, Long requestUserNo) {
        return of(userNo, null, null, paging, date, null, exclusiveStandardGeulNo, includePrivateGeuls, requestUserNo);
    }
}
