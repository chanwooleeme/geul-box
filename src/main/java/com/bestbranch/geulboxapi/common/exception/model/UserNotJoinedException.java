package com.bestbranch.geulboxapi.common.exception.model;

public class UserNotJoinedException extends Exception {

	public UserNotJoinedException(String message) {
		super(message);
	}

	public UserNotJoinedException() {
		this("가입된 정보가 존재하지 않습니다.");
	}
}
