package com.bestbranch.geulboxapi.user.profile.service;

import com.bestbranch.geulboxapi.common.cache.EhCacheService;
import com.bestbranch.geulboxapi.common.exception.model.AlreadyExistsException;
import com.bestbranch.geulboxapi.common.exception.model.ResourceNotExistsException;
import com.bestbranch.geulboxapi.geul.repository.GeulQuerydslRepository;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.profile.service.dto.Profile;
import com.bestbranch.geulboxapi.user.profile.service.dto.ProfileUpdateRequest;
import com.bestbranch.geulboxapi.user.report.service.UserReportService;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserSpringDataJpaRepository userRepository;
    private final NicknameService nicknameService;
    private final UserReportService userReportService;
    private final GeulQuerydslRepository geulRepository;
    private final EhCacheService ehCacheService;

    @Transactional(readOnly = true)
    public Profile getProfileOf(Long userNo) throws ResourceNotExistsException {
        User user = userRepository.findById(userNo).orElseThrow(() -> new ResourceNotExistsException("존재하지 않는 회원입니다."));
        return Profile.of(user.getNickname(), user.getIntroduction(), user.getStatus(), user.isBlocked() ? userReportService.getUserReport(user) : null);
    }

    @Transactional
    public void saveProfile(ProfileUpdateRequest profile, Long userNo) throws ResourceNotExistsException, AlreadyExistsException {
        User user = userRepository.findById(userNo).orElseThrow(() -> new ResourceNotExistsException("존재하지 않는 회원입니다."));

        if (nicknameService.isNicknameAlreadyExist(profile.getNickname())
                && (user.isNickNameEmpty() || !user.equalsNickName(profile.getNickname()))) {
            throw new AlreadyExistsException("이미 사용중인 별명입니다.");
        }

        user.setProfile(profile.getNickname(), profile.getIntroduction());
        //List<Geul> geuls = geulRepository.getGeulsWithoutOrder(GeulSearchRequest.of(userNo));
        //geuls.forEach(geul -> ehCacheService.delete(CacheKeyNames.GEUL_DETAIL, String.valueOf(geul.getGeulNo())));
    }
}
