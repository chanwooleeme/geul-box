package com.bestbranch.geulboxapi.common.interceptor;

import com.bestbranch.geulboxapi.client.kakao.KakaoApiClient;
import com.bestbranch.geulboxapi.client.kakao.model.KakaoResponse;
import com.bestbranch.geulboxapi.common.exception.model.ExpiredTokenException;
import com.bestbranch.geulboxapi.common.exception.model.TokenNullException;
import com.bestbranch.geulboxapi.common.exception.model.UserNotJoinedException;
import com.bestbranch.geulboxapi.common.model.OAuth2Token;
import com.bestbranch.geulboxapi.common.type.RequestServiceUri;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.repository.UserSpringDataJpaRepository;
import feign.FeignException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class KakaoUserTokenAuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private KakaoApiClient kakaoApiClient;
    @Autowired
    private UserSpringDataJpaRepository userRepository;

    //FIXME START 추후 삭제
    @Autowired
    private KakaoUserTokenAuthenticationInterceptorOld kakaoUserTokenAuthenticationInterceptorOld;
    private static final List<String> oldVersionEndpoint = Arrays.asList("/third-party/users",
            "/third-party/user/auth",
            "/v1.1/third-party/user/auth");
    //FIXME END

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //FIXME START 추후 삭제
        if (oldVersionEndpoint.contains(request.getRequestURI())) {
            return kakaoUserTokenAuthenticationInterceptorOld.preHandle(request, response, handler);
        }
        //FIXME END

        OAuth2Token token = OAuth2Token.from(request.getHeader("Authorization"));

        if (StringUtils.isBlank(token.getValue())) {
            throw new TokenNullException();
        }

        KakaoResponse.User kakaoUser = null;
        try {
            kakaoUser = kakaoApiClient.getKakaoAccountBy(token.getValueWithType());
        } catch (FeignException.Unauthorized e) {
            throw new ExpiredTokenException("엑세스 토큰이 만료됐습니다.");
        }

        request.setAttribute("accessToken", token.getValueWithoutType());
        request.setAttribute("email", kakaoUser.getKakaoEmail());
        request.setAttribute("thirdPartyAccountId", kakaoUser.getId());
        request.setAttribute("name", kakaoUser.getKakaoNickname());

        RequestServiceUri requestServiceUri = RequestServiceUri.from(request.getRequestURI());

        if (!requestServiceUri.equals(RequestServiceUri.THIRD_PARTY_USER_JOIN)) {
            User user = userRepository.findByThirdPartyAccountIdAndUserAuthenticationType(String.valueOf(kakaoUser.getId()), User.AuthenticationType.KAKAO)
                    .orElseThrow(UserNotJoinedException::new);
            request.setAttribute("userNo", user.getUserNo());
        }
        return true;
    }
}
