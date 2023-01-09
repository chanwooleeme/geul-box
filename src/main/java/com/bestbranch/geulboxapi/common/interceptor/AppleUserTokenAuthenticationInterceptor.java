package com.bestbranch.geulboxapi.common.interceptor;

import com.bestbranch.geulboxapi.client.apple.AppleClient;
import com.bestbranch.geulboxapi.client.apple.model.AppleToken;
import com.bestbranch.geulboxapi.client.apple.model.GrantType;
import com.bestbranch.geulboxapi.client.common.ThirdPartyResponse;
import com.bestbranch.geulboxapi.common.exception.model.ExpiredTokenException;
import com.bestbranch.geulboxapi.common.exception.model.InvalidTokenException;
import com.bestbranch.geulboxapi.common.exception.model.TokenNullException;
import com.bestbranch.geulboxapi.common.exception.model.UserNotJoinedException;
import com.bestbranch.geulboxapi.common.model.OAuth2Token;
import com.bestbranch.geulboxapi.common.type.RequestServiceUri;
import com.bestbranch.geulboxapi.common.utils.jwt.AppleJwtUtils;
import com.bestbranch.geulboxapi.common.utils.jwt.JwtUtils;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AppleUserTokenAuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserSpringDataJpaRepository userRepository;
    @Autowired
    private AppleClient appleClient;
    @Autowired
    @Qualifier("appleJwtUtils")
    private JwtUtils jwtUtils;
    @Value("${apple.client.id}")
    private String appleClientId;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        OAuth2Token token = OAuth2Token.from(request.getHeader("Authorization"));
        String appleAuthorizationCode = request.getHeader("AppleAuthorizationCode");
        if (StringUtils.isBlank(token.getValue())) {
            throw new TokenNullException();
        }

        try {
            ThirdPartyResponse thirdPartyResponse = ThirdPartyResponse.of(getAppleAccountId(token, appleAuthorizationCode, request), null);
            request.setAttribute("thirdPartyAccountId", thirdPartyResponse.getThirdPartyAccountId());

            RequestServiceUri requestServiceUri = RequestServiceUri.from(request.getRequestURI());

            if (!requestServiceUri.equals(RequestServiceUri.THIRD_PARTY_USER_JOIN)) {
                User user = userRepository.findByThirdPartyAccountIdAndUserAuthenticationType(thirdPartyResponse.getThirdPartyAccountId(), User.AuthenticationType.APPLE)
                        .orElseThrow(UserNotJoinedException::new);
                request.setAttribute("userNo", user.getUserNo());
            }

            return true;
        } catch (FeignException e) {
            e.printStackTrace();
            if (e.status() == 400 || e.status() == 401) {
                throw new InvalidTokenException();
            } else {
                throw e;
            }
        } catch (SignatureException | MalformedJwtException e) {
            throw new InvalidTokenException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        }
    }

    private String getAppleAccountId(OAuth2Token token, String appleAuthorizationCode, HttpServletRequest request)
            throws InvalidKeySpecException, IOException, NoSuchAlgorithmException, ExpiredTokenException {
        String clientSecret = ((AppleJwtUtils) jwtUtils).makeClientSecret();
        switch (RequestServiceUri.from(request.getRequestURI())) {
            case THIRD_PARTY_USER_JOIN: //token은 id_token 성격을 띔.
                String appleAccountId = jwtUtils.getClaimsBy(token.getValueWithoutType()).getSubject();

                AppleToken.Request tokenRequest = AppleToken.Request.of(appleAuthorizationCode, appleClientId, clientSecret,
                        GrantType.AUTHORIZATION_CODE.getName(), null);
                String refreshToken = generateAppleRefreshToken(tokenRequest);
                request.setAttribute("refreshToken", refreshToken);
                return appleAccountId;
            default: //token은 refresh token 성격을 띔, apple 정책상 refresh token 만료시간 없음
                return getAccountIdAndVerifyRefreshToken(token);
        }
    }

    private String generateAppleRefreshToken(AppleToken.Request request) {
        AppleToken.Response appleTokenResponse = appleClient.getToken(request);
        return appleTokenResponse.getRefreshToken();
    }

    private String getAccountIdAndVerifyRefreshToken(OAuth2Token token) throws ExpiredTokenException {
        //access token을 재발급 받아보는것만으로 refresh token 유효성 검증 -> 시간 너무 오래걸림.
        //appleClient.getToken(request);

        //DB에서 조회해서 검증하는방식으로 변경.
        return userRepository.findByRefreshToken(token.getValueWithoutType()).orElseThrow(ExpiredTokenException::new).getThirdPartyAccountId();
    }
}
