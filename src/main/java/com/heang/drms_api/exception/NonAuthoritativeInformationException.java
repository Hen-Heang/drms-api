package com.heang.drms_api.exception;

public class NonAuthoritativeInformationException extends RuntimeException{
    public NonAuthoritativeInformationException(String message){
        super(message);
    }
}
