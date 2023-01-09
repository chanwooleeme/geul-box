package com.bestbranch.geulboxapi.user.login.controller;

import com.bestbranch.geulboxapi.common.exception.model.BadRequestException;
import com.bestbranch.geulboxapi.common.exception.model.UnauthorizedException;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.HttpStatus;
import com.bestbranch.geulboxapi.common.model.JwtAccessToken;
import com.bestbranch.geulboxapi.common.model.JwtToken;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.login.service.GeneralUserAuthTokenService;
import com.bestbranch.geulboxapi.user.login.service.dto.RegenerationAllToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Api(description = "일반 회원 Token API 목록", tags = {"[USER] [GENERAL] [TOKEN]"})
@RestController
@RequiredArgsConstructor
public class GeneralUserTokenController {
    private final GeneralUserAuthTokenService generalUserAuthTokenService;

    @ApiOperation(value = "리프레시 토큰을 통해 엑세스 토큰 재발급")
    @PostMapping("/user/token")
    public ApiResponse<JwtAccessToken> getUserAccessTokenUsingRefreshToken(@ApiParam(defaultValue = "Bearer ", value = "Bearer {refreshToken}", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken)
            throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        try {
            JwtAccessToken accessToken = generalUserAuthTokenService.getAccessTokenOfUserBy(refreshToken);
            return ApiResponse.success(accessToken);
        } catch (SignatureException | MalformedJwtException e) {
            return ApiResponse.failure(HttpStatus.UNAUTHORIZED, "유효하지 않은 refresh token 입니다.");
        } catch (ExpiredJwtException e) {
            return ApiResponse.failure(HttpStatus.EXPIRED_TOKEN, "Refresh token이 만료됐습니다.");
        }
    }

    @ApiOperation(value = "리프레시 토큰을 통해 엑세스 토큰 재발급")
    @PostMapping("/v1.2/user/token")
    public ApiResponse<JwtAccessToken> getUserAccessTokenUsingRefreshTokenV1_2(@ApiParam(defaultValue = "Bearer ", value = "Bearer {refreshToken}", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken)
            throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        try {
            JwtAccessToken accessToken = generalUserAuthTokenService.getAccessTokenOfUserBy(refreshToken);
            return ApiResponse.success(accessToken);
        } catch (SignatureException | MalformedJwtException e) {
            return ApiResponse.failure(HttpStatus.INVALID_TOKEN, "유효하지 않은 refresh token 입니다.");
        } catch (ExpiredJwtException e) {
            return ApiResponse.failure(HttpStatus.EXPIRED_TOKEN, "Refresh token이 만료됐습니다.");
        }
    }

    @ApiOperation(value = "엑세스, 리프레시 토큰 재발급")
    @PostMapping("/user/all-token")
    public ApiResponse<JwtToken> getUserAllToken(@RequestBody RegenerationAllToken regenerationAllToken,
                                                 @RequestParam("userAuthenticationType") User.AuthenticationType userAuthenticationType) {
        try {
            regenerationAllToken.setUserAuthenticationType(userAuthenticationType);
            regenerationAllToken.validate();
            return ApiResponse.success(generalUserAuthTokenService.getAllTokenBy(regenerationAllToken));
        } catch (BadRequestException e) {
            return ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UnauthorizedException e) {
            return ApiResponse.failure(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiOperation(value = "엑세스, 리프레시 토큰 재발급")
    @PostMapping("/v1.2/user/all-token")
    public ApiResponse<JwtToken> getUserAllTokenV1_2(@RequestBody RegenerationAllToken regenerationAllToken) {
        try {
            regenerationAllToken.setUserAuthenticationType(User.AuthenticationType.GENERAL);
            regenerationAllToken.validate();
            return ApiResponse.success(generalUserAuthTokenService.getAllTokenBy(regenerationAllToken));
        } catch (BadRequestException e) {
            return ApiResponse.failure(HttpStatus.BAD_REQUEST, "이메일 또는 패스워드가 요청에 올바르지 않습니다.");
        } catch (UnauthorizedException e) {
            return ApiResponse.failure(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
