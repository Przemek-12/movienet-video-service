package com.video.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.video.domain.Person;

public class PersonTest {

    @Test
    void shouldGetFullName() {
        Person person = Person.create("John", "Smith");
        assertThat(person.getFullName()).isEqualTo("John Smith");
    }

}
