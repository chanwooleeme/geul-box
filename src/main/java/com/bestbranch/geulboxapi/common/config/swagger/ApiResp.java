package com.bestbranch.geulboxapi.common.config.swagger;

import com.bestbranch.geulboxapi.common.exception.model.ErrorCode;
import io.swagger.annotations.ResponseHeader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResp {
    int code() default 0;

    String message() default "";

    ErrorCode errorCode() default ErrorCode.DUMMY;

    Class response() default Void.class;

    String reference() default "";

    ResponseHeader[] responseHeaders() default {@ResponseHeader()};

    String responseContainer() default "";
}
