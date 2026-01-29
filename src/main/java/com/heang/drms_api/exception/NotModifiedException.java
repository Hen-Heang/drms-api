package com.heang.drms_api.exception;

public class NotModifiedException extends RuntimeException{
    public NotModifiedException(String message){
        super(message);
    }
}
