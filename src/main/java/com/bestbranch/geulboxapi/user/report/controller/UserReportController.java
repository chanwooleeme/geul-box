package com.bestbranch.geulboxapi.user.report.controller;

import com.bestbranch.geulboxapi.common.config.swagger.ApiResp;
import com.bestbranch.geulboxapi.common.config.swagger.ApiResps;
import com.bestbranch.geulboxapi.common.exception.model.ApiException;
import com.bestbranch.geulboxapi.common.exception.model.ErrorCode;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.geul.report.domain.GeulReport;
import com.bestbranch.geulboxapi.user.domain.TokenAuthentication;
import com.bestbranch.geulboxapi.user.report.controller.dto.UserReportRequest;
import com.bestbranch.geulboxapi.user.report.service.UserReportService;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(description = "회원 신고 API 목록", tags = {"[USER] [REPORT]"})
@RestController
@RequiredArgsConstructor
public class UserReportController {
    private final UserReportService userReportService;

    @ApiOperation(value = "회원 신고 하기")
    @ApiResps(value = {
            @ApiResp(code = 200, message = "신고 완료"),

            @ApiResp(errorCode = ErrorCode.USER_AUTHENTICATION_TYPE_OR_TOKEN_IS_NULL),
            @ApiResp(errorCode = ErrorCode.TOKEN_IS_EXPIRED),
            @ApiResp(errorCode = ErrorCode.USER_NOT_JOINED),
            @ApiResp(errorCode = ErrorCode.INVALID_TOKEN),

            @ApiResp(errorCode = ErrorCode.NEED_USER_REPORT_CUSTOM_REASON),
            @ApiResp(errorCode = ErrorCode.USER_NOT_FOUND),
            @ApiResp(errorCode = ErrorCode.USER_IS_BLOCKED),
            @ApiResp(errorCode = ErrorCode.USER_ALREADY_REPORTED)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
    })
    @PostMapping("/users/{userNo}/report")
    @TokenAuthentication
    public ApiResponse report(@ApiParam(hidden = true) @RequestAttribute("userNo") Long reporterUserNo, @PathVariable("userNo") Long reportedUserNo, @RequestBody UserReportRequest userReportRequest) {
        if (userReportRequest.getReportReasonType().equals(GeulReport.ReportReasonType.CUSTOM) && StringUtils.isBlank(userReportRequest.getReportCustomReason())) {
            throw new ApiException(ErrorCode.NEED_USER_REPORT_CUSTOM_REASON);
        }
        userReportService.reportUser(reporterUserNo, reportedUserNo, userReportRequest.getReportReasonType(), userReportRequest.getReportCustomReason());
        return ApiResponse.success();
    }
}
