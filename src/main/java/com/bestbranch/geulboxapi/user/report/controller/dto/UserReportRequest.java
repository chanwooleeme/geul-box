package com.bestbranch.geulboxapi.user.report.controller.dto;

import com.bestbranch.geulboxapi.user.report.domain.UserReport;
import lombok.Getter;

@Getter
public class UserReportRequest {
    private UserReport.ReportReasonType reportReasonType;
    private String reportCustomReason;
}
