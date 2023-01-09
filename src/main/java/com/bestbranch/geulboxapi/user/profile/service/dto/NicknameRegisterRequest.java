package com.bestbranch.geulboxapi.user.profile.service.dto;

import lombok.Getter;

@Getter
public class NicknameRegisterRequest {
    private String nickname;

    public void setNickname(String nickname) {
        this.nickname = nickname.trim();
    }
}
