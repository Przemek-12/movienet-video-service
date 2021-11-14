package com.video.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.video.application.exceptions.VideoCreationFailedException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
//@Table(indexes = @Index(columnList = "title"))
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @NonNull
    @NotNull
    @Getter
    private String miniaturePath;

    @NonNull
    @NotNull
    @Getter
    private String filePath;

    @NonNull
    @NotNull
    @Getter
    private String title;

    @NonNull
    @NotNull
    @Getter
    private Integer year;

    @NonNull
    @NotNull
    @Getter
    private String description;
    
    @Getter
    @NonNull
    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Genre> genres;

    @Getter
    @NonNull
    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Person> cast;

    @Getter
    @NonNull
    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Person> directors;

    @Getter
    @NonNull
    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Person> screenwriters;

    public static Video create(
            String miniaturePath, 
            String filePath,
            String title, 
            Integer year, 
            String description,
            Set<Genre> genres, 
            Set<Person> cast, 
            Set<Person> directors, 
            Set<Person> screenwriters) throws VideoCreationFailedException {

        if (genres.isEmpty()) {
            throw new VideoCreationFailedException("Genres cannot be empty.");
        } else if (cast.isEmpty()) {
            throw new VideoCreationFailedException("Cast cannot be empty.");
        } else if (directors.isEmpty()) {
            throw new VideoCreationFailedException("Directors cannot be empty.");
        } else if (screenwriters.isEmpty()) {
            throw new VideoCreationFailedException("Screenwriters cannot be empty.");
        }

        Video video = new Video(
                miniaturePath,
                filePath,
                title,
                year,
                description,
                genres,
                cast,
                directors,
                screenwriters);
        return video;
    }
    
    public Video addGenre(Genre genre) {
        genres.add(genre);
        return this;
    }

    public Video removeGenre(Genre genre) {
        genres.remove(genre);
        return this;
    }

    public Video addCastMember(Person person) {
        cast.add(person);
        return this;
    }

    public Video removeCastMember(Person person) {
        cast.remove(person);
        return this;
    }

    public Video addDirector(Person person) {
        directors.add(person);
        return this;
    }

    public Video removeDirector(Person person) {
        directors.remove(person);
        return this;
    }

    public Video addScreenwriter(Person person) {
        screenwriters.add(person);
        return this;
    }

    public Video removeScreenwriter(Person person) {
        screenwriters.remove(person);
        return this;
    }

}
