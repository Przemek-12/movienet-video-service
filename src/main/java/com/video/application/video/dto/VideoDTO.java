package com.video.application.video.dto;

import java.util.Set;

import com.video.domain.Genre;
import com.video.domain.Person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
@Builder
public class VideoDTO {

    @NonNull
    private VideoBasicData basicData;

    @NonNull
    private Set<Genre> genres;

    @NonNull
    private Set<Person> cast;

    @NonNull
    private Set<Person> directors;

    @NonNull
    private Set<Person> screenwriters;
}
