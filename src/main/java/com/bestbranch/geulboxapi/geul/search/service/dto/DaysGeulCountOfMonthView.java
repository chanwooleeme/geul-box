package com.bestbranch.geulboxapi.geul.search.service.dto;

import lombok.Getter;

import java.util.Set;

@Getter
public class DaysGeulCountOfMonthView {
    private Integer year;
    private Integer month;
    private Set<Integer> days;

    public static DaysGeulCountOfMonthView of(Integer year, Integer month, Set<Integer> days) {
        DaysGeulCountOfMonthView daysGeulCountOfMonth = new DaysGeulCountOfMonthView();
        daysGeulCountOfMonth.year = year;
        daysGeulCountOfMonth.month = month;
        daysGeulCountOfMonth.days = days;
        return daysGeulCountOfMonth;
    }
}
