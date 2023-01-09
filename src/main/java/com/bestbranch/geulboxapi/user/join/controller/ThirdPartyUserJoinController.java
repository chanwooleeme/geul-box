package com.bestbranch.geulboxapi.user.join.controller;

import com.bestbranch.geulboxapi.common.exception.model.AlreadyExistsException;
import com.bestbranch.geulboxapi.common.exception.model.BadRequestException;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.HttpStatus;
import com.bestbranch.geulboxapi.user.domain.User;
import com.bestbranch.geulboxapi.user.domain.dto.UserView;
import com.bestbranch.geulboxapi.user.join.service.UserJoinService;
import com.bestbranch.geulboxapi.user.join.service.dto.UserJoinRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Api(description = "서드파티 회원 가입 API 목록", tags = {"[USER] [THIRD-PARTY] [JOIN]"})
@RestController
@RequiredArgsConstructor
public class ThirdPartyUserJoinController {
    private final UserJoinService appleUserJoinService;
    private final UserJoinService kakaoUserJoinService;
    private final UserJoinService kakaoUserJoinServiceOld;

    //FIXME 리팩토링 필요
    @ApiOperation(value = "서드파티 회원 가입")
    @ApiImplicitParam(value = "APPLE 회원가입시 기입", name = "AppleAuthorizationCode", paramType = "header")
    @PostMapping("/third-party/users")
    public ApiResponse<UserView> joinThirdPartyUser(@ApiParam(hidden = true, required = true) @RequestAttribute String thirdPartyAccountId,
                                                    @ApiParam(hidden = true) @RequestAttribute(required = false) String name,
                                                    @ApiParam(hidden = true) @RequestAttribute(required = false) String email,
                                                    @ApiParam(hidden = true) @RequestAttribute(required = false) String accessToken,
                                                    @ApiParam(hidden = true) @RequestAttribute(required = false) String refreshToken,
                                                    @RequestBody(required = false) UserJoinRequest joinParam,
                                                    @RequestParam User.AuthenticationType userAuthenticationType)
            throws AlreadyExistsException {
        try {
            UserView userResponse = null;
            switch (userAuthenticationType) {
                case KAKAO:
                    UserJoinRequest kakaoParam = UserJoinRequest.thirdPartyUserJoinOf(thirdPartyAccountId, name, email, userAuthenticationType,
                            accessToken, refreshToken, joinParam.getOsType(),
                            joinParam.getOsVersion());
                    userResponse = kakaoUserJoinServiceOld.joinUser(kakaoParam);
                    break;
                case APPLE:
                    UserJoinRequest appleParam = UserJoinRequest.thirdPartyUserJoinOf(thirdPartyAccountId,
                            joinParam.getName(),
                            email, userAuthenticationType, accessToken, refreshToken,
                            joinParam.getOsType(), joinParam.getOsVersion(), joinParam.getNickname());
                    userResponse = appleUserJoinService.joinUser(appleParam);
            }
            return ApiResponse.success(HttpStatus.CREATED, userResponse);
        } catch (BadRequestException e) {
            return ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    //FIXME 리팩토링 필요
    @ApiOperation(value = "서드파티 회원 가입")
    @ApiImplicitParam(value = "APPLE 회원가입시 기입", name = "AppleAuthorizationCode", paramType = "header")
    @PostMapping("/v1.2/third-party/users")
    public ApiResponse<UserView> joinThirdPartyUser(@ApiParam(hidden = true, required = true) @RequestAttribute String thirdPartyAccountId,
                                                    @ApiParam(hidden = true) @RequestAttribute(required = false) String name,
                                                    @ApiParam(hidden = true) @RequestAttribute(required = false) String email,
                                                    @ApiParam(hidden = true) @RequestAttribute(required = false) String accessToken,
                                                    @ApiParam(hidden = true) @RequestAttribute(required = false) String refreshToken,
                                                    @RequestParam User.AuthenticationType userAuthenticationType)
            throws AlreadyExistsException {
        try {
            UserView userResponse = null;
            switch (userAuthenticationType) {
                case KAKAO:
                    UserJoinRequest kakaoParam = UserJoinRequest.thirdPartyUserJoinOf(thirdPartyAccountId, name, email, userAuthenticationType);
                    userResponse = kakaoUserJoinService.joinUser(kakaoParam);
                    break;
                case APPLE:
                    UserJoinRequest appleParam =
                            UserJoinRequest.thirdPartyUserJoinOf(thirdPartyAccountId, name, email, userAuthenticationType, accessToken, refreshToken);
                    userResponse = appleUserJoinService.joinUser(appleParam);
            }
            return ApiResponse.success(HttpStatus.CREATED, userResponse);
        } catch (BadRequestException e) {
            return ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}