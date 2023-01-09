package com.bestbranch.geulboxapi.geul.report.controller.dto;

import com.bestbranch.geulboxapi.geul.report.domain.GeulReport;
import lombok.Getter;

@Getter
public class GeulReportRequest {
    private GeulReport.ReportReasonType reportReasonType;
    private String reportCustomReason;
}
