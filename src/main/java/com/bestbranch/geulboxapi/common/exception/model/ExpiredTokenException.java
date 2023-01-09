package com.bestbranch.geulboxapi.common.exception.model;

public class ExpiredTokenException extends RuntimeException {
	public ExpiredTokenException(String message) {
		super(message);
	}

	public ExpiredTokenException() {
		this("토큰이 만료됐습니다.");
	}
}
