package com.bestbranch.geulboxapi.geul.search.controller;

import com.bestbranch.geulboxapi.common.exception.model.ResourceNotExistsException;
import com.bestbranch.geulboxapi.common.exception.model.UnauthorizedException;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.HttpStatus;
import com.bestbranch.geulboxapi.geul.search.service.GeulSearchService;
import com.bestbranch.geulboxapi.geul.search.service.dto.GeulView;
import com.bestbranch.geulboxapi.user.domain.TokenAuthentication;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "글 조회 API 목록", tags = {"[GEUL] [SEARCH]"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class GeulDetailSearchController {
    private final GeulSearchService geulSearchService;

    @ApiOperation(value = "글 상세 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE")
    })
    @TokenAuthentication
    @GetMapping("/geuls/{geulNo}")
    public ApiResponse<GeulView> getGeulDetail(@PathVariable Long geulNo, @ApiParam(hidden = true) @RequestAttribute Long userNo) {
        try {
            GeulView geulResponse = geulSearchService.getGeulDetailBy(geulNo, userNo);
            if (!geulResponse.getUser().getUserNo().equals(userNo)) {
                throw new UnauthorizedException("글을 조회할 수 있는 권한이 없습니다.");
            }
            return ApiResponse.success(geulResponse);
        } catch (ResourceNotExistsException e) {
            return ApiResponse.failure(HttpStatus.RESOURCE_NOT_EXIST, e.getMessage());
        } catch (UnauthorizedException e) {
            return ApiResponse.failure(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @ApiOperation(value = "글 상세 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE")
    })
    @TokenAuthentication
    @GetMapping("/v1.2/geuls/{geulNo}")
    public ApiResponse<GeulView> getGeulDetailV1_2(@PathVariable Long geulNo, @ApiParam(hidden = true) @RequestAttribute Long userNo) {
        try {
            GeulView geulResponse = geulSearchService.getGeulDetailBy(geulNo, userNo);
            if (!geulResponse.canView(userNo)) {
                throw new UnauthorizedException("글을 조회할 수 있는 권한이 없습니다.");
            }
            return ApiResponse.success(geulResponse);
        } catch (ResourceNotExistsException e) {
            return ApiResponse.failure(HttpStatus.RESOURCE_NOT_EXIST, e.getMessage());
        } catch (UnauthorizedException e) {
            return ApiResponse.failure(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}

