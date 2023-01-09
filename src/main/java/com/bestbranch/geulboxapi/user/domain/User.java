package com.bestbranch.geulboxapi.user.domain;

import com.bestbranch.geulboxapi.common.model.JwtAccessToken;
import com.bestbranch.geulboxapi.common.model.JwtToken;
import com.bestbranch.geulboxapi.geul.domain.Geul;
import com.bestbranch.geulboxapi.user.block.domain.UserBlock;
import com.bestbranch.geulboxapi.user.join.service.dto.UserJoinRequest;
import com.bestbranch.geulboxapi.user.login.service.dto.AppEnvironment;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@DynamicInsert
@DynamicUpdate
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo;

    @Getter
    @Column(name = "third_party_account_id")
    private String thirdPartyAccountId;

    @Getter
    @Column(name = "email")
    private String email;

    @Getter
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @Column(name = "nickname")
    private String nickname;

    @Setter
    @Column(name = "password")
    private String password;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "user_authentication_type")
    private AuthenticationType userAuthenticationType;

    @Getter
    @Setter
    @Column(name = "access_token")
    private String accessToken;

    @Getter
    @Column(name = "access_token_expire_millisecond")
    private Long accessTokenExpireMillisecond;

    @Getter
    @Setter
    @Column(name = "refresh_token")
    private String refreshToken;

    @Getter
    @Column(name = "refresh_token_expire_millisecond")
    private Long refreshTokenExpireMillisecond;

    @Enumerated(EnumType.STRING)
    @Column(name = "os_type")
    private OSType osType;

    @Column(name = "os_version")
    private String osVersion;

    @Getter
    @Column(name = "introduction")
    private String introduction;

    @Setter
    @Column(name = "push_token")
    private String pushToken;

    @Getter
    @Column(name = "app_version")
    private String appVersion;

    @Setter
    @Column(name = "last_login_date_time")
    private LocalDateTime lastLoginDateTime;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "modify_date_time")
    private LocalDateTime modifyDateTime;

    @Column(name = "register_date_time")
    private LocalDateTime registerDateTime;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Geul> geuls = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "blocker_user_no")
    @Getter
    private List<UserBlock> myUserBlocks = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "blocked_user_no")
    @Getter
    private List<UserBlock> userBlocksMe = new ArrayList();

    protected User() {
    }

    public static User from(UserJoinRequest join) {
        User user = new User();
        user.email = join.getEmail();
        user.name = join.getName();
        user.password = join.getPassword();
        user.nickname = join.getNickname();
        user.userAuthenticationType = join.getUserAuthenticationType();
        user.thirdPartyAccountId = join.getThirdPartyAccountId();
        user.accessToken = join.getAccessToken();
        user.refreshToken = join.getRefreshToken();
        user.osType = join.getOsType();
        user.osVersion = join.getOsVersion();
        return user;
    }

    public enum AuthenticationType {
        GENERAL, KAKAO, KAKAO_OLD, APPLE, NONE;

        public static AuthenticationType getCodeOf(String name) {
            for (AuthenticationType userAuthenticationType : AuthenticationType.values()) {
                if (userAuthenticationType.name().equals(name)) {
                    return userAuthenticationType;
                }
            }
            return NONE;
        }

        public boolean isCorrect() {
            return !this.equals(NONE);
        }

        public boolean isThirdPartyUser() {
            return this.equals(KAKAO)
                    || this.equals(APPLE);
        }
    }

    public enum OSType {
        iOS, ANDROID
    }

    public enum Status {
        ACTIVE, BLOCKED
    }

    public void setJwtTokens(JwtToken token) {
        this.accessToken = token.getAccessToken().getToken();
        this.accessTokenExpireMillisecond = token.getAccessToken().getExp();
        this.refreshToken = token.getRefreshToken().getToken();
        this.refreshTokenExpireMillisecond = token.getRefreshToken().getExp();
    }

    public boolean equalsRefreshToken(String refreshToken) {
        return this.refreshToken.equals(refreshToken.replaceFirst("Bearer ", StringUtils.EMPTY));
    }

    public boolean equalsAccessToken(String accessToken) {
        return this.accessToken.equals(accessToken.replaceFirst("Bearer ", StringUtils.EMPTY));
    }

    public void setJwtAccessToken(JwtAccessToken jwtAccessToken) {
        this.accessToken = jwtAccessToken.getToken();
        this.accessTokenExpireMillisecond = jwtAccessToken.getExp();
    }

    public void setTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void setOS(OSType osType, String osVersion) {
        this.osType = osType;
        this.osVersion = osVersion;
    }

    public void setEnvironment(AppEnvironment environment) {
        this.osType = environment.getOsType();
        this.osVersion = environment.getOsVersion();
        this.appVersion = environment.getAppVersion();
        this.pushToken = environment.getPushToken();
    }

    public void setProfile(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }

    public boolean equalsNickName(String nickname) {
        return this.nickname.equals(nickname);
    }

    public boolean isNickNameEmpty() {
        return StringUtils.isBlank(this.nickname);
    }

    public void block() {
        this.status = Status.BLOCKED;
    }

    public Boolean isBlocked() {
        return this.status == Status.BLOCKED;
    }

    public Boolean isInterectiveBy(Long userNo) {
        return userBlocksMe.stream().noneMatch(userBlock -> userBlock.getBlockerUserNo().equals(userNo)) &&
                myUserBlocks.stream().noneMatch(userBlock -> userBlock.getBlockedUserNo().equals(userNo));
    }
}
