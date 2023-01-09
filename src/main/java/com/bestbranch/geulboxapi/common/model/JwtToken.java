package com.bestbranch.geulboxapi.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtToken {
	private JwtAccessToken accessToken;
	private JwtRefreshToken refreshToken;
}
