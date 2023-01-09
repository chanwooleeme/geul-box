package com.bestbranch.geulboxapi.user.join.controller;

import com.bestbranch.geulboxapi.common.exception.model.AlreadyExistsException;
import com.bestbranch.geulboxapi.common.exception.model.BadRequestException;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.HttpStatus;
import com.bestbranch.geulboxapi.user.domain.dto.UserView;
import com.bestbranch.geulboxapi.user.join.service.UserJoinService;
import com.bestbranch.geulboxapi.user.join.service.dto.UserJoinRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "일반 회원 가입 API 목록", tags = {"[USER] [GENERAL] [JOIN]"})
@RestController
@RequiredArgsConstructor
public class GeneralUserJoinController {
    private final UserJoinService generalUserJoinService;

    @ApiOperation(value = "일반 회원 가입", notes = "일반 회원가입, 만료기간 [accessToken|24시간] [refreshToken|30일]")
    @PostMapping(value = {"/users", "/v1.2/users"})
    public ApiResponse<UserView> joinUser(@RequestBody UserJoinRequest userJoinRequest) {

        try {
            return ApiResponse.success(HttpStatus.CREATED, generalUserJoinService.joinUser(userJoinRequest));
        } catch (BadRequestException e) {
            return ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (AlreadyExistsException e) {
            return ApiResponse.failure(HttpStatus.ALREADY_EXIST, e.getMessage());
        }
    }
}