package com.video.application.video.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class VideoBasicData {

    private Long id;
    private String title;
    private Integer year;
    private String description;

}
