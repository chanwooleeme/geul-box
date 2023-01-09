package com.bestbranch.geulboxapi.client.kakao;

import com.bestbranch.geulboxapi.client.kakao.model.KakaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoApiClient", url = "https://kapi.kakao.com")
public interface KakaoApiClient {
    @GetMapping(value = "/v2/user/me", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    KakaoResponse.User getKakaoAccountBy(@RequestHeader("Authorization") String accessToken);

}
