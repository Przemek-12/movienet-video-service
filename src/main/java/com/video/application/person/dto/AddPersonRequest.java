package com.video.application.person.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class AddPersonRequest {

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;
}
