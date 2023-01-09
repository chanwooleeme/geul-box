package com.bestbranch.geulboxapi.geul.report.domain;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "geul_report")
@Getter
public class GeulReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "geul_report_no")
    private Long geulReportNo;

    @Column(name = "geul_no")
    private Long geulNo;

    @Column(name = "user_no")
    private Long userNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_reason_type")
    private ReportReasonType reportReasonType;

    @Column(name = "report_custom_reason")
    private String reportCustomReason;

    @Column(name = "register_date_time")
    private LocalDateTime registerDateTime;

    @Column(name = "modify_date_time")
    private LocalDateTime modifyDateTime;


    protected GeulReport() {
    }

    public static GeulReport of(Long reporterUserNo, Long geulNo, ReportReasonType reportReasonType) {
        return of(reporterUserNo, geulNo, reportReasonType, null);
    }

    public static GeulReport of(Long reporterUserNo, Long geulNo, ReportReasonType reportReasonType, String reportCustomReason) {
        GeulReport report = new GeulReport();
        report.userNo = reporterUserNo;
        report.geulNo = geulNo;
        report.reportReasonType = reportReasonType;
        if (reportReasonType.equals(ReportReasonType.CUSTOM) && StringUtils.isBlank(reportCustomReason)) {
            throw new IllegalArgumentException("신고 직접입력 사유가 입력되지 않았습니다.");
        }
        report.reportCustomReason = reportCustomReason;
        return report;
    }

    public enum ReportReasonType {
        PROMOTION,    // 홍보성
        SPAM,         // 도배성
        DIRTY,        // 욕설/혐오
        OBSCENITY,    // 음란성
        PRIVACY_LEAK, // 개인정보 유출
        CUSTOM        // 직접입력(기타)
    }
}
