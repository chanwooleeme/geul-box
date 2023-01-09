package com.bestbranch.geulboxapi.user.password.controller;

import com.bestbranch.geulboxapi.common.exception.model.BadRequestException;
import com.bestbranch.geulboxapi.common.exception.model.EmailNotFoundException;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.HttpStatus;
import com.bestbranch.geulboxapi.user.password.service.PasswordInitializationService;
import com.bestbranch.geulboxapi.user.password.service.dto.PasswordInitializationConfirmationRequest;
import com.bestbranch.geulboxapi.user.password.service.dto.PasswordInitializationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(description = "일반 회원 비밀번호 API 목록", tags = {"[USER] [GENERAL] [PASSWORD]"})
@RestController
@RequestMapping("/v1.2/auth/password-initialization")
@RequiredArgsConstructor
public class PasswordInitializationController {
    private final PasswordInitializationService passwordInitializationService;

    @ApiOperation(value = "비밀번호 초기화를 위한 인증번호 이메일 전송")
    @PostMapping("/emails/{email}")
    public ApiResponse sendAuthNo(@PathVariable String email) {
        try {
            passwordInitializationService.sendAuthNo(email);
            return ApiResponse.success();
        } catch (EmailNotFoundException e) {
            return ApiResponse.failure(HttpStatus.RESOURCE_NOT_EXIST, e.getMessage());
        }
    }

    @ApiOperation(value = "비밀번호 초기화를 위한 인증번호 확인 (인증번호 1회 유효)")
    @PostMapping("/confirmation")
    public ApiResponse confirmAuthNo(@RequestBody PasswordInitializationConfirmationRequest passwordInitializationConfirmationRequest) {
        if (passwordInitializationService.confirmAuthNo(passwordInitializationConfirmationRequest)) {
            return ApiResponse.success();
        } else {
            return ApiResponse.failure(HttpStatus.UNAUTHORIZED, "인증 가능 시간이 지났거나 이메일 또는 인증번호가 올바르지 않습니다.");
        }
    }

    @ApiOperation(value = "비밀번호 변경 (인증번호 1회 유효)")
    @PutMapping
    public ApiResponse initializePassword(@Valid @RequestBody PasswordInitializationRequest passwordInitializationRequest) {
        try {
            passwordInitializationService.initializePassword(passwordInitializationRequest);
            return ApiResponse.success();
        } catch (EmailNotFoundException e) {
            return ApiResponse.failure(HttpStatus.RESOURCE_NOT_EXIST, e.getMessage());
        } catch (BadRequestException e) {
            return ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
