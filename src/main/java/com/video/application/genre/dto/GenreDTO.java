package com.video.application.genre.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GenreDTO {

    private Long id;
    private String name;
}
