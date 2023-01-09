package com.bestbranch.geulboxapi.common.utils.jwt;

import com.bestbranch.geulboxapi.common.model.JwtAccessToken;
import com.bestbranch.geulboxapi.common.model.JwtRefreshToken;
import com.bestbranch.geulboxapi.common.model.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class GeneralJwtUtils implements JwtUtils {
    private static final long JWT_ACCESS_TOKEN_EXPIRE_MILLISECOND = 1000L * 60L * 60L * 24L;
    private static final long JWT_REFRESH_TOKEN_EXPIRE_MILLISECOND = 1000L * 60L * 60L * 24L * 30L;

    private static String jwtSecretKey;

    public static JwtToken generateJwtAccessAndRefreshTokenBy(Long userNo) {
        return new JwtToken(generateJwtAccessTokenBy(userNo), generateJwtRefreshTokenBy(userNo));
    }

    public static JwtAccessToken generateJwtAccessTokenBy(Long userNo) {
        long expireMillisecond = System.currentTimeMillis() + JWT_ACCESS_TOKEN_EXPIRE_MILLISECOND;
        String accessToken = Jwts.builder()
                .setSubject("geul-box-access-token")
                .setHeaderParam("typ", "JWT")
                .claim("userNo", userNo)
                .claim("id", UUID.randomUUID().toString())
                .setExpiration(new Date(expireMillisecond))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
        return new JwtAccessToken(accessToken, expireMillisecond);
    }

    private static JwtRefreshToken generateJwtRefreshTokenBy(Long userNo) {
        long expireMillisecond = System.currentTimeMillis() + JWT_REFRESH_TOKEN_EXPIRE_MILLISECOND;
        String refreshToken = Jwts.builder()
                .setSubject("geul-box-refresh-token")
                .setHeaderParam("typ", "JWT")
                .claim("userNo", userNo)
                .claim("id", UUID.randomUUID().toString())
                .setExpiration(new Date(expireMillisecond))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
        return new JwtRefreshToken(refreshToken, expireMillisecond);
    }

    @Value("${spring.jwt.general.secret-key}")
    public void setJwtSecretKey(String jwtSecretKey) {
        GeneralJwtUtils.jwtSecretKey = jwtSecretKey;
    }

    @Override
    public Claims getClaimsBy(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token.replaceFirst("Bearer ", StringUtils.EMPTY)).getBody();
    }
}
