package com.bestbranch.geulboxapi.user.withdrawal.service;

import com.bestbranch.geulboxapi.client.kakao.KakaoApiClient;
import com.bestbranch.geulboxapi.client.kakao.model.KakaoResponse;
import com.bestbranch.geulboxapi.geul.repository.GeulQuerydslRepository;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoUserWithdrawalService {
    private final UserSpringDataJpaRepository userRepository;
    private final GeulQuerydslRepository geulRepository;
    private final KakaoApiClient kakaoApiClient;

    @Transactional
    public void withdrawalUser(String accessToken) {
        KakaoResponse.User kakaoUser = kakaoApiClient.getKakaoAccountBy(accessToken);
        Optional<User> user = userRepository.findByThirdPartyAccountIdAndUserAuthenticationType(String.valueOf(kakaoUser.getId()), User.AuthenticationType.KAKAO);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            geulRepository.deleteByUserNo(user.get().getUserNo());
        }
    }
}
