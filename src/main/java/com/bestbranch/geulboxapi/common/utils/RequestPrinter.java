package com.bestbranch.geulboxapi.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
public class RequestPrinter {

    public static String extractRequestInformation(HttpServletRequest request) {
        StringBuilder requestInformation = new StringBuilder();

        requestInformation.append(String.format("\nENDPOINT = %s", request.getRequestURI()));
        requestInformation.append(String.format("\nMETHOD = %s", request.getMethod()));

        Enumeration headerNames = request.getHeaderNames();
        if (headerNames != null) {
            requestInformation.append("\n\n[HEADER]\n");
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                requestInformation.append(key + "=" + value + "\n");
            }
        }


        Enumeration parameterNames = request.getParameterNames();
        if (parameterNames != null) {
            requestInformation.append("\n\n[PARAM]\n");
            while (parameterNames.hasMoreElements()) {
                String key = (String) parameterNames.nextElement();
                String value = request.getParameter(key);
                requestInformation.append(key + "=" + value + "\n");
            }
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            requestInformation.append("\n\n[COOKIE]\n");
            for (Cookie cookie : cookies) {
                requestInformation.append(cookie.getName() + "=" + cookie.getValue() + "\n");
            }
        }

        return requestInformation.toString();
    }
}
