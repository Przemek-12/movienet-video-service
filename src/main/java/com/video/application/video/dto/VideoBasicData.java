package com.video.application.video.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
@Builder
public class VideoBasicData {

    @NonNull
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private Integer year;

    @NonNull
    private String description;
}
