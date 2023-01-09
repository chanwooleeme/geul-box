package com.bestbranch.geulboxapi.geul.report.service;

import com.bestbranch.geulboxapi.common.exception.model.ApiException;
import com.bestbranch.geulboxapi.common.exception.model.ErrorCode;
import com.bestbranch.geulboxapi.geul.domain.Geul;
import com.bestbranch.geulboxapi.geul.report.domain.GeulReport;
import com.bestbranch.geulboxapi.geul.report.repository.GeulReportSpringDataJpaRepository;
import com.bestbranch.geulboxapi.geul.repository.GeulSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GeulReportService {
    private final GeulReportSpringDataJpaRepository geulReportSpringDataJpaRepository;
    private final GeulSpringDataJpaRepository geulSpringDataJpaRepository;

    @Transactional
    public void reportGeul(Long reporterUserNo, Long geulNo, GeulReport.ReportReasonType reportReasonType, String reportCustomReason) {
        if (isAlreadyReportedBy(geulNo, reporterUserNo)) {
            throw new ApiException(ErrorCode.GEUL_ALREADY_REPORTED);
        }

        GeulReport geulReport;
        if (reportReasonType.equals(GeulReport.ReportReasonType.CUSTOM)) {
            geulReport = GeulReport.of(reporterUserNo, geulNo, reportReasonType, reportCustomReason);
        } else {
            geulReport = GeulReport.of(reporterUserNo, geulNo, reportReasonType);
        }

        geulReportSpringDataJpaRepository.save(geulReport);

        if (isGeulReportAccumulatedThreeTimes(geulNo)) {
            Geul geul = geulSpringDataJpaRepository.findById(geulNo).orElseThrow(() -> new ApiException(ErrorCode.GEUL_NOT_FOUND));
            geul.hide();
        }
    }

    private Boolean isAlreadyReportedBy(Long geulNo, Long reporterUserNo) {
        return geulReportSpringDataJpaRepository.countByGeulNoAndUserNo(geulNo, reporterUserNo) > 0;
    }

    private Boolean isGeulReportAccumulatedThreeTimes(Long geulNo) {
        return geulReportSpringDataJpaRepository.countByGeulNo(geulNo) >= 3;
    }
}
