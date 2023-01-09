package com.bestbranch.geulboxapi.user.report.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "user_report")
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_report_no")
    @Getter
    private Long userReportNo;

    //신고자
    @Column(name = "reporter_user_no")
    private Long reporterUserNo;

    //피신고자
    @Column(name = "reported_user_no")
    private Long reportedUserNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_reason_type")
    @Getter
    private ReportReasonType reportReasonType;

    @Column(name = "report_custom_reason")
    private String reportCustomReason;

    @Getter
    @Column(name = "register_date_time")
    private LocalDateTime registerDateTime;

    @Column(name = "modify_date_time")
    private LocalDateTime modifyDateTime;

    protected UserReport() {
    }

    public static UserReport of(Long reporterUserNo, Long reportedUserNo, ReportReasonType reportReasonType) {
        return of(reporterUserNo, reportedUserNo, reportReasonType, null);
    }

    public static UserReport of(Long reporterUserNo, Long reportedUserNo, ReportReasonType reportReasonType, String reportCustomReason) {
        UserReport report = new UserReport();
        report.reporterUserNo = reporterUserNo;
        report.reportedUserNo = reportedUserNo;
        report.reportReasonType = reportReasonType;
        if (reportReasonType.equals(ReportReasonType.CUSTOM) && StringUtils.isBlank(reportCustomReason)) {
            throw new IllegalArgumentException("신고 직접입력 사유가 입력되지 않았습니다.");
        }
        report.reportCustomReason = reportCustomReason;
        return report;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ReportReasonType {
        PROMOTION("홍보성"),
        SPAM("도배성"),
        DIRTY("욕설/혐오"),
        OBSCENITY("음란성"),
        PRIVACY_LEAK("개인정보 유출"),
        CUSTOM("직접입력");

        private final String reason;
    }

    public String getReason() {
        if (this.reportReasonType.equals(ReportReasonType.CUSTOM)) {
            return this.reportCustomReason;
        }
        return this.reportReasonType.reason;
    }
}
