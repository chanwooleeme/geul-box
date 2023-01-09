package com.bestbranch.geulboxapi.client.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.bestbranch.geulboxapi.client.apple.model.ApplePublicKeyResponse;
import com.bestbranch.geulboxapi.client.apple.model.AppleToken;
import com.bestbranch.geulboxapi.common.config.FeignConfig;

@FeignClient(name = "appleClient", url = "https://appleid.apple.com/auth", configuration = FeignConfig.class)
public interface AppleClient {
	@GetMapping(value = "/keys")
	ApplePublicKeyResponse getAppleAuthPublicKey();

	@PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
	AppleToken.Response getToken(AppleToken.Request request);
}
