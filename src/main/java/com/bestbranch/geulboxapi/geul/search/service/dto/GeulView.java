package com.bestbranch.geulboxapi.geul.search.service.dto;


import com.bestbranch.geulboxapi.geul.domain.Geul;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
public class GeulView {
    private Long geulNo;
    private UserView user;
    private String geulContent;
    private String geulSources;
    private String geulAuthor;
    private Geul.Category geulCategory;
    private String pageOfBook;
    private Boolean isPrivate;
    private Integer reactionCount;
    private Integer geulViewCount;
    @Setter
    private Boolean isApproachedUserReacted;
    private LocalDateTime modifyDateTime;
    private LocalDateTime registerDateTime;

    @Getter
    public static class UserView {
        private Long userNo;
        private String nickname;
    }

    public boolean canView(Long userNoOfViewer) {
        return userNoOfViewer.equals(this.user.getUserNo()) || !isPrivate;
    }

    public static GeulView from(Geul geul) {
        GeulView view = new GeulView();
        view.user = new UserView();

        view.geulNo = geul.getGeulNo();
        view.geulContent = geul.getGeulContent();
        view.geulSources = geul.getGeulSources();
        view.geulAuthor = geul.getGeulAuthor();
        view.geulCategory = geul.getGeulCategory();
        view.pageOfBook = geul.getPageOfBook();
        view.isPrivate = geul.getIsPrivate();
        view.modifyDateTime = geul.getModifyDateTime();
        view.registerDateTime = geul.getRegisterDateTime();
        view.reactionCount = geul.getReactionCount();
        view.user.userNo = geul.getUser().getUserNo();
        view.user.nickname = geul.getUser().getNickname();
        view.geulViewCount = geul.getViewCount();
        return view;
    }
}
