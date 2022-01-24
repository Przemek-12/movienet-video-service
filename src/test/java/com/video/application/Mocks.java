package com.video.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.video.application.video.dto.AddVideoRequest;
import com.video.domain.Genre;
import com.video.domain.Person;
import com.video.domain.Video;

public class Mocks {

    public static Video videoWithEmptyCollectionsMock() {
        return Video.create("minPath", "filePath", "title", 1990,
                "desc", Set.of(), Set.of(), Set.of(), Set.of());
    }

    public static List<Video> videosWithEmptyCollectionsMock(int size) {
        List<Video> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(Video.create("minPath" + i, "filePath" + i, "title" + i, 1990,
                    "desc" + i, Set.of(), Set.of(), Set.of(), Set.of()));
        }
        return list;
    }

    public static List<Genre> genresMock(int size) {
        List<Genre> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(Genre.create("name" + i));
        }
        return list;
    }

    public static List<Person> personsMock(int size) {
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(Person.create("name" + i, "lastname" + i));
        }
        return list;
    }

    public static Genre genreMock() {
        return Genre.create("name");
    }

    public static Person personMock() {
        return Person.create("name", "lastname");
    }

    public static AddVideoRequest addVideoRequestMock() {
        return AddVideoRequest.builder()
                .miniatureFileName("minName")
                .fileName("fName")
                .title("title")
                .year(1990)
                .description("desc")
                .genresIds(Set.of(1L, 2L))
                .castIds(Set.of(1L, 2L))
                .directorsIds(Set.of(1L, 2L))
                .screenwritersIds(Set.of(1L, 2L))
                .build();
    }

}
