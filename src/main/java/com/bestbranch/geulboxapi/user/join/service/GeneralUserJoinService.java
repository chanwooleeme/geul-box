package com.bestbranch.geulboxapi.user.join.service;

import com.bestbranch.geulboxapi.common.exception.model.AlreadyExistsException;
import com.bestbranch.geulboxapi.common.exception.model.BadRequestException;
import com.bestbranch.geulboxapi.common.model.JwtToken;
import com.bestbranch.geulboxapi.common.utils.jwt.GeneralJwtUtils;
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
public class GeneralUserJoinService implements UserJoinService {
    private final UserSpringDataJpaRepository userRepository;

    @Transactional
    @Override
    public UserView joinUser(UserJoinRequest param) throws AlreadyExistsException, BadRequestException {
        param.setUserAuthenticationType(User.AuthenticationType.GENERAL);
        param.validateForGeneralUser();
        Optional<User> nullableUser = userRepository.findByEmailAndUserAuthenticationType(param.getEmail(), User.AuthenticationType.GENERAL);
        if (nullableUser.isPresent()) {
            throw new AlreadyExistsException("이미 가입된 정보가 존재합니다.");
        }
        User user = User.from(param);
        userRepository.save(user);
        JwtToken jwtToken = GeneralJwtUtils.generateJwtAccessAndRefreshTokenBy(user.getUserNo());
        user.setJwtTokens(jwtToken);
        user.setOS(param.getOsType(), param.getOsVersion());
        return UserView.of(user, jwtToken);
    }

}
