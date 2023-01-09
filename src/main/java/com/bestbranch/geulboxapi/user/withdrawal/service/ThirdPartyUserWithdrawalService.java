package com.bestbranch.geulboxapi.user.withdrawal.service;

import com.bestbranch.geulboxapi.geul.repository.GeulQuerydslRepository;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import com.bestbranch.geulboxapi.user.withdrawal.service.dto.UserWithdrawalRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThirdPartyUserWithdrawalService {
    private final UserSpringDataJpaRepository userRepository;
    private final GeulQuerydslRepository geulRepository;

    @Transactional
    public void withdrawalUser(UserWithdrawalRequest userWithdrawalRequest) {
        Optional<User> user = userRepository.findByThirdPartyAccountId(userWithdrawalRequest.getThirdPartyAccountId());
        if (user.isPresent()) {
            userRepository.delete(user.get());
            geulRepository.deleteByUserNo(user.get().getUserNo());
        }
    }
}
