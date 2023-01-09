package com.bestbranch.geulboxapi.user.join.controller;

import com.bestbranch.geulboxapi.common.cache.CacheKeyNames;
import com.bestbranch.geulboxapi.common.cache.EhCacheService;
import com.bestbranch.geulboxapi.common.exception.model.EmailAlreadyExistsException;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.HttpStatus;
import com.bestbranch.geulboxapi.user.join.exception.EmailAuthenticationException;
import com.bestbranch.geulboxapi.user.join.service.GeneralUserEmailAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(description = "일반 회원 가입 API 목록", tags = {"[USER] [GENERAL] [JOIN]"})
@Controller
@RequestMapping("/v1.1.2/auth")
@RequiredArgsConstructor
public class GeneralUserEmailAuthenticationController {
    private final GeneralUserEmailAuthenticationService generalUserEmailAuthenticationService;
    private final EhCacheService ehCacheService;

    @ApiOperation(value = "", hidden = true)
    @GetMapping("/email/authKeys/{authKey}")
    public String callbackAuthenticationEmail(@PathVariable String authKey) {
        try {
            generalUserEmailAuthenticationService.authenticateEmail(authKey);
            return "email-auth-complete";
        } catch (EmailAuthenticationException e) {
            return "email-auth-failure";
        } catch (Exception e) {
            e.printStackTrace();
            return "email-auth-failure";
        }
    }

    @ApiOperation(value = "이메일 인증 여부 검사", notes = "메일인증 완료 시점부터 3분간 유효")
    @PostMapping("/auth-keys/{authKey}")
    @ResponseBody
    public ApiResponse isAuthenticatedEmail(@PathVariable String authKey) {
        if (ehCacheService.isValueEmpty(CacheKeyNames.EMAIL_AUTHENTICATION_CONFIRMATION, authKey)) {
            return ApiResponse.failure(HttpStatus.UNAUTHORIZED);
        } else {
            return ApiResponse.success();
        }
    }

    @ApiOperation(value = "이메일 인증 메일 전송", notes = "5분간 유효")
    @PostMapping("/emails/{email}")
    @ResponseBody
    public ApiResponse sendAuthenticationMail(@PathVariable String email) {
        try {
            String authenticationKey = generalUserEmailAuthenticationService.sendAuthenticationMailAndGetAuthenticationKey(email);
            Map<String, String> result = new HashMap<>();
            result.put("authKey", authenticationKey);
            return ApiResponse.success(result);
        } catch (EmailAlreadyExistsException e) {
            return ApiResponse.failure(HttpStatus.ALREADY_EXIST, e.getMessage());
        }
    }
}
