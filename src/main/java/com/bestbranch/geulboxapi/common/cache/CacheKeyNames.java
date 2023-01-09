package com.bestbranch.geulboxapi.common.cache;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheKeyNames {
	public static final String RELEASE_VERSION = "RELEASE_VERSION";
	public static final String GEUL_DETAIL = "GEUL_DETAIL";
	public static final String EMAIL_AUTHENTICATION_WAITING = "EMAIL_AUTHENTICATION_WAITING";
	public static final String EMAIL_AUTHENTICATION_CONFIRMATION = "EMAIL_AUTHENTICATION_CONFIRMATION";
	public static final String PASSWORD_INITIALIZATION_AUTHENTICATION_WAITING = "PASSWORD_INITIALIZATION_AUTHENTICATION_WAITING";
	public static final String PASSWORD_INITIALIZATION_AUTHENTICATION_CONFIRMATION = "PASSWORD_INITIALIZATION_AUTHENTICATION_CONFIRMATION";
}
