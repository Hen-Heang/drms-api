package com.heang.drms_api.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiStatus {
    private final int code;
    private final String message;

    public ApiStatus(Code statusCode) {
        this(statusCode.getCode(), statusCode.getMessage());
    }
}
