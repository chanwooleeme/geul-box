package com.bestbranch.geulboxapi.user.report.repository;

import com.bestbranch.geulboxapi.user.report.domain.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReportSpringDataJpaRepository extends JpaRepository<UserReport, Long> {
    Long countByReportedUserNo(Long reportedUserNo);

    Long countByReporterUserNoAndReportedUserNo(Long reporterUserNo, Long reportedUserNo);

    List<UserReport> findByReportedUserNoOrderByRegisterDateTimeDesc(Long reportedUserNo);
}
