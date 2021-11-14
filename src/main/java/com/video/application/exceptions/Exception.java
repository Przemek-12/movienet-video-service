package com.video.application.exceptions;

import org.springframework.http.HttpStatus;

public interface Exception {

    HttpStatus getStatus();

    String getMessage();
}
