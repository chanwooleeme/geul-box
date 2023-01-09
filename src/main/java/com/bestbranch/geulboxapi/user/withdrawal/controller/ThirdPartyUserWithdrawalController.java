package com.bestbranch.geulboxapi.user.withdrawal.controller;

import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.user.withdrawal.service.KakaoUserWithdrawalService;
import com.bestbranch.geulboxapi.user.withdrawal.service.ThirdPartyUserWithdrawalService;
import com.bestbranch.geulboxapi.user.withdrawal.service.dto.UserWithdrawalRequest;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(description = "서드파티 회원 탈퇴 API 목록", tags = {"[USER] [THIRD-PARTY] [WITHDRAWAL]"})
@RestController
@RequiredArgsConstructor
public class ThirdPartyUserWithdrawalController {
    private final ThirdPartyUserWithdrawalService thirdPartyUserWithdrawalService;
    private final KakaoUserWithdrawalService kakaoUserWithdrawalService;

    @ApiOperation(value = "서드파티 회원 탈퇴")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
    })
    @DeleteMapping("/third-party/users/withdrawal")
    public ApiResponse withdrawalFromUser(@ApiParam(hidden = true) @RequestAttribute String thirdPartyAccountId) {
        UserWithdrawalRequest param = UserWithdrawalRequest.of(thirdPartyAccountId);
        thirdPartyUserWithdrawalService.withdrawalUser(param);
        return ApiResponse.success();
    }

    @ApiOperation(value = "카카오계정 연결끊기 콜백 API(카카오 회원 탈퇴)")
    @ApiImplicitParam(value = "KakaoAK {accessToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "KakaoAK ")
    @PostMapping("/kakao-user-disconnect")
    public ApiResponse disconnectKakaoUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        kakaoUserWithdrawalService.withdrawalUser(accessToken.replaceAll("KakaoAK ", StringUtils.EMPTY));
        return ApiResponse.success();
    }
}

