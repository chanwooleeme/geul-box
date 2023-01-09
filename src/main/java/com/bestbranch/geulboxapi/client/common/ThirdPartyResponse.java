package com.bestbranch.geulboxapi.client.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThirdPartyResponse {
	private String thirdPartyAccountId;
	private String name;
	private String refreshToken;

	public static ThirdPartyResponse of(String thirdPartyAccountId, String name) {
		return ThirdPartyResponse.of(thirdPartyAccountId, name, null);
	}

	public static ThirdPartyResponse of(String thirdPartyAccountId, String name, String refreshToken) {
		ThirdPartyResponse response = new ThirdPartyResponse();
		response.thirdPartyAccountId = thirdPartyAccountId;
		response.name = name;
		response.refreshToken = refreshToken;
		return response;
	}
}
