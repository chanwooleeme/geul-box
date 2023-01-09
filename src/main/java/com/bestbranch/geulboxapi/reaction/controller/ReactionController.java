package com.bestbranch.geulboxapi.reaction.controller;

import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.reaction.service.ReactionService;
import com.bestbranch.geulboxapi.user.domain.TokenAuthentication;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(description = "글 공감 API 목록", tags = {"[GEUL] [REACTION]"})
@RestController
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;

    @ApiOperation(value = "공감하기")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
    })
    @PostMapping("/geuls/{geulNo}/reaction")
    @TokenAuthentication
    public ApiResponse react(@ApiParam(hidden = true) @RequestAttribute Long userNo, @PathVariable Long geulNo) {
        reactionService.react(userNo, geulNo);
        return ApiResponse.success();
    }

    @ApiOperation(value = "공감 최소하기")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "KAKAO/GENERAL: Bearer {accessToken}\nAPPLE: Bearer {refreshToken}", name = HttpHeaders.AUTHORIZATION, required = true, paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(value = "Available values : KAKAO, APPLE, GENERAL", name = "userAuthenticationType", required = true, paramType = "query", defaultValue = "APPLE"),
    })
    @DeleteMapping("/geuls/{geulNo}/reaction")
    @TokenAuthentication
    public ApiResponse cancelReaction(@ApiParam(hidden = true) @RequestAttribute Long userNo, @PathVariable Long geulNo) {
        reactionService.cancelReaction(userNo, geulNo);
        return ApiResponse.success();
    }
}
