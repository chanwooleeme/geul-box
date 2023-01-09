package com.bestbranch.geulboxapi.common.exception.model;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private String message;
    private int errorCode;
    private int statusCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.message = errorCode.getMessage();
        this.errorCode = errorCode.getErrorCode();
        this.statusCode = errorCode.getStatusCode();
    }
}
