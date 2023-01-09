package com.bestbranch.geulboxapi.user.join.service.dto;

import com.bestbranch.geulboxapi.common.exception.model.BadRequestException;
import com.bestbranch.geulboxapi.user.domain.User;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;


@Getter
public class UserJoinRequest {
    private String thirdPartyAccountId;
    private String email;
    private String name;
    private String nickname;
    private String password;
    private String accessToken;
    private String refreshToken;
    private User.AuthenticationType userAuthenticationType;
    private User.OSType osType;
    private String osVersion;

    private static UserJoinRequest of(String thirdPartyAccountId, User.AuthenticationType userAuthenticationType, String email, String password,
                                      String name, String accessToken, String refreshToken, User.OSType osType, String osVersion, String nickname) {
        UserJoinRequest join = new UserJoinRequest();
        join.thirdPartyAccountId = thirdPartyAccountId;
        join.userAuthenticationType = userAuthenticationType;
        join.email = email;
        join.password = password;
        join.name = name;
        join.nickname = nickname;
        join.accessToken = accessToken;
        join.refreshToken = refreshToken;
        join.osType = osType;
        join.osVersion = osVersion;
        return join;
    }

    private static void validateThirdPartyUserJoinParameters(User.AuthenticationType userAuthenticationType, String thirdPartyAccountId)
            throws BadRequestException {
        if (!(userAuthenticationType.isThirdPartyUser()
                && StringUtils.isNotBlank(thirdPartyAccountId))) {
            throw new BadRequestException("회원 인증타입 또는 thirdPartyAccountId 가 잘못됐습니다.");
        }
    }

    public static UserJoinRequest thirdPartyUserJoinOf(String thirdPartyAccountId, String name, String email, User.AuthenticationType userAuthenticationType)
            throws BadRequestException {
        validateThirdPartyUserJoinParameters(userAuthenticationType, thirdPartyAccountId);
        return of(thirdPartyAccountId, userAuthenticationType, email, null, name, null, null, null, null, null);
    }

    public static UserJoinRequest thirdPartyUserJoinOf(String thirdPartyAccountId, String name, String email, User.AuthenticationType userAuthenticationType,
                                                       String accessToken, String refreshToken, User.OSType osType, String osVersion) throws BadRequestException {
        validateThirdPartyUserJoinParameters(userAuthenticationType, thirdPartyAccountId);
        return of(thirdPartyAccountId, userAuthenticationType, email, null, name, accessToken, refreshToken, osType, osVersion, null);
    }

    public static UserJoinRequest thirdPartyUserJoinOf(String thirdPartyAccountId, String name, String email, User.AuthenticationType userAuthenticationType,
                                                       String accessToken, String refreshToken) throws BadRequestException {
        validateThirdPartyUserJoinParameters(userAuthenticationType, thirdPartyAccountId);
        return of(thirdPartyAccountId, userAuthenticationType, email, null, name, accessToken, refreshToken, null, null, null);
    }

    public static UserJoinRequest thirdPartyUserJoinOf(String thirdPartyAccountId, String name, String email, User.AuthenticationType userAuthenticationType,
                                                       String accessToken, String refreshToken, User.OSType osType, String osVersion, String nickname) throws
            BadRequestException {
        validateThirdPartyUserJoinParameters(userAuthenticationType, thirdPartyAccountId);
        return of(thirdPartyAccountId, userAuthenticationType, email, null, name, accessToken, refreshToken, osType, osVersion, nickname);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserAuthenticationType(User.AuthenticationType userAuthenticationType) {
        this.userAuthenticationType = userAuthenticationType;
    }

    public void validateForGeneralUser() throws BadRequestException {
        if (!(StringUtils.isNotBlank(email)
                && StringUtils.isNotBlank(password))) {
            throw new BadRequestException("이메일 또는 패스워드가 올바르지 않습니다.");
        }
    }
}
