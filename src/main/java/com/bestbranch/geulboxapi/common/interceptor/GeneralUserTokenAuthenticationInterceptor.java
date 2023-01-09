package com.bestbranch.geulboxapi.common.interceptor;

import com.bestbranch.geulboxapi.common.exception.model.ExpiredTokenException;
import com.bestbranch.geulboxapi.common.exception.model.InvalidTokenException;
import com.bestbranch.geulboxapi.common.exception.model.TokenNullException;
import com.bestbranch.geulboxapi.common.utils.jwt.JwtUtils;
import com.bestbranch.geulboxapi.user.login.service.GeneralUserAuthTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class GeneralUserTokenAuthenticationInterceptor extends UserAuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private GeneralUserAuthTokenService generalUserAuthTokenService;
    @Autowired
    @Qualifier("generalJwtUtils")
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader("Authorization");

        if (StringUtils.isBlank(accessToken)) {
            throw new TokenNullException();
        }

        try {
            Long userNo = Long.valueOf(jwtUtils.getClaimsBy(accessToken).get("userNo").toString());
            generalUserAuthTokenService.verifyAccessTokenOfUserBy(userNo, accessToken);
            request.setAttribute("userNo", userNo);
            return true;
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (MalformedJwtException | SignatureException e) {
            throw new InvalidTokenException();
        }
    }
}
