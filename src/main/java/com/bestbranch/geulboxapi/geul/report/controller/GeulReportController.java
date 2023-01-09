package com.bestbranch.geulboxapi.geul.report.controller;

import com.bestbranch.geulboxapi.common.config.swagger.ApiResp;
import com.bestbranch.geulboxapi.common.config.swagger.ApiResps;
import com.bestbranch.geulboxapi.common.exception.model.ApiException;
import com.bestbranch.geulboxapi.common.exception.model.ErrorCode;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.geul.report.controller.dto.GeulReportRequest;
import com.bestbranch.geulboxapi.geul.report.domain.GeulReport;
import com.bestbranch.geulboxapi.geul.report.service.GeulReportService;
import com.bestbranch.geulboxapi.user.domain.TokenAuthentication;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(description = "글 신고 API 목록", tags = {"[GEUL] [REPORT]"})
@RestController
@RequiredArgsConstructor
public class GeulReportController {
    private final GeulReportService geulReportService;

    @ApiOperation(value = "글 신고 하기")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
    })
    @ApiResps(value = {
            @ApiResp(code = 200, message = "신고 완료"),

            @ApiResp(errorCode = ErrorCode.USER_AUTHENTICATION_TYPE_OR_TOKEN_IS_NULL),
            @ApiResp(errorCode = ErrorCode.TOKEN_IS_EXPIRED),
            @ApiResp(errorCode = ErrorCode.USER_NOT_JOINED),
            @ApiResp(errorCode = ErrorCode.INVALID_TOKEN),

            @ApiResp(errorCode = ErrorCode.NEED_GEUL_REPORT_CUSTOM_REASON),
            @ApiResp(errorCode = ErrorCode.GEUL_ALREADY_REPORTED),
            @ApiResp(errorCode = ErrorCode.GEUL_NOT_FOUND)
    })
    @PostMapping("/geuls/{geulNo}/report")
    @TokenAuthentication
    public ApiResponse report(@ApiParam(hidden = true) @RequestAttribute Long userNo, @PathVariable Long geulNo, @RequestBody GeulReportRequest geulReportRequest) {
        if (geulReportRequest.getReportReasonType().equals(GeulReport.ReportReasonType.CUSTOM) && StringUtils.isBlank(geulReportRequest.getReportCustomReason())) {
            throw new ApiException(ErrorCode.NEED_GEUL_REPORT_CUSTOM_REASON);
        }
        geulReportService.reportGeul(userNo, geulNo, geulReportRequest.getReportReasonType(), geulReportRequest.getReportCustomReason());
        return ApiResponse.success();
    }
}