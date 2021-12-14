package com.video.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(columnList = "title"))
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
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Genre> genres = new HashSet<>();

    @Getter
    @NonNull
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Person> cast = new HashSet<>();

    @Getter
    @NonNull
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Person> directors = new HashSet<>();

    @Getter
    @NonNull
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Person> screenwriters = new HashSet<>();

    private Video(@NonNull String miniaturePath, @NonNull String filePath,
            @NonNull String title, @NonNull Integer year, @NonNull String description,
            @NonNull Set<Genre> genres, @NonNull Set<Person> cast,
            @NonNull Set<Person> directors, @NonNull Set<Person> screenwriters) {
        this.miniaturePath = miniaturePath;
        this.filePath = filePath;
        this.title = title;
        this.year = year;
        this.description = description;
        this.genres.addAll(genres);
        this.cast.addAll(cast);
        this.directors.addAll(directors);
        this.screenwriters.addAll(screenwriters);
    }

    public static Video create(
            String miniaturePath, 
            String filePath,
            String title, 
            Integer year, 
            String description,
            Set<Genre> genres, 
            Set<Person> cast, 
            Set<Person> directors, 
            Set<Person> screenwriters) {

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
