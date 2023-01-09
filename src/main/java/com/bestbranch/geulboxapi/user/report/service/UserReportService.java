package com.bestbranch.geulboxapi.user.report.service;

import com.bestbranch.geulboxapi.common.exception.model.ApiException;
import com.bestbranch.geulboxapi.common.exception.model.ErrorCode;
import com.bestbranch.geulboxapi.geul.domain.Geul;
import com.bestbranch.geulboxapi.geul.repository.GeulSpringDataJpaRepository;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.report.domain.UserReport;
import com.bestbranch.geulboxapi.user.report.repository.UserReportSpringDataJpaRepository;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserReportService {
    private final UserReportSpringDataJpaRepository userReportSpringDataJpaRepository;
    private final UserSpringDataJpaRepository userSpringDataJpaRepository;
    private final GeulSpringDataJpaRepository geulSpringDataJpaRepository;

    @Transactional
    public void reportUser(Long reporterUserNo, Long reportedUserNo, UserReport.ReportReasonType reportReasonType, String reportCustomReason) {
        User user = userSpringDataJpaRepository.findById(reportedUserNo).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (user.isBlocked()) {
            throw new ApiException(ErrorCode.USER_IS_BLOCKED);
        }

        if (isAlreadyReportedBy(reporterUserNo, reportedUserNo)) {
            throw new ApiException(ErrorCode.USER_ALREADY_REPORTED);
        }

        UserReport userReport;
        if (reportReasonType.equals(UserReport.ReportReasonType.CUSTOM)) {
            userReport = UserReport.of(reporterUserNo, reportedUserNo, reportReasonType, reportCustomReason);
        } else {
            userReport = UserReport.of(reporterUserNo, reportedUserNo, reportReasonType);
        }
        userReportSpringDataJpaRepository.save(userReport);

        if (isUserReportAccumulatedThreeTimes(reportedUserNo)) {
            user.block();
            geulSpringDataJpaRepository.findByUserNo(reportedUserNo).forEach(Geul::hide);
        }
    }

    private Boolean isAlreadyReportedBy(Long reporterUserNo, Long reportedUserNo) {
        return userReportSpringDataJpaRepository.countByReporterUserNoAndReportedUserNo(reporterUserNo, reportedUserNo) > 0;
    }

    private Boolean isUserReportAccumulatedThreeTimes(Long reportedUserNo) {
        return userReportSpringDataJpaRepository.countByReportedUserNo(reportedUserNo) >= 3;
    }

    @Transactional(readOnly = true)
    public List<UserReport> getUserReportsOfBlockedUser(User user) {
        if (user.isBlocked()) {
            return userReportSpringDataJpaRepository.findByReportedUserNoOrderByRegisterDateTimeDesc(user.getUserNo());
        }
        return Collections.emptyList();
    }

    public UserReport getUserReport(User user) {
        List<UserReport> userReports = getUserReportsOfBlockedUser(user);

        if (allEqualsUserReports(userReports) || allDifferentReports(userReports)) {
            return userReports.get(0);
        }

        return getMostReport(userReports);
    }

    private UserReport getMostReport(List<UserReport> userReports) {
        Set<UserReport.ReportReasonType> userReportTypeSet = new HashSet<>();
        UserReport.ReportReasonType mostUserReportType = null;
        for (UserReport userReport : userReports) {
            boolean isMostReport = !userReportTypeSet.add(userReport.getReportReasonType());
            if (isMostReport) {
                mostUserReportType = userReport.getReportReasonType();
                break;
            }
        }

        for (UserReport userReport : userReports) {
            if (userReport.getReportReasonType() == mostUserReportType) {
                return userReport;
            }
        }

        throw new IllegalArgumentException("가장 많이 신고된 유형을 추출할 수 없습니다.");
    }

    private Boolean allDifferentReports(List<UserReport> userReports) {
        Set<UserReport.ReportReasonType> userReportTypeSet = new HashSet<>();
        userReports.forEach(userReport -> userReportTypeSet.add(userReport.getReportReasonType()));
        return userReportTypeSet.size() == userReports.size();
    }

    private Boolean allEqualsUserReports(List<UserReport> userReports) {
        return userReports.stream().allMatch(userReport -> userReports.get(0).getReportReasonType() == userReport.getReportReasonType());
    }
}
