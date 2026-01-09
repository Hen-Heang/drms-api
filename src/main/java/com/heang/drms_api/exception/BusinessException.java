package com.heang.drms_api.exception;

import com.heang.drms_api.common.api.Code;
import lombok.Getter;

@Getter


public class BusinessException extends RuntimeException{
    private Object body;
    private final Code errorCode;

    public BusinessException(Code errorCode, Object body) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.body = body;
    }

    public BusinessException(Code errorCode, String message) {

        super(message);
        this.errorCode = errorCode;

    }

    public BusinessException(Code errorCode) {

        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }

    public BusinessException(Code errorCode, Throwable e) {

        this(errorCode);
//        AppLogManager.error(e);

    }



}
