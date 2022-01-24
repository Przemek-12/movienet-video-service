package com.video.domain.specification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.video.domain.Person;
import com.video.domain.repository.PersonRepository;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PersonSpecificationIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void shouldGetByTitlePhrase() {

        entityManager.persist(Person.create("john1", "smith1"));
        entityManager.persist(Person.create("john2", "smith2"));
        entityManager.persist(Person.create("john", "smith3"));
        entityManager.persist(Person.create("john", "smith4"));
        entityManager.persist(Person.create("John", "Smith"));
        entityManager.persist(Person.create("Mike", "Spike"));
        entityManager.persist(Person.create("John", "Mike"));
        entityManager.persist(Person.create("Jimmy", "Cage"));
        entityManager.flush();

        assertThat(personRepository.findAll(PersonSpecification.byFullNamePhrase("Mike"))).hasSize(2);
        assertThat(personRepository.findAll(PersonSpecification.byFullNamePhrase("johnsmith"))).hasSize(2);
        assertThat(personRepository.findAll(PersonSpecification.byFullNamePhrase("j"))).hasSize(4);
        assertThat(personRepository.findAll(PersonSpecification.byFullNamePhrase("MikeSpike"))).hasSize(1);
        assertThat(personRepository.findAll(PersonSpecification.byFullNamePhrase("Mike"))).hasSize(2);
        assertThat(personRepository.findAll(PersonSpecification.byFullNamePhrase("Cage"))).hasSize(1);
    }
}
