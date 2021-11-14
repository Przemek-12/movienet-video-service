package com.video.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
//@Table(indexes = @Index(columnList = "firstName, lastName"))
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @NonNull
    @NotNull
    @Getter
    private String firstName;

    @NonNull
    @NotNull
    @Getter
    private String lastName;

    public static Person create(String firstName, String lastName) {
        return new Person(firstName, lastName);
    }

    public String getFullName() {
        return new StringBuilder().append(firstName).append(" ").append(lastName).toString();
    }

}
