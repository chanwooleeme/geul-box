package com.bestbranch.geulboxapi.user.withdrawal.service;

import com.bestbranch.geulboxapi.common.component.mail.UserWithdrawalMailSender;
import com.bestbranch.geulboxapi.common.component.mail.UserWithdrawalNotificationInAdvanceMailSender;
import com.bestbranch.geulboxapi.geul.repository.GeulQuerydslRepository;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import com.bestbranch.geulboxapi.user.withdrawal.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserWithdrawalService {
    private final UserSpringDataJpaRepository userRepository;
    private final GeulQuerydslRepository geulRepository;
    private final UserWithdrawalMailSender userWithdrawalMailSender;
    private final UserWithdrawalNotificationInAdvanceMailSender userWithdrawalNotificationInAdvanceMailSender;

    @Transactional
    public void withdrawalUser(Long userNo) {
        User user = userRepository.findById(userNo).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
        geulRepository.deleteByUserNo(userNo);
    }

    public void notifyWithdrawalUnusedUser() {
        LocalDateTime standardDateTime = LocalDateTime.now().minusYears(1);
        List<User> users = userRepository.findByLastLoginDateTimeIsNullOrLastLoginDateTimeLessThan(standardDateTime);

        users.forEach(user -> {
            withdrawalUser(user.getUserNo());
            if (StringUtils.isNotBlank(user.getEmail())) {
                userWithdrawalMailSender.send(user.getEmail());
            }
        });

    }

    public void notifyWithdrawalUnusedUserInAdvance() {
        LocalDateTime standardDateTime = LocalDateTime.now().plusDays(30).minusYears(1);
        List<User> users = userRepository.findByLastLoginDateTimeIsNullOrLastLoginDateTimeLessThan(standardDateTime);

        users.forEach(user -> {
            if (StringUtils.isNotBlank(user.getEmail())) {
                userWithdrawalNotificationInAdvanceMailSender.send(user.getEmail());
            }
        });

    }
}
