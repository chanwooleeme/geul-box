package com.bestbranch.geulboxapi.user.login.service;

import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.login.service.dto.AppEnvironment;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import com.bestbranch.geulboxapi.user.withdrawal.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ThirdPartyUserLoginService implements UserLoginService {
    private final UserSpringDataJpaRepository userRepository;

    @Transactional
    public void updateLoginDateTimeAndEnvironment(AppEnvironment environment, Long userNo) {
        User user = userRepository.findById(userNo).orElseThrow(UserNotFoundException::new);
        user.setEnvironment(environment);
        user.setLastLoginDateTime(LocalDateTime.now());
    }
}
