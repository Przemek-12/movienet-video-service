package com.video.application.genre.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@AllArgsConstructor
public class GenreDTO {

    @NonNull
    private Long id;

    @NonNull
    private String name;
}
