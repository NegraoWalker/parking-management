package com.walker.parkingmanagement.exception;

public class CpfUniqueViolationException extends RuntimeException {
    public CpfUniqueViolationException(String message){
        super(message);
    }
}
