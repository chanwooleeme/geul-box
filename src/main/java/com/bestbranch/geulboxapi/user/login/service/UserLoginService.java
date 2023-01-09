package com.bestbranch.geulboxapi.user.login.service;

import com.bestbranch.geulboxapi.user.login.service.dto.AppEnvironment;

public interface UserLoginService {
    void updateLoginDateTimeAndEnvironment(AppEnvironment environment, Long userNo);
}
