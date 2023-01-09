package com.bestbranch.geulboxapi.client.apple.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GrantType {
	AUTHORIZATION_CODE("authorization_code"), REFRESH_TOKEN("refresh_token");

	private String name;
}
