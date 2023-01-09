package com.bestbranch.geulboxapi.user.profile.controller;

import com.bestbranch.geulboxapi.common.exception.model.AlreadyExistsException;
import com.bestbranch.geulboxapi.common.exception.model.ResourceNotExistsException;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.HttpStatus;
import com.bestbranch.geulboxapi.user.domain.TokenAuthentication;
import com.bestbranch.geulboxapi.user.profile.service.NicknameService;
import com.bestbranch.geulboxapi.user.profile.service.ProfileService;
import com.bestbranch.geulboxapi.user.profile.service.dto.NicknameRegisterRequest;
import com.bestbranch.geulboxapi.user.profile.service.dto.ProfileUpdateRequest;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(description = "프로필 API 목록", tags = {"[USER] [PROFILE]"})
@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final NicknameService nicknameService;

    @ApiOperation(value = "회원 프로필 조회 (별명, 소개글)")
    @GetMapping("/v1.2/users/{userNo}/profile")
    public ApiResponse getProfile(@PathVariable Long userNo) {
        try {
            return ApiResponse.success(profileService.getProfileOf(userNo));
        } catch (ResourceNotExistsException e) {
            return ApiResponse.failure(HttpStatus.RESOURCE_NOT_EXIST, e.getMessage());
        }
    }

    @ApiOperation(value = "회원 프로필 업데이트 (별명, 소개글)")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
    })
    @PutMapping("/v1.2/users/profile")
    @TokenAuthentication
    public ApiResponse saveProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest,
                                   @ApiParam(hidden = true) @RequestAttribute Long userNo) {
        try {
            profileService.saveProfile(profileUpdateRequest, userNo);
            return ApiResponse.success();
        } catch (AlreadyExistsException e) {
            return ApiResponse.failure(HttpStatus.ALREADY_EXIST, e.getMessage());
        } catch (ResourceNotExistsException e) {
            return ApiResponse.failure(HttpStatus.RESOURCE_NOT_EXIST, e.getMessage());
        }
    }

    @ApiOperation(value = "회원 별명 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
    })
    @PostMapping("/v1.1/users/nicknames")
    @TokenAuthentication
    public ApiResponse saveNickname(@RequestBody NicknameRegisterRequest nicknameRegisterRequest, @ApiParam(hidden = true) @RequestAttribute Long userNo) {
        nicknameService.saveNickname(nicknameRegisterRequest, userNo);
        return ApiResponse.success();

    }

    @ApiOperation(value = "회원 별명 중복 검사")
    @GetMapping("/v1.1/users/nicknames/{nickname}")
    public ApiResponse<Map<String, Boolean>> getNicknameV1_1(@PathVariable String nickname) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("isNicknameAlreadyExist", nicknameService.isNicknameAlreadyExist(nickname.trim()));
        return ApiResponse.success(result);

    }
}
