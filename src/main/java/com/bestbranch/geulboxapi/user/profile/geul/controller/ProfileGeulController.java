package com.bestbranch.geulboxapi.user.profile.geul.controller;

import com.bestbranch.geulboxapi.common.exception.model.UnauthorizedException;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.HttpStatus;
import com.bestbranch.geulboxapi.common.model.OrderType;
import com.bestbranch.geulboxapi.common.model.Paging;
import com.bestbranch.geulboxapi.geul.search.service.GeulSearchService;
import com.bestbranch.geulboxapi.geul.search.service.dto.GeulSearchRequest;
import com.bestbranch.geulboxapi.user.domain.TokenAuthentication;
import com.bestbranch.geulboxapi.user.profile.geul.service.ProfileGeulService;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Api(description = "프로필 글 조회 API 목록", tags = {"[USER] [PROFILE] [GEUL]"})
@RestController
@RequiredArgsConstructor
public class ProfileGeulController {
    private final ProfileGeulService profileGeulService;
    private final GeulSearchService geulSearchService;

    @ApiOperation(value = "회원 글 목록 조회(프로필 미존재 버전)")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
            @ApiImplicitParam(value = "yyyy-MM-dd", name = "searchDate", paramType = "query")
    })
    @GetMapping("/users/{userNo}/geuls")
    @TokenAuthentication
    public ApiResponse getGeulsOfUser(@PathVariable("userNo") Long geulHostUserNo, @RequestParam OrderType orderType,
                                      @RequestParam(required = false) Integer itemCount,
                                      @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate,
                                      @ApiParam(hidden = true) @RequestAttribute("userNo") Long userNoOfViewer) {
        try {
            if (!geulHostUserNo.equals(userNoOfViewer)) {
                throw new UnauthorizedException("글을 조회할 수 있는 권한이 없습니다.");
            }
            GeulSearchRequest param = GeulSearchRequest.of(geulHostUserNo, orderType, Paging.of(itemCount, page), searchDate);
            return ApiResponse.success(geulSearchService.getPagedGeulsOfUser(param));
        } catch (UnauthorizedException e) {
            return ApiResponse.failure(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @ApiOperation(value = "프로필 글 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
            @ApiImplicitParam(value = "yyyy-MM-dd", name = "searchDate", paramType = "query")
    })
    @GetMapping("/v1.2/users/{userNo}/geuls")
    @TokenAuthentication
    public ApiResponse getGeulsOfUserV1_2(@PathVariable("userNo") Long geulHostUserNo,
                                          @RequestParam(required = false) Integer itemCount,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate,
                                          @ApiParam(hidden = true) @RequestAttribute("userNo") Long userNoOfViewer,
                                          @RequestParam(required = false) Long exclusiveStandardGeulNo) {
        GeulSearchRequest param = GeulSearchRequest.of(geulHostUserNo, Paging.from(itemCount), searchDate, geulHostUserNo.equals(userNoOfViewer), exclusiveStandardGeulNo);
        return ApiResponse.success(profileGeulService.getProfileGeuls(param));
    }

    @ApiOperation(value = "프로필 글 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
            @ApiImplicitParam(value = "yyyy-MM-dd", name = "searchDate", paramType = "query")
    })
    @GetMapping("/v1.4/users/{userNo}/geuls")
    @TokenAuthentication
    public ApiResponse getGeulsOfUserV1_4(@PathVariable("userNo") Long geulHostUserNo,
                                          @RequestParam(required = false) Integer itemCount,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate,
                                          @ApiParam(hidden = true) @RequestAttribute("userNo") Long userNoOfViewer,
                                          @RequestParam(required = false) Long exclusiveStandardGeulNo) {
        GeulSearchRequest param = GeulSearchRequest.of(geulHostUserNo, Paging.from(itemCount), searchDate, geulHostUserNo.equals(userNoOfViewer), exclusiveStandardGeulNo, userNoOfViewer);
        return ApiResponse.success(profileGeulService.getProfileGeuls(param));
    }
}
