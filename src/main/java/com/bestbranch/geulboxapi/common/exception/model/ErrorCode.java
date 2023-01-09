package com.bestbranch.geulboxapi.common.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //Legacy
    USER_AUTHENTICATION_TYPE_OR_TOKEN_IS_NULL("회원 인증 타입 또는 토큰이 NULL 입니다.", 400, HttpStatus.OK.value()),
    TOKEN_IS_EXPIRED("토큰이 만료됐습니다.", 402, HttpStatus.OK.value()),
    USER_NOT_JOINED("회원이 가입된 정보가 존재하지 않습니다.", 408, HttpStatus.OK.value()),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", 411, HttpStatus.OK.value()),
    

    //Geul 10xx
    GEUL_NOT_FOUND("존재하지 않는 글입니다.", 1000, HttpStatus.OK.value()),

    //User 13xx
    USER_NOT_FOUND("존재하지 않는 회원입니다.", 1300, HttpStatus.OK.value()),
    USER_IS_BLOCKED("차단된 회원 입니다.", 1301, HttpStatus.OK.value()),

    //Geul Report 11xx
    NEED_GEUL_REPORT_CUSTOM_REASON("직접 입력 사유가 누락 됐습니다.", 1100, HttpStatus.OK.value()),
    GEUL_ALREADY_REPORTED("이미 신고한 글입니다.", 1101, HttpStatus.OK.value()),

    //User Report 12xx
    NEED_USER_REPORT_CUSTOM_REASON("직접 입력 사유가 누락 됐습니다.", 1200, HttpStatus.OK.value()),
    USER_ALREADY_REPORTED("이미 신고한 회원입니다.", 1201, HttpStatus.OK.value()),
    USER_ALREADY_BLOCKED("이미 차단한 회원입니다.", 1202, HttpStatus.OK.value()),


    DUMMY("", 0, 0);

    private final String message;
    private final int errorCode;
    private final int statusCode;
}
