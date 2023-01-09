package com.bestbranch.geulboxapi.common.model;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2Token {
	private String value;

	public static String makeTokenWithPrefixedTokenType(String token) {
		return token.contains("Bearer ") ? token : "Bearer " + token;
	}

	public static String makeTokenWithoutPrefixedTokenType(String token) {
		return token.replaceAll("Bearer ", StringUtils.EMPTY);
	}

	public static OAuth2Token from(String value) {
		OAuth2Token token = new OAuth2Token();
		token.value = value;
		return token;
	}

	public String getValueWithType() {
		return value.contains("Bearer ") ? value : "Bearer " + value;
	}

	public String getValueWithoutType() {
		return value.replaceAll("Bearer ", StringUtils.EMPTY);
	}

	private OAuth2Token() {}
}
