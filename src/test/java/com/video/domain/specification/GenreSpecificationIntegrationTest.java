package com.video.domain.specification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.video.domain.Genre;
import com.video.domain.repository.GenreRepository;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class GenreSpecificationIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldGetByNamePhrase() {

        entityManager.persist(Genre.create("abcdefgh"));
        entityManager.persist(Genre.create("abcdefg"));
        entityManager.persist(Genre.create("zzzzzzfg"));
        entityManager.persist(Genre.create("abcdef"));
        entityManager.persist(Genre.create("abcde"));
        entityManager.persist(Genre.create("abcd"));
        entityManager.persist(Genre.create("abc"));
        entityManager.persist(Genre.create("ab"));
        entityManager.persist(Genre.create("a"));
        entityManager.flush();

        assertThat(genreRepository.findAll(GenreSpecification.byNamePhrase("a"))).hasSize(8);
        assertThat(genreRepository.findAll(GenreSpecification.byNamePhrase("ab"))).hasSize(7);
        assertThat(genreRepository.findAll(GenreSpecification.byNamePhrase("bcd"))).hasSize(5);
        assertThat(genreRepository.findAll(GenreSpecification.byNamePhrase("fg"))).hasSize(3);
        assertThat(genreRepository.findAll(GenreSpecification.byNamePhrase("fgh"))).hasSize(1);
        assertThat(genreRepository.findAll(GenreSpecification.byNamePhrase("abcdefgh"))).hasSize(1);
    }
}
