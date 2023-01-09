package com.bestbranch.geulboxapi.common.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestServiceUri {
	THIRD_PARTY_USER_JOIN(() -> Arrays.asList("/third-party/users", "/v1.2/third-party/users")),
	THIRD_PARTY_USER_LOGIN(() -> Arrays.asList("/third-party/user/auth", "/v1.1/third-party/user/auth")),
	NONE(Collections::emptyList);


	private Supplier<List> uris;

	public static RequestServiceUri from(String uri) {
		for (RequestServiceUri type : RequestServiceUri.values()) {
			if (type.uris.get().contains(uri)) {
				return type;
			}
		}
		return NONE;
	}
}
