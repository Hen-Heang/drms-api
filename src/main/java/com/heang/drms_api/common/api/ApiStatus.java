package com.heang.drms_api.common.api;

public record ApiStatus(int code, String message) {
    public ApiStatus(Code statusCode) {
        this(statusCode.getCode(), statusCode.getMessage());
    }
}
