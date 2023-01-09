package com.bestbranch.geulboxapi.user.login.service;

import com.bestbranch.geulboxapi.common.exception.model.InvalidTokenException;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KakaoUserAuthTokenService {
    private final UserSpringDataJpaRepository userRepository;

    @Transactional
    public void modifyAccessToken(String originalAccessToken, String newAccessToken) {
        User user = userRepository.findByAccessToken(originalAccessToken).orElseThrow(() -> new InvalidTokenException("올바르지 않은 토큰입니다."));
        user.setAccessToken(newAccessToken);
    }
}
