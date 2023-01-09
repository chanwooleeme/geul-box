package com.bestbranch.geulboxapi.user.search.controller;

import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.user.domain.TokenAuthentication;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.domain.dto.UserStatusView;
import com.bestbranch.geulboxapi.user.report.service.UserReportService;
import com.bestbranch.geulboxapi.user.search.service.UserSearchService;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "회원 API 목록", tags = {"[USER]"})
@RestController
@RequiredArgsConstructor
public class UserSearchController {

    private final UserSearchService userSearchService;
    private final UserReportService userReportService;

    @ApiOperation(value = "회원 조회", notes = "APPLE 은 access token 대신 refresh token 으로 인증을 시도하며, refresh token 은 만료시간이 없음.")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
    })
    @TokenAuthentication
    @GetMapping("/user")
    public ApiResponse<UserStatusView> getUser(@ApiParam(hidden = true) @RequestAttribute Long userNo) {
        User user = userSearchService.getUser(userNo);
        return ApiResponse.success(UserStatusView.of(user, user.isBlocked() ? userReportService.getUserReport(user) :null));
    }
}
