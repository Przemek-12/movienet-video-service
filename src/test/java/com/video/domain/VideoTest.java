package com.video.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.video.application.Mocks;
import com.video.domain.Genre;
import com.video.domain.Person;
import com.video.domain.Video;

public class VideoTest {

    @Test
    void shouldAddGenre() {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Genre genre = Mocks.genreMock();
        video.addGenre(genre);
        assertThat(video.getGenres().contains(genre)).isTrue();
    }

    @Test
    void shouldRemoveGenre() {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Genre genre = Mocks.genreMock();
        video.addGenre(genre).removeGenre(genre);
        assertThat(video.getGenres().contains(genre)).isFalse();
    }

    @Test
    void shouldAddCastMember() {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();
        video.addCastMember(person);
        assertThat(video.getCast().contains(person)).isTrue();
    }

    @Test
    void shouldRemoveCastMember() {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();
        video.addCastMember(person).removeCastMember(person);
        assertThat(video.getCast().contains(person)).isFalse();
    }

    @Test
    void shouldAddDirector() {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();
        video.addDirector(person);
        assertThat(video.getDirectors().contains(person)).isTrue();
    }

    @Test
    void shouldRemoveDirector() {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();
        video.addDirector(person).removeDirector(person);
        assertThat(video.getDirectors().contains(person)).isFalse();
    }

    @Test
    void shouldAddScreenwriter() {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();
        video.addScreenwriter(person);
        assertThat(video.getScreenwriters().contains(person)).isTrue();
    }

    @Test
    void shouldRemoveScreenwriter() {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();
        video.addScreenwriter(person).removeScreenwriter(person);
        assertThat(video.getScreenwriters().contains(person)).isFalse();
    }

}
