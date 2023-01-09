package com.bestbranch.geulboxapi.common.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AuthCodeGenerator {

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateSixDigitsAuthCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 6; ++i) {
            stringBuilder.append(ThreadLocalRandom.current().nextInt(10));
        }
        return stringBuilder.toString();
    }
}
