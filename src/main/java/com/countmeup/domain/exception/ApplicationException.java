package com.countmeup.domain.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {
    private HttpStatus httpStatus;
    private final String errorCode;

    public ApplicationException(String errorCode, HttpStatus httpStatus) {
        super(errorCode);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
