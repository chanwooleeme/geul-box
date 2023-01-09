package com.bestbranch.geulboxapi.common.interceptor;

import com.bestbranch.geulboxapi.client.apple.model.GrantType;
import com.bestbranch.geulboxapi.client.common.ThirdPartyResponse;
import com.bestbranch.geulboxapi.client.kakao.KakaoApiClient;
import com.bestbranch.geulboxapi.client.kakao.KakaoAuthClient;
import com.bestbranch.geulboxapi.client.kakao.model.KakaoRequest;
import com.bestbranch.geulboxapi.client.kakao.model.KakaoResponse;
import com.bestbranch.geulboxapi.common.exception.model.ExpiredTokenException;
import com.bestbranch.geulboxapi.common.exception.model.InvalidTokenException;
import com.bestbranch.geulboxapi.common.exception.model.TokenNullException;
import com.bestbranch.geulboxapi.common.exception.model.UserNotJoinedException;
import com.bestbranch.geulboxapi.common.model.OAuth2Token;
import com.bestbranch.geulboxapi.common.type.RequestServiceUri;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.login.service.KakaoUserAuthTokenService;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import feign.FeignException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class KakaoUserTokenAuthenticationInterceptorOld implements HandlerInterceptor {
    @Autowired
    private KakaoApiClient kakaoApiClient;
    @Autowired
    private KakaoAuthClient kakaoAuthClient;
    @Autowired
    private KakaoUserAuthTokenService kakaoUserAuthTokenService;
    @Autowired
    private UserSpringDataJpaRepository userRepository;
    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws TokenNullException, ExpiredTokenException, InvalidTokenException, UserNotJoinedException {
        OAuth2Token token = OAuth2Token.from(request.getHeader("Authorization"));

        if (StringUtils.isBlank(token.getValue())) {
            throw new TokenNullException();
        }

        setTokensWhenThroughKakaoJoin(token, request);

        KakaoResponse.User kakaoUser;
        try {
            kakaoUser = kakaoApiClient.getKakaoAccountBy(token.getValueWithType());
        } catch (FeignException.Unauthorized e) { //거부됐다면 access token을 한번더 갱신해서 조회해본다.
            kakaoUser = getKakaoUserAgainAfterRefreshAccessToken(token, request);
        }
        request.setAttribute("email", kakaoUser.getKakaoAccount().getEmail());

        try {
            ThirdPartyResponse thirdPartyResponse = ThirdPartyResponse.of(String.valueOf(kakaoUser.getId()), kakaoUser.getProperties().getNickname());

            request.setAttribute("thirdPartyAccountId", thirdPartyResponse.getThirdPartyAccountId());
            request.setAttribute("name", thirdPartyResponse.getName());

            RequestServiceUri requestServiceUri = RequestServiceUri.from(request.getRequestURI());

            if (!requestServiceUri.equals(RequestServiceUri.THIRD_PARTY_USER_JOIN)) {
                User user = userRepository.findByThirdPartyAccountIdAndUserAuthenticationType(thirdPartyResponse.getThirdPartyAccountId(), User.AuthenticationType.KAKAO)
                        .orElseThrow(UserNotJoinedException::new);
                request.setAttribute("userNo", user.getUserNo());
            }
            return true;
        } catch (FeignException e) {
            e.printStackTrace();
            if (e.status() == 401) {
                throw new InvalidTokenException(e.getMessage());
            } else {
                throw e;
            }
        }
    }

    private void setTokensWhenThroughKakaoJoin(OAuth2Token token, HttpServletRequest request) {
        if (RequestServiceUri.THIRD_PARTY_USER_JOIN.getUris().get().contains(request.getRequestURI())) {
            request.setAttribute("refreshToken", token.getValueWithoutType());
            KakaoResponse.AccessToken kakaoAccessToken = kakaoAuthClient.getAccessToken(KakaoRequest.of(GrantType.REFRESH_TOKEN.getName(),
                    kakaoClientId, token.getValueWithoutType()));
            token.setValue(kakaoAccessToken.getAccessToken());
            request.setAttribute("accessToken", token.getValueWithoutType());
        }
    }

    private KakaoResponse.User getKakaoUserAgainAfterRefreshAccessToken(OAuth2Token token, HttpServletRequest request)
            throws InvalidTokenException, ExpiredTokenException {
        User user = userRepository.findByAccessToken(token.getValueWithoutType()).orElseThrow(() -> new InvalidTokenException("올바르지 않은 토큰입니다."));
        try {
            KakaoResponse.AccessToken kakaoAccessToken = kakaoAuthClient.getAccessToken(KakaoRequest.of(GrantType.REFRESH_TOKEN.getName(),
                    kakaoClientId, user.getRefreshToken()));
            kakaoUserAuthTokenService.modifyAccessToken(token.getValueWithoutType(), kakaoAccessToken.getAccessToken());
            request.setAttribute("accessToken", kakaoAccessToken.getAccessToken());
            return kakaoApiClient.getKakaoAccountBy(OAuth2Token.makeTokenWithPrefixedTokenType(kakaoAccessToken.getAccessToken()));
        } catch (FeignException e) {
            throw new ExpiredTokenException("refresh 토큰이 만료됐습니다.");
        }
    }
}
