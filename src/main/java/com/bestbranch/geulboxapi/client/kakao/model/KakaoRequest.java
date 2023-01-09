package com.bestbranch.geulboxapi.client.kakao.model;

import lombok.Setter;

@Setter
public class KakaoRequest {
	private String grant_type;
	private String client_id;
	private String refresh_token;

	public static KakaoRequest of(String grantType, String clientId, String refreshToken) {
		KakaoRequest request = new KakaoRequest();
		request.grant_type = grantType;
		request.client_id = clientId;
		request.refresh_token = refreshToken;
		return request;
	}
}
