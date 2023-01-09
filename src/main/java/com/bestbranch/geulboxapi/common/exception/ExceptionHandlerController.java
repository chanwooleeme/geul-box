package com.bestbranch.geulboxapi.common.exception;

import com.bestbranch.geulboxapi.common.component.mail.ServerErrorLogMailSender;
import com.bestbranch.geulboxapi.common.exception.model.*;
import com.bestbranch.geulboxapi.common.model.ApiResponse;
import com.bestbranch.geulboxapi.common.model.HttpStatus;
import com.bestbranch.geulboxapi.common.type.Phase;
import com.bestbranch.geulboxapi.common.utils.RequestPrinter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerController {
    private final ServerErrorLogMailSender serverErrorLogMailSender;

    @Value("${spring.profiles}")
    private String phaseForString;
    private Phase phase;

    @PostConstruct
    public void init() {
        phase = Phase.convertFrom(phaseForString);
    }

    @ExceptionHandler(value = TokenNullException.class)
    public ApiResponse handleTokenNullException(TokenNullException e) {
        return ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = RequiredLoginException.class)
    public ApiResponse handleRequiredLoginException(RequiredLoginException e) {
        return ApiResponse.failure(HttpStatus.EXPIRED_TOKEN, e.getMessage());
    }

    @ExceptionHandler(value = ExpiredTokenException.class)
    public ApiResponse handleExpiredTokenException(ExpiredTokenException e) {
        return ApiResponse.failure(HttpStatus.EXPIRED_TOKEN, e.getMessage());
    }

    @ExceptionHandler(value = UserNotJoinedException.class)
    public ApiResponse handleUserNotJoinedException(UserNotJoinedException e) {
        return ApiResponse.failure(HttpStatus.JOIN_INFORMATION_NOT_EXIST, e.getMessage());
    }

    @ExceptionHandler(value = UserAuthenticationTypeNullException.class)
    public ApiResponse handleUserAuthenticationTypeNullException(UserAuthenticationTypeNullException e) {
        return ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ApiResponse handleInvalidTokenException(InvalidTokenException e) {
        return ApiResponse.failure(HttpStatus.INVALID_TOKEN, e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return ApiResponse.failure(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(value = ApiException.class)
    public ApiResponse handleApiException(ApiException e, HttpServletResponse response) {
        e.printStackTrace();
        response.setStatus(e.getStatusCode());
        return ApiResponse.failure(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResponse handleAbstractException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        sendEmailForExceptionLogs(e, request);
        return ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private void sendEmailForExceptionLogs(Exception e, HttpServletRequest request) {
        switch (phase) {
            case BETA:
            case RELEASE:
                serverErrorLogMailSender.send(e.getMessage(), ExceptionUtils.getStackTrace(e), phase, RequestPrinter.extractRequestInformation(request));
                break;
        }
    }
}
