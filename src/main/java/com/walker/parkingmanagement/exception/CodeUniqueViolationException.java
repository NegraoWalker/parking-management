package com.walker.parkingmanagement.exception;

public class CodeUniqueViolationException extends RuntimeException{
    public CodeUniqueViolationException(String message){
        super(message);
    }
}
