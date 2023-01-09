package com.bestbranch.geulboxapi.user.withdrawal.controller;

import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.user.withdrawal.service.UserWithdrawalService;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "일반 회원 탈퇴 API 목록", tags = {"[USER] [GENERAL] [WITHDRAWAL]"})
@RestController
@RequiredArgsConstructor
public class GeneralUserWithdrawalController {
    private final UserWithdrawalService userWithdrawalService;

    @ApiOperation(value = "일반 회원 탈퇴")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "Bearer {accessToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "GENERAL"),
    })
    @DeleteMapping(value = {"/users/withdrawal", "/v1.2/users/withdrawal"})
    public ApiResponse withdrawalFromThirdPartyUser(@ApiParam(hidden = true) @RequestAttribute Long userNo) {
        userWithdrawalService.withdrawalUser(userNo);
        return ApiResponse.success();
    }
}

