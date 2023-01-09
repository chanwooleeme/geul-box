package com.bestbranch.geulboxapi.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paging {
    private Integer page;
    private Integer itemCount;


    public Integer getItemCount() {
        return itemCount == null || itemCount < 1 ? 10 : itemCount;
    }

    public int getOffset() {
        if (page == null || page < 1) {
            page = 1;
        }
        if (itemCount == null || itemCount < 1) {
            itemCount = 10;
        }
        return (page - 1) * itemCount;
    }

    public static Paging of(Integer itemCount, Integer page) {
        Paging paging = new Paging();
        paging.itemCount = itemCount;
        paging.page = page;
        return paging;
    }

    public static Paging from(Integer itemCount) {
        return of(itemCount, null);
    }
}