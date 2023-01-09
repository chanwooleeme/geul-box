package com.bestbranch.geulboxapi.user.block.service;

import com.bestbranch.geulboxapi.common.exception.model.ApiException;
import com.bestbranch.geulboxapi.common.exception.model.ErrorCode;
import com.bestbranch.geulboxapi.user.block.domain.UserBlock;
import com.bestbranch.geulboxapi.user.block.repository.UserBlockSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBlockService {

    private final UserBlockSpringDataJpaRepository repository;

    @Transactional
    public void blockUser(Long blockerUserNo, Long blockedUserNo) {
        if (isBlocked(blockerUserNo, blockedUserNo)) {
            throw new ApiException(ErrorCode.USER_ALREADY_BLOCKED);
        }
        repository.save(UserBlock.of(blockerUserNo, blockedUserNo));
    }

    @Transactional
    public void unblockUser(Long blockerUserNo, Long blockedUserNo) {
        if (!isBlocked(blockerUserNo, blockedUserNo)) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }
        repository.deleteUserBlockByBlockerUserNoAndBlockedUserNo(blockerUserNo, blockedUserNo);
    }

    private Boolean isBlocked(Long blockerUserNo, Long blockedUserNo) {
        return repository.findUserBlockByBlockerUserNoAndBlockedUserNo(blockerUserNo, blockedUserNo).isPresent();
    }
}
