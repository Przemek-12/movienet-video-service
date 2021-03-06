package com.video.application.video.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@Builder
public class AddVideoRequest {

    @NonNull
    private String miniatureFileName;

    @NonNull
    private String fileName;

    @NonNull
    private String title;

    @NonNull
    private Integer year;

    @NonNull
    private String description;

    @NonNull
    private Set<Long> genresIds;

    @NonNull
    private Set<Long> castIds;

    @NonNull
    private Set<Long> directorsIds;

    @NonNull
    private Set<Long> screenwritersIds;

}
