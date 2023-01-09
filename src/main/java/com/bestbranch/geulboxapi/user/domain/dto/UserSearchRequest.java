package com.bestbranch.geulboxapi.user.domain.dto;

import com.bestbranch.geulboxapi.user.domain.User;
import lombok.Getter;
import lombok.Setter;


@Getter
public class UserSearchRequest {
    private String thirdPartyAccountId;
    @Setter
    private User.AuthenticationType userAuthenticationType;
    private String email;
    private String password;
    private String nickname;
    private String accessToken;
    private String refreshToken;

    public static UserSearchRequest from(String thirdPartyAccountId) {
        return of(thirdPartyAccountId, null);
    }

    public static UserSearchRequest of(String thirdPartyAccountId, User.AuthenticationType userAuthenticationType) {
        return of(thirdPartyAccountId, userAuthenticationType, null, null, null, null, null);
    }

    public static UserSearchRequest of(User.AuthenticationType userAuthenticationType, String email, String password) {
        return of(null, userAuthenticationType, email, password, null, null, null);
    }

    public static UserSearchRequest of(String thirdPartyAccountId, User.AuthenticationType userAuthenticationType, String email) {
        return of(thirdPartyAccountId, userAuthenticationType, email, null, null, null, null);
    }

    public static UserSearchRequest of(String thirdPartyAccountId, User.AuthenticationType userAuthenticationType, String email, String password,
                                       String accessToken, String refreshToken, String nickname) {
        UserSearchRequest search = new UserSearchRequest();
        search.thirdPartyAccountId = thirdPartyAccountId;
        search.userAuthenticationType = userAuthenticationType;
        search.email = email;
        search.password = password;
        search.nickname = nickname;
        search.accessToken = accessToken;
        search.refreshToken = refreshToken;
        return search;
    }
}
