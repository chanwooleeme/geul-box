package com.bestbranch.geulboxapi.geul.search.controller;

import com.bestbranch.geulboxapi.common.exception.model.UnauthorizedException;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.HttpStatus;
import com.bestbranch.geulboxapi.geul.search.service.GeulSearchService;
import com.bestbranch.geulboxapi.user.domain.TokenAuthentication;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(description = "글 조회 API 목록", tags = {"[GEUL] [SEARCH]"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class GeulSearchByDateController {
    private final GeulSearchService geulSearchService;

    @ApiOperation(value = "월별 글을 작성한 일자 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE")
    })
    @TokenAuthentication
    @GetMapping("/users/{userNo}/days-geul-count-of-month")
    public ApiResponse getGeulCountByMonth(@PathVariable Long userNo, @RequestParam Integer searchYear, @RequestParam Integer searchMonth,
                                           @ApiParam(hidden = true) @RequestAttribute("userNo") Long requestUserNo) {
        try {
            if (!userNo.equals(requestUserNo)) {
                throw new UnauthorizedException("글을 조회할 수 있는 권한이 없습니다.");
            }
            return ApiResponse.success(geulSearchService.getDaysGeulCountOfMonth(userNo, searchYear, searchMonth));
        } catch (UnauthorizedException e) {
            return ApiResponse.failure(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @ApiOperation(value = "월별 글을 작성한 일자 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE")
    })
    @TokenAuthentication
    @GetMapping("/v1.2/users/{userNo}/days-geul-count-of-month")
    public ApiResponse getGeulCountByMonthV1_2(@PathVariable Long userNo, @RequestParam Integer searchYear, @RequestParam Integer searchMonth,
                                               @ApiParam(hidden = true) @RequestAttribute("userNo") Long requestUserNo) {
        try {
            if (!userNo.equals(requestUserNo)) {
                throw new UnauthorizedException("글을 조회할 수 있는 권한이 없습니다.");
            }
            return ApiResponse.success(geulSearchService.getDaysGeulCountOfMonth(userNo, searchYear, searchMonth));
        } catch (UnauthorizedException e) {
            return ApiResponse.failure(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}

