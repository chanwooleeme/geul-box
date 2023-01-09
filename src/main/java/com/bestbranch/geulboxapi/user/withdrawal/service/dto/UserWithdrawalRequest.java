package com.bestbranch.geulboxapi.user.withdrawal.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class UserWithdrawalRequest {
	@Setter
	private String thirdPartyAccountId;
	private Long userNo;
	private String accessToken;

	public static UserWithdrawalRequest of(Long userNo) {
		return of(userNo, null, null);
	}

	public static UserWithdrawalRequest of(String thirdPartyAccountId) {
		return of(null, thirdPartyAccountId, null);
	}

	public static UserWithdrawalRequest of(Long userNo, String thirdPartyAccountId, String accessToken) {
		UserWithdrawalRequest withdrawal = new UserWithdrawalRequest();
		withdrawal.userNo = userNo;
		withdrawal.thirdPartyAccountId = thirdPartyAccountId;
		withdrawal.accessToken = accessToken;
		return withdrawal;
	}
}
