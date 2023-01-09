package com.bestbranch.geulboxapi.user.block.controller;

import com.bestbranch.geulboxapi.common.config.swagger.ApiResp;
import com.bestbranch.geulboxapi.common.config.swagger.ApiResps;
import com.bestbranch.geulboxapi.common.exception.model.ErrorCode;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.user.block.service.UserBlockService;
import com.bestbranch.geulboxapi.user.domain.TokenAuthentication;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(description = "회원 차단 API 목록", tags = {"[USER] [BLOCK]"})
@RestController
@RequiredArgsConstructor
public class UserBlockController {
    private final UserBlockService userBlockService;

    @ApiOperation(value = "회원 차단 하기")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
    })
    @ApiResps(
            value = {
                    @ApiResp(code = 200, message = "차단 완료"),

                    @ApiResp(errorCode = ErrorCode.USER_ALREADY_BLOCKED)
            }
    )
    @PostMapping("/users/{blockedUserNo}/block")
    @TokenAuthentication
    public ApiResponse block(@ApiParam(hidden = true) @RequestAttribute("userNo") Long blockerUserNo, @PathVariable("blockedUserNo") Long blockedUserNo) {
        userBlockService.blockUser(blockerUserNo, blockedUserNo);
        return ApiResponse.success();
    }

    @ApiOperation(value = "회원 차단 해제")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
    })
    @ApiResps(
            value = {
                    @ApiResp(code = 200, message = "차단 해제 완료"),

                    @ApiResp(errorCode = ErrorCode.USER_NOT_FOUND)
            }
    )
    @DeleteMapping("/users/{blockedUserNo}/unblock")
    @TokenAuthentication
    public ApiResponse unblock(@ApiParam(hidden = true) @RequestAttribute("userNo") Long blockerUserNo, @PathVariable("blockedUserNo") Long blockedUserNo) {
        userBlockService.unblockUser(blockerUserNo, blockedUserNo);
        return ApiResponse.success();
    }
}
