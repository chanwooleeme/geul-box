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

@Api(description = "서드파티 회원 로그인 API 목록", tags = {"[USER] [THIRD-PARTY] [LOGIN]"})
@RestController
@RequiredArgsConstructor
public class ThirdPartyUserLoginController {
    private final UserSearchService userSearchService;
    private final UserLoginService thirdPartyUserLoginService;
    private final UserReportService userReportService;

    @ApiOperation(value = "서드파티 회원 로그인")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE")
    })
    @PostMapping("/third-party/user/auth")
    public ApiResponse<UserView> loginThirdPartyUserV1_0(@ApiParam(hidden = true) @RequestAttribute Long userNo) {
        return ApiResponse.success(UserView.from(userSearchService.getUser(userNo)));
    }

    @ApiOperation(value = "서드파티 회원 로그인", notes = "APPLE 은 access token 대신 refresh token 으로 인증을 시도하며, refresh token 은 만료시간이 없음.")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE")
    })
    @PostMapping("/v1.1/third-party/user/auth")
    public ApiResponse<UserView> loginThirdPartyUserV1_1(@RequestBody AppEnvironment environment, @ApiParam(hidden = true) @RequestAttribute Long userNo) {
        thirdPartyUserLoginService.updateLoginDateTimeAndEnvironment(environment, userNo);
        User user = userSearchService.getUser(userNo);
        return ApiResponse.success(UserView.from(user, user.isBlocked() ? userReportService.getUserReport(user) : null));
    }
}
