package com.bestbranch.geulboxapi.geul.domain;


import com.bestbranch.geulboxapi.geul.report.domain.GeulReport;
import com.bestbranch.geulboxapi.geul.service.dto.GeulRegisterRequest;
import com.bestbranch.geulboxapi.geul.service.dto.GeulUpdateRequest;
import com.bestbranch.geulboxapi.user.domain.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "geul")
@Getter
public class Geul {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "geul_no")
    private Long geulNo;

    @Column(name = "user_no", insertable = false, updatable = false)
    private Long userNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @Column(name = "geul_content")
    private String geulContent;

    @Column(name = "geul_sources")
    private String geulSources;

    @Column(name = "geul_author")
    private String geulAuthor;

    @Column(name = "geul_category")
    @Enumerated(EnumType.STRING)
    private Category geulCategory;

    @Column(name = "page_of_book")
    private String pageOfBook;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "reaction_count")
    private Integer reactionCount;

    @Column(name = "modify_date_time")
    private LocalDateTime modifyDateTime;

    @Column(name = "register_date_time")
    private LocalDateTime registerDateTime;

    @Column(name = "view_count")
    private Integer viewCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "visible_status")
    private VisibleStatus visibleStatus;

    @OneToMany
    @JoinColumn(name = "geul_no")
    private List<GeulReport> geulReports = new ArrayList<>();

    protected Geul() {
    }

    public enum Category {
        BOOK, MOVIE, MUSIC, DRAMA, ETC
    }

    public enum VisibleStatus {
        SHOW, HIDDEN
    }

    public static Geul of(GeulRegisterRequest geulRequest, User user) {
        Geul geul = new Geul();
        geul.user = user;
        geul.appVersion = user.getAppVersion();
        geul.geulContent = geulRequest.getGeulContent();
        geul.geulSources = geulRequest.getGeulSources();
        geul.geulAuthor = geulRequest.getGeulAuthor();
        geul.geulCategory = geulRequest.getGeulCategory();
        geul.isPrivate = geulRequest.isPrivate();
        geul.pageOfBook = geulRequest.getPageOfBook();
        geul.reactionCount = 0;
        geul.viewCount = 0;
        geul.visibleStatus = VisibleStatus.SHOW;
        return geul;
    }

    public void updateGeulInfo(GeulUpdateRequest geulRequest) {
        this.geulContent = geulRequest.getGeulContent();
        this.geulSources = geulRequest.getGeulSources();
        this.geulAuthor = geulRequest.getGeulAuthor();
        this.modifyDateTime = LocalDateTime.now();
        this.geulCategory = geulRequest.getGeulCategory();
        this.isPrivate = geulRequest.isPrivate();
        this.pageOfBook = geulRequest.getPageOfBook();
    }

    public boolean isRegisterDateTimeIncludeOf(LocalDateTime firstDateTime, LocalDateTime lastDateTime) {
        return (this.registerDateTime.isEqual(firstDateTime) || this.registerDateTime.isAfter(firstDateTime))
                && (this.registerDateTime.isEqual(lastDateTime) || this.registerDateTime.isBefore(lastDateTime));
    }

    public Integer getRegisterDayOfMonth() {
        return this.registerDateTime.getDayOfMonth();
    }

    public void increaseReactionCount() {
        this.reactionCount++;
    }

    public void decreaseReactionCount() {
        this.reactionCount--;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public Boolean isHidden() {
        return this.visibleStatus.equals(VisibleStatus.HIDDEN);
    }

    public Boolean isShow() {
        return this.visibleStatus.equals(VisibleStatus.SHOW);
    }

    public void hide() {
        this.visibleStatus = VisibleStatus.HIDDEN;
    }
}

