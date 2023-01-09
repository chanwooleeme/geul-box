package com.bestbranch.geulboxapi.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Phase {
    LOCAL("local"), BETA("beta"), RELEASE("release");

    @Getter
    private final String lowerCase;

    public static Phase convertFrom(String value) {
        for (Phase phase : Phase.values()) {
            if (value.toLowerCase().equals(phase.lowerCase)) {
                return phase;
            }
        }
        throw new IllegalArgumentException("올바르지 않은 phase 입니다.");
    }
}
