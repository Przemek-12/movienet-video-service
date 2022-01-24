package com.video.application.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.video.application.Mocks;
import com.video.application.exceptions.EntityObjectAlreadyExistsException;
import com.video.application.exceptions.EntityObjectNotFoundException;
import com.video.application.person.dto.AddPersonRequest;
import com.video.domain.Person;
import com.video.domain.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    private void whenPersonRepositorySaveThenAnswer() {
        when(personRepository.save(Mockito.any(Person.class)))
                .thenAnswer(i -> i.getArgument(0));
    }

    private void personExistsByFirstNameAndLastName(boolean bool) {
        when(personRepository.existsByFirstNameAndLastName(
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn(bool);
    }

    private void personExistsById(boolean bool) {
        when(personRepository.existsById(Mockito.anyLong()))
                .thenReturn(bool);
    }

    private void whenPersonRepositoryFindByIdThenReturn(Optional<Person> person) {
        when(personRepository.findById(Mockito.anyLong()))
                .thenReturn(person);
    }

    private void whenPersonRepositoryFindAllTheReturn(int size) {
        when(personRepository.findAll())
                .thenReturn(Mocks.personsMock(size));
    }

    private void whenPersonRepositoryFindAllBySpecificationTheReturn(int size) {
        when(personRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Mocks.personsMock(size));
    }

    private void whenPersonRepositoryFindAllByIdsInThenAnswer() {
        when(personRepository.findAllById(Mockito.anySet()))
                .thenAnswer(i -> Mocks.personsMock(((Set<?>) i.getArgument(0)).size()));
    }

    @Test
    void shouldAddGenre() throws EntityObjectAlreadyExistsException {
        whenPersonRepositorySaveThenAnswer();
        personExistsByFirstNameAndLastName(false);

        assertThat(personService.addPerson(new AddPersonRequest("name", "name")))
                .isNotNull();
        verify(personRepository, times(1)).save(Mockito.any(Person.class));
    }

    @Test
    void shouldNotAddGenreIfAlreadyExists() {
        personExistsByFirstNameAndLastName(true);

        assertThrows(EntityObjectAlreadyExistsException.class,
                () -> personService.addPerson(new AddPersonRequest("name", "name")),
                "Person already exists.");
        verify(personRepository, never()).save(Mockito.any(Person.class));
    }

    @Test
    void shouldFindGenreById() throws EntityObjectNotFoundException {
        whenPersonRepositoryFindByIdThenReturn(Optional.of(Mocks.personMock()));

        assertThat(personService.findPersonById(1L)).isNotNull();
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundById() {
        whenPersonRepositoryFindByIdThenReturn(Optional.empty());

        assertThrows(EntityObjectNotFoundException.class,
                () -> personService.findPersonById(1L),
                "Person not found.");
    }

    @Test
    void shouldDeleteGenre() throws EntityObjectNotFoundException {
        personExistsById(true);

        personService.deletePerson(1L);

        verify(personRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldNotDeleteGenreIfNotExistsById() {
        personExistsById(false);

        assertThrows(EntityObjectNotFoundException.class,
                () -> personService.deletePerson(1L),
                "Person not found.");

        verify(personRepository, never()).deleteById(1L);
    }

    @Test
    void shouldFindAllDTO() {
        final int EXPECTED_SIZE = 10;

        whenPersonRepositoryFindAllTheReturn(EXPECTED_SIZE);

        assertThat(personService.findAllDTO()).hasSize(EXPECTED_SIZE);
    }

    @Test
    void shouldFindAllDTOByNamePhrase() {
        final int EXPECTED_SIZE = 10;

        whenPersonRepositoryFindAllBySpecificationTheReturn(EXPECTED_SIZE);

        assertThat(personService.findAllDTOByNamePhrase("john")).hasSize(EXPECTED_SIZE);
    }

    @Test
    void shouldFindAllByIdsIn() {
        final Set<Long> ids = Set.of(1L, 2L, 3L);

        whenPersonRepositoryFindAllByIdsInThenAnswer();

        assertThat(personService.findAllByIdsIn(ids)).hasSize(ids.size());
    }

}
