package com.video.application.exceptions;

import java.io.IOException;

import org.springframework.http.HttpStatus;

public class FileException extends IOException implements Exception {

    private static final long serialVersionUID = 1L;

    public FileException() {
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getMessage() {
        return "File error";
    }
}
