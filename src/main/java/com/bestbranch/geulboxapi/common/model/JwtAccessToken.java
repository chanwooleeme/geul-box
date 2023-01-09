package com.bestbranch.geulboxapi.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtAccessToken {
	private String token;
	private Long exp;
}
