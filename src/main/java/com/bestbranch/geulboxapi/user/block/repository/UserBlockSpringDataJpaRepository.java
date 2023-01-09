package com.bestbranch.geulboxapi.user.block.repository;

import com.bestbranch.geulboxapi.geul.report.domain.GeulReport;
import com.bestbranch.geulboxapi.user.block.domain.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBlockSpringDataJpaRepository extends JpaRepository<UserBlock, Long> {

    Long deleteUserBlockByBlockerUserNoAndBlockedUserNo(Long blockerUserNo, Long blockedUserNo);

    Optional<UserBlock> findUserBlockByBlockerUserNoAndBlockedUserNo(Long blockerUserNo, Long blockedUserNo);
}
