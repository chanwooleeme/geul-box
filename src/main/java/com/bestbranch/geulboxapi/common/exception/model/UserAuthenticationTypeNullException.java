package com.bestbranch.geulboxapi.common.exception.model;

public class UserAuthenticationTypeNullException extends Exception {
	public UserAuthenticationTypeNullException(String message) {
		super(message);
	}

	public UserAuthenticationTypeNullException() {
		this("회원 인증 타입이 NULL 입니다.");
	}
}
