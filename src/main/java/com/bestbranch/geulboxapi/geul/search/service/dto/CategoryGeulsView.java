package com.bestbranch.geulboxapi.geul.search.service.dto;


import com.bestbranch.geulboxapi.geul.domain.Geul;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@RequiredArgsConstructor(staticName = "from")
public class CategoryGeulsView {
    private final List<GeulView> geuls;

    @Getter
    public static class GeulView {
        private Long geulNo;
        private String geulContent;
        private String geulSources;
        private String geulAuthor;
        private Geul.Category geulCategory;
        private String pageOfBook;
        private Boolean isPrivate;
        private Integer reactionCount;
        private LocalDateTime modifyDateTime;
        private LocalDateTime registerDateTime;

        public static GeulView from(Geul geul) {
            GeulView view = new GeulView();
            view.geulNo = geul.getGeulNo();
            view.geulContent = geul.getGeulContent();
            view.geulSources = geul.getGeulSources();
            view.geulAuthor = geul.getGeulAuthor();
            view.geulCategory = geul.getGeulCategory();
            view.pageOfBook = geul.getPageOfBook();
            view.isPrivate = geul.getIsPrivate();
            view.reactionCount = geul.getReactionCount();
            view.modifyDateTime = geul.getModifyDateTime();
            view.registerDateTime = geul.getRegisterDateTime();
            return view;
        }
    }
}
