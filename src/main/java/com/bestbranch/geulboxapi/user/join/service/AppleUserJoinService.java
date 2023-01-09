package com.bestbranch.geulboxapi.user.join.service;

import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.domain.dto.UserView;
import com.bestbranch.geulboxapi.user.join.service.dto.UserJoinRequest;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppleUserJoinService implements UserJoinService {
    private final UserSpringDataJpaRepository userRepository;

    @Transactional
    @Override
    public UserView joinUser(UserJoinRequest param) {
        Optional<User> nullableUser = userRepository.findByThirdPartyAccountIdAndUserAuthenticationType(param.getThirdPartyAccountId(), User.AuthenticationType.APPLE);
        User user;
        if (!nullableUser.isPresent()) {
            user = User.from(param);
        } else {
            user = nullableUser.get();
            user.setRefreshToken(param.getRefreshToken());
            user.setOS(param.getOsType(), param.getOsVersion());
        }
        userRepository.save(user);
        return UserView.from(user);
    }
}
