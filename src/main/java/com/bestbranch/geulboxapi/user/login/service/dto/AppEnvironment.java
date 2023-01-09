package com.bestbranch.geulboxapi.user.login.service.dto;

import com.bestbranch.geulboxapi.user.domain.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppEnvironment {
    private User.OSType osType;
    private String osVersion;
    private String appVersion;
    private String pushToken;
}
