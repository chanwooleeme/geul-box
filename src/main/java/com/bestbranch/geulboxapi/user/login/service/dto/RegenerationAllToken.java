package com.bestbranch.geulboxapi.user.login.service.dto;

import com.bestbranch.geulboxapi.common.exception.model.BadRequestException;
import com.bestbranch.geulboxapi.user.domain.User;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;


@Getter
public class RegenerationAllToken {
    private String email;
    private String password;
    private User.AuthenticationType userAuthenticationType;

    public void setUserAuthenticationType(User.AuthenticationType userAuthenticationType) {
        this.userAuthenticationType = userAuthenticationType;
    }

    public void validate() throws BadRequestException {
        if (!(StringUtils.isNotBlank(this.email)
                && StringUtils.isNotBlank(this.password)
                && this.userAuthenticationType.equals(User.AuthenticationType.GENERAL))) {
            throw new BadRequestException("인증 타입 또는 이메일 또는 패스워드가 잘못됐습니다.");
        }

    }
}
