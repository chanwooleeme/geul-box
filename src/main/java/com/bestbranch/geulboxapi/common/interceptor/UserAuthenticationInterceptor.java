package com.bestbranch.geulboxapi.common.interceptor;

import com.bestbranch.geulboxapi.user.domain.TokenAuthentication;
import org.springframework.web.method.HandlerMethod;

public abstract class UserAuthenticationInterceptor {

    protected boolean isHandlerMethodExcludeThisInterceptor(HandlerMethod handlerMethod) {
        TokenAuthentication tokenAuthentication = handlerMethod.getMethodAnnotation(TokenAuthentication.class);
        return tokenAuthentication == null;

    }
}
