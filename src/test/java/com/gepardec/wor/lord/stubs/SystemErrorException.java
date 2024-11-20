package com.gepardec.wor.lord.stubs;

public class SystemErrorException extends RuntimeException {
    public SystemErrorException(String message, Exception e) {
        super(message);
    }
}
