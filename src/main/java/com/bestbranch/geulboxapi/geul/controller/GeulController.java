package com.bestbranch.geulboxapi.geul.controller;

import com.bestbranch.geulboxapi.common.exception.model.ResourceNotExistsException;
import com.bestbranch.geulboxapi.common.exception.model.UnauthorizedException;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.HttpStatus;
import com.bestbranch.geulboxapi.geul.service.GeulService;
import com.bestbranch.geulboxapi.geul.service.dto.GeulRegisterRequest;
import com.bestbranch.geulboxapi.geul.service.dto.GeulUpdateRequest;
import com.bestbranch.geulboxapi.user.domain.TokenAuthentication;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(description = "글 API 목록", tags = {"[GEUL]"})
@RestController
@RequiredArgsConstructor
public class GeulController {
    private final GeulService geulService;

    @ApiOperation(value = "글 작성")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE")
    })
    @PostMapping("/geuls")
    @TokenAuthentication
    public ApiResponse registerGeul(@ApiParam(hidden = true) @RequestAttribute Long userNo, @RequestBody GeulRegisterRequest geulRegisterRequest) {
        try {
            geulService.registerGeul(geulRegisterRequest, userNo);
            return ApiResponse.success(HttpStatus.CREATED.getStatusCode());
        } catch (ResourceNotExistsException e) {
            return ApiResponse.failure(HttpStatus.RESOURCE_NOT_EXIST, e.getMessage());
        }
    }

    @ApiOperation(value = "글 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE")
    })
    @PutMapping("/geuls/{geulNo}")
    @TokenAuthentication
    public ApiResponse updateGeul(@PathVariable Long geulNo, @RequestBody GeulUpdateRequest geulUpdateRequest,
                                  @ApiParam(hidden = true) @RequestAttribute Long userNo) {
        try {
            geulService.updateGeul(geulNo, geulUpdateRequest, userNo);
            return ApiResponse.success();
        } catch (ResourceNotExistsException e) {
            return ApiResponse.failure(HttpStatus.RESOURCE_NOT_EXIST, e.getMessage());
        } catch (UnauthorizedException e) {
            return ApiResponse.failure(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiOperation(value = "글 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE")
    })
    @DeleteMapping("/geuls/{geulNo}")
    @TokenAuthentication
    public ApiResponse deleteGeul(@PathVariable Long geulNo, @ApiParam(hidden = true) @RequestAttribute Long userNo) {
        try {
            geulService.removeGeul(geulNo, userNo);
            return ApiResponse.success();
        } catch (ResourceNotExistsException e) {
            return ApiResponse.failure(HttpStatus.RESOURCE_NOT_EXIST, e.getMessage());
        } catch (UnauthorizedException e) {
            return ApiResponse.failure(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
