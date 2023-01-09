package com.bestbranch.geulboxapi.common.exception.model;

public class TokenNullException extends Exception {
	public TokenNullException(String message) {
		super(message);
	}

	public TokenNullException() {
		this("토큰이 NULL 입니다.");
	}
}
