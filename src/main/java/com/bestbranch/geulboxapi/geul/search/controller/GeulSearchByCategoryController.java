package com.bestbranch.geulboxapi.geul.search.controller;

import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.OrderType;
import com.bestbranch.geulboxapi.common.model.Paging;
import com.bestbranch.geulboxapi.geul.domain.Geul;
import com.bestbranch.geulboxapi.geul.search.service.GeulSearchService;
import com.bestbranch.geulboxapi.geul.search.service.dto.GeulSearchRequest;
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
public class GeulSearchByCategoryController {
    private final GeulSearchService geulSearchService;

    @ApiOperation(value = "카테고리별 최신글 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "이전 페이지의 마지막 글 no", name = "exclusiveStandardGeulNo", paramType = "query"),
            @ApiImplicitParam(value = "페이지 당 글 개수", name = "itemCount", paramType = "query")
    })
    @GetMapping("/v1.2/categories/{category}/geuls")
    public ApiResponse getGeulsByCategoryV1_2(@PathVariable Geul.Category category, @RequestParam(required = false) Integer itemCount,
                                              @RequestParam(required = false) Long exclusiveStandardGeulNo) {
        GeulSearchRequest param = GeulSearchRequest.of(OrderType.DESC, Paging.from(itemCount), category, exclusiveStandardGeulNo);
        return ApiResponse.success(geulSearchService.getGeulsByCategory(param));
    }

    @ApiOperation(value = "카테고리별 최신글 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
            @ApiImplicitParam(value = "이전 페이지의 마지막 글 no", name = "exclusiveStandardGeulNo", paramType = "query"),
            @ApiImplicitParam(value = "페이지 당 글 개수", name = "itemCount", paramType = "query")
    })
    @GetMapping("/v1.4/categories/{category}/geuls")
    @TokenAuthentication
    public ApiResponse getGeulsByCategoryV1_4(@PathVariable Geul.Category category, @RequestParam(required = false) Integer itemCount,
                                              @RequestParam(required = false) Long exclusiveStandardGeulNo,
                                              @ApiParam(hidden = true) @RequestAttribute("userNo") Long requestUserNo) {
        GeulSearchRequest param = GeulSearchRequest.of(OrderType.DESC, Paging.from(itemCount), category, exclusiveStandardGeulNo, requestUserNo);
        return ApiResponse.success(geulSearchService.getGeulsByCategory(param));
    }
}

