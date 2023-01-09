package com.bestbranch.geulboxapi.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Deprecated
@AllArgsConstructor
@Getter
public enum HttpStatus {
    OK(200, StringUtils.EMPTY),
    CREATED(201, StringUtils.EMPTY),
    BAD_REQUEST(400, StringUtils.EMPTY),
    UNAUTHORIZED(401, StringUtils.EMPTY),
    EXPIRED_TOKEN(402, StringUtils.EMPTY),
    FORBIDDEN(403, StringUtils.EMPTY),
    JOIN_INFORMATION_NOT_EXIST(408, StringUtils.EMPTY),
    ALREADY_EXIST(409, StringUtils.EMPTY),
    RESOURCE_NOT_EXIST(410, StringUtils.EMPTY),
    INVALID_TOKEN(411, StringUtils.EMPTY),
    INTERNAL_SERVER_ERROR(500, StringUtils.EMPTY),
    BAD_GATEWAY(502, StringUtils.EMPTY),
    SERVICE_UNAVAILABLE(503, StringUtils.EMPTY),
    NONE(0, StringUtils.EMPTY);


    private final int statusCode;
    private final String message;
}