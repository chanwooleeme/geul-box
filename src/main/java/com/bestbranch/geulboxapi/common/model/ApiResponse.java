package com.bestbranch.geulboxapi.common.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class ApiResponse<T> {
    private ApiHeader header;
    private T data;

    @Getter
    public class ApiHeader<M> {
        private int code;
        private M message;

        public ApiHeader(int code) {
            this.code = code;
        }

        public ApiHeader(int code, M message) {
            this.code = code;
            this.message = message;
        }
    }

    private ApiResponse(int code) {
        this.header = new ApiHeader(code);
    }

    private ApiResponse(int code, String message) {
        this.header = new ApiHeader(code, message);
    }

    private ApiResponse(int code, Map message) {
        this.header = new ApiHeader(code, message);
    }

    private ApiResponse(int code, T data) {
        this.header = new ApiHeader(code);
        this.data = data;
    }


    public static <T> ApiResponse<T> success(HttpStatus code, String message) {
        return new ApiResponse<>(code.getStatusCode(), message);
    }

    public static <T> ApiResponse<T> success(HttpStatus code, T data) {
        return new ApiResponse<>(code.getStatusCode(), data);
    }

    public static <T> ApiResponse<T> success(HttpStatus code) {
        return new ApiResponse<>(code.getStatusCode());
    }

    public static <T> ApiResponse<T> success(String message) {
        return success(HttpStatus.OK, message);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(HttpStatus.OK, data);
    }

    public static <T> ApiResponse<T> success() {
        return success(HttpStatus.OK);
    }

    public static <T> ApiResponse<T> failure(HttpStatus code, String message) {
        return new ApiResponse<>(code.getStatusCode(), message);
    }

    public static <T> ApiResponse<T> failure(int code, String message) {
        return new ApiResponse<>(code, message);
    }

    public static <T> ApiResponse<T> failure(HttpStatus code, Map<String, String> message) {
        return new ApiResponse<>(code.getStatusCode(), message);
    }

    public static <T> ApiResponse<T> failure(HttpStatus code) {
        return new ApiResponse<>(code.getStatusCode());
    }
}
