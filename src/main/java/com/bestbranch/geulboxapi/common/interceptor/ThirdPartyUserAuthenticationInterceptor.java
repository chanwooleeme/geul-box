package com.bestbranch.geulboxapi.common.interceptor;

import com.bestbranch.geulboxapi.common.exception.model.UserAuthenticationTypeNullException;
import com.bestbranch.geulboxapi.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class ThirdPartyUserAuthenticationInterceptor extends UserAuthenticationInterceptor implements HandlerInterceptor {

    private Map<User.AuthenticationType, HandlerInterceptor> authenticationInterceptors;

    public ThirdPartyUserAuthenticationInterceptor(KakaoUserTokenAuthenticationInterceptor kakaoUserTokenAuthenticationInterceptor,
                                                   AppleUserTokenAuthenticationInterceptor appleUserTokenAuthenticationInterceptor) {
        Map<User.AuthenticationType, HandlerInterceptor> userAuthenticationInterceptorMap = new HashMap<>();
        userAuthenticationInterceptorMap.put(User.AuthenticationType.KAKAO, kakaoUserTokenAuthenticationInterceptor);
        userAuthenticationInterceptorMap.put(User.AuthenticationType.APPLE, appleUserTokenAuthenticationInterceptor);

        this.authenticationInterceptors = userAuthenticationInterceptorMap;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User.AuthenticationType userAuthenticationType =
                User.AuthenticationType.getCodeOf(request.getParameter("userAuthenticationType"));

        if (!userAuthenticationType.isCorrect()) {
            throw new UserAuthenticationTypeNullException();
        }

        return authenticationInterceptors.get(userAuthenticationType).preHandle(request, response, handler);
    }
}
