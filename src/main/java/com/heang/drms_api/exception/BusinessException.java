package com.heang.drms_api.exception;

import com.heang.drms_api.common.api.ExitCode;
import lombok.Getter;

@Getter


public class BusinessException extends RuntimeException{
    private Object body;
    private final ExitCode errorCode;

    public BusinessException(ExitCode errorCode, Object body) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.body = body;
    }

    public BusinessException(ExitCode errorCode, String message) {

        super(message);
        this.errorCode = errorCode;

    }

    public BusinessException(ExitCode errorCode) {

        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }

    public BusinessException(ExitCode errorCode, Throwable e) {

        this(errorCode);
//        AppLogManager.error(e);

    }



}
