package com.bestbranch.geulboxapi.user.login.service;

import com.bestbranch.geulboxapi.common.exception.model.UnauthorizedException;
import com.bestbranch.geulboxapi.common.exception.model.UserNotJoinedException;
import com.bestbranch.geulboxapi.common.model.JwtAccessToken;
import com.bestbranch.geulboxapi.common.model.JwtToken;
import com.bestbranch.geulboxapi.common.utils.jwt.GeneralJwtUtils;
import com.bestbranch.geulboxapi.common.utils.jwt.JwtUtils;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.login.service.dto.RegenerationAllToken;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import com.bestbranch.geulboxapi.user.withdrawal.exception.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
public class GeneralUserAuthTokenService {
    private final UserSpringDataJpaRepository userRepository;
    private final JwtUtils jwtUtils;

    public GeneralUserAuthTokenService(UserSpringDataJpaRepository userRepository,
                                       @Qualifier("generalJwtUtils") JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public JwtAccessToken getAccessTokenOfUserBy(String refreshToken) throws JwtException, InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        Long userNo = Long.valueOf(jwtUtils.getClaimsBy(refreshToken).get("userNo").toString());

        verifyRefreshTokenOfUserBy(userNo, refreshToken);
        JwtAccessToken jwtAccessToken = GeneralJwtUtils.generateJwtAccessTokenBy(userNo);
        User user = userRepository.findById(userNo).get();
        user.setJwtAccessToken(jwtAccessToken);
        return jwtAccessToken;
    }

    private void verifyRefreshTokenOfUserBy(Long userNo, String refreshToken) throws JwtException {
        User user = userRepository.findById(userNo).orElseThrow(UserNotFoundException::new);
        if (user != null && !user.equalsRefreshToken(refreshToken)) {
            throw new SignatureException("유효하지 않은 refresh 토큰입니다.");
        }
    }

    public void verifyAccessTokenOfUserBy(Long userNo, String accessToken) throws JwtException, UserNotJoinedException {
        User user = userRepository.findById(userNo).orElseThrow(UserNotJoinedException::new);
        if (!user.equalsAccessToken(accessToken)) {
            throw new SignatureException("유효하지 않은 토큰입니다.");
        }
    }

    @Transactional
    public JwtToken getAllTokenBy(RegenerationAllToken regenerationAllTokenParam) throws UnauthorizedException {
        User user = userRepository.findByEmailAndUserAuthenticationTypeAndPassword(regenerationAllTokenParam.getEmail(), regenerationAllTokenParam.getUserAuthenticationType(), regenerationAllTokenParam.getPassword())
                .orElseThrow(() -> new UnauthorizedException("이메일 또는 패스워드가 올바르지 않습니다."));
        JwtToken jwtToken = GeneralJwtUtils.generateJwtAccessAndRefreshTokenBy(user.getUserNo());
        user.setJwtTokens(jwtToken);
        userRepository.save(user);
        return jwtToken;
    }
}
