package com.bestbranch.geulboxapi.client.kakao;

import com.bestbranch.geulboxapi.client.kakao.model.KakaoRequest;
import com.bestbranch.geulboxapi.client.kakao.model.KakaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "kakaoAuthClient", url = "https://kauth.kakao.com")
public interface KakaoAuthClient {
    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    KakaoResponse.AccessToken getAccessToken(KakaoRequest request);

}
