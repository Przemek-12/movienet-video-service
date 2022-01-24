package com.video.application.person.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PersonDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
}
