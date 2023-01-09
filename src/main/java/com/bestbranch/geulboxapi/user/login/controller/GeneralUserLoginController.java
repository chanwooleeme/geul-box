package com.bestbranch.geulboxapi.user.login.controller;

import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.domain.dto.UserView;
import com.bestbranch.geulboxapi.user.login.service.UserLoginService;
import com.bestbranch.geulboxapi.user.login.service.dto.AppEnvironment;
import com.bestbranch.geulboxapi.user.report.service.UserReportService;
import com.bestbranch.geulboxapi.user.search.service.UserSearchService;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "일반 회원 로그인 API 목록", tags = {"[USER] [GENERAL] [LOGIN]"})
@RestController
@RequiredArgsConstructor
public class GeneralUserLoginController {
    private final UserSearchService userSearchService;
    private final UserLoginService generalUserLoginService;
    private final UserReportService userReportService;

    @ApiOperation(value = "일반 회원 로그인")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "Bearer {accessToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "GENERAL")
    })
    @PostMapping("/user/auth")
    public ApiResponse<UserView> loginGeneralUserV1_0(@ApiParam(hidden = true) @RequestAttribute Long userNo) {
        return ApiResponse.success(UserView.from(userSearchService.getUser(userNo)));
    }

    @ApiOperation(value = "일반 회원 로그인")
    @ApiImplicitParam(value = "Bearer {accessToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer ")
    @PostMapping(value = {"/v1.1/user/auth", "/v1.2/user/auth"})
    public ApiResponse<UserView> loginGeneralUserV1_1AndV1_2(@RequestBody AppEnvironment environment, @ApiParam(hidden = true) @RequestAttribute Long userNo) {
        generalUserLoginService.updateLoginDateTimeAndEnvironment(environment, userNo);
        User user = userSearchService.getUser(userNo);
        return ApiResponse.success(UserView.from(user, user.isBlocked()? userReportService.getUserReport(user) : null));
    }
}