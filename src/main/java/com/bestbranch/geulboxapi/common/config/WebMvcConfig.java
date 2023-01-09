package com.bestbranch.geulboxapi.common.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bestbranch.geulboxapi.common.interceptor.AppleUserTokenAuthenticationInterceptor;
import com.bestbranch.geulboxapi.common.interceptor.GeneralUserTokenAuthenticationInterceptor;
import com.bestbranch.geulboxapi.common.interceptor.KakaoUserTokenAuthenticationInterceptor;
import com.bestbranch.geulboxapi.common.interceptor.ThirdPartyUserAuthenticationInterceptor;
import com.bestbranch.geulboxapi.common.interceptor.TokenAuthenticationInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final static List<String> TOKEN_GENERAL_USER_AUTHENTICATION_INTERCEPTOR_INCLUDE_URIS =
			Arrays.asList("/v1.2/user/auth",
						  "/v1.1/user/auth",
						  "/user/auth",
						  "/users/withdrawal",
						  "/v1.2/users/withdrawal");

	private final static List<String> TOKEN_THIRD_PARTY_USER_AUTHENTICATION_INTERCEPTOR_INCLUDE_URIS =
			Arrays.asList("/third-party/users",
						  "/v1.2/third-party/users",
						  "/third-party/user/auth",
						  "/third-party/users/withdrawal",
						  "/v1.1/third-party/user/auth");

	//@TokenAuthentication 으로 대체
	private final static List<String> TOKEN_INTEGRATION_AUTHENTICATION_INTERCEPTOR_INCLUDE_URIS =
			Arrays.asList("/geuls",
						  "/geuls/*",
						  "/users/*/days-geul-count-of-month",
						  "/v1.2/users/*/days-geul-count-of-month",
						  "/users/{userNo}/geuls",
						  "/v1.2/geuls/*",
						  "/v1.2/users/*/geuls",
						  "/v1.1/users/nicknames",
						  "/v1.2/users/profile");

	private final static List<String> SWAGGER_INTERCEPTOR_EXCLUDE_URIS =
			Arrays.asList("/", "/csrf", "/error", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/swagger/**");


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(thirdPartyUserAuthenticationInterceptor())
				.addPathPatterns(TOKEN_THIRD_PARTY_USER_AUTHENTICATION_INTERCEPTOR_INCLUDE_URIS);
		registry.addInterceptor(generalUserAuthenticationInterceptor())
				.addPathPatterns(TOKEN_GENERAL_USER_AUTHENTICATION_INTERCEPTOR_INCLUDE_URIS);
		registry.addInterceptor(tokenAuthenticationInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns(SWAGGER_INTERCEPTOR_EXCLUDE_URIS);
	}

	@Bean
	public TokenAuthenticationInterceptor tokenAuthenticationInterceptor() {
		return new TokenAuthenticationInterceptor(generalUserAuthenticationInterceptor(), thirdPartyUserAuthenticationInterceptor());
	}

	@Bean
	public GeneralUserTokenAuthenticationInterceptor generalUserAuthenticationInterceptor() {
		return new GeneralUserTokenAuthenticationInterceptor();
	}

	@Bean
	public ThirdPartyUserAuthenticationInterceptor thirdPartyUserAuthenticationInterceptor() {
		return new ThirdPartyUserAuthenticationInterceptor(kakaoUserTokenAuthenticationInterceptor(), appleUserTokenAuthenticationInterceptor());
	}

	@Bean
	public KakaoUserTokenAuthenticationInterceptor kakaoUserTokenAuthenticationInterceptor() {
		return new KakaoUserTokenAuthenticationInterceptor();
	}

	@Bean
	public AppleUserTokenAuthenticationInterceptor appleUserTokenAuthenticationInterceptor() {
		return new AppleUserTokenAuthenticationInterceptor();
	}
}