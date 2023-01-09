package com.bestbranch.geulboxapi.client.kakao.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoResponse {

	@Getter
	public static class AccessToken {
		private String accessToken;
		private String tokenType;
		private Integer expiresIn;

		public void setAccess_token(String access_token) {
			this.accessToken = access_token;
		}

		public void setToken_type(String token_type) {
			this.tokenType = token_type;
		}

		public void setExpires_in(Integer expires_in) {
			this.expiresIn = expires_in;
		}
	}

	@Getter
	@Setter
	public static class User {
		private long id;
		private Properties properties;
		private Account kakaoAccount;
		private String msg;

		public void setKakao_account(Account kakao_account) {
			this.kakaoAccount = kakao_account;
		}

		public String getKakaoEmail() {
			if (this.kakaoAccount != null) {
				return this.kakaoAccount.email;
			}
			return null;
		}

		public String getKakaoNickname() {
			if (this.kakaoAccount != null && this.kakaoAccount.profile != null) {
				return this.kakaoAccount.profile.nickname;
			}
			return null;
		}

		@Getter
		@Setter
		public class Properties {
			private String nickname;
		}

		@Getter
		@Setter
		public class Account {
			private String email;
			private Profile profile;

			@Getter
			@Setter
			public class Profile {
				private String nickname;
			}
		}
	}
}
