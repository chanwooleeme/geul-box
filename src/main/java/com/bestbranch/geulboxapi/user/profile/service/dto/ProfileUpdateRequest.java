package com.bestbranch.geulboxapi.user.profile.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ProfileUpdateRequest {
	private String nickname;
	private String introduction;

	public void setNickname(String nickname) {
		this.nickname = nickname.trim();
	}
}
