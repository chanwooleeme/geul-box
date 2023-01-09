package com.bestbranch.geulboxapi.geul.report.repository;

import com.bestbranch.geulboxapi.geul.report.domain.GeulReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeulReportSpringDataJpaRepository extends JpaRepository<GeulReport, Long> {
    Long countByGeulNo(Long geulNo);

    Long countByGeulNoAndUserNo(Long geulNo, Long userNo);

    List<GeulReport> findAllByGeulNo(Long geulNo);
}
