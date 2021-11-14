package com.video.application.exceptions;

import java.io.IOException;

import org.springframework.http.HttpStatus;

public class VideoCreationFailedException extends IOException implements Exception {

    private static final long serialVersionUID = 1L;

    private final String msg;

    public VideoCreationFailedException(String msg) {
        this.msg = msg;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.PRECONDITION_FAILED;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
