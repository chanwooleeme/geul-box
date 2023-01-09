package com.bestbranch.geulboxapi.user.profile.service;

import com.bestbranch.geulboxapi.common.cache.EhCacheService;
import com.bestbranch.geulboxapi.geul.repository.GeulQuerydslRepository;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.profile.service.dto.NicknameRegisterRequest;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NicknameService {
    private final UserSpringDataJpaRepository userRepository;
    private final GeulQuerydslRepository geulRepository;
    private final EhCacheService ehCacheService;

    @Transactional(readOnly = true)
    public Boolean isNicknameAlreadyExist(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    @Transactional
    public void saveNickname(NicknameRegisterRequest nicknameRegisterRequest, Long userNo) {
        User user = userRepository.findById(userNo).get();
        user.setNickname(nicknameRegisterRequest.getNickname());
//        List<Geul> geuls = geulRepository.getGeulsWithoutOrder(GeulSearchRequest.of(userNo));
//        geuls.forEach(geul -> ehCacheService.delete(CacheKeyNames.GEUL_DETAIL, String.valueOf(geul.getGeulNo())));
    }
}
