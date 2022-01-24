package com.video.application.genre;

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
import com.video.application.genre.dto.AddGenreRequest;
import com.video.domain.Genre;
import com.video.domain.repository.GenreRepository;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @InjectMocks
    private GenreService genreService;

    @Mock
    private GenreRepository genreRepository;

    private void whenGenreRepositorySaveThenAnswer() {
        when(genreRepository.save(Mockito.any(Genre.class)))
                .thenAnswer(i -> i.getArgument(0));
    }

    private void genreExistsByName(boolean bool) {
        when(genreRepository.existsByName(Mockito.anyString()))
                .thenReturn(bool);
    }

    private void genreExistsById(boolean bool) {
        when(genreRepository.existsById(Mockito.anyLong()))
                .thenReturn(bool);
    }

    private void whenGenreRepositoryFindByIdThenReturn(Optional<Genre> genre) {
        when(genreRepository.findById(Mockito.anyLong()))
                .thenReturn(genre);
    }

    private void whenGenreRepositoryFindAllTheReturn(int size) {
        when(genreRepository.findAll())
                .thenReturn(Mocks.genresMock(size));
    }

    private void whenGenreRepositoryFindAllBySpecificationTheReturn(int size) {
        when(genreRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Mocks.genresMock(size));
    }

    private void whenGenreRepositoryFindAllByIdsInThenAnswer() {
        when(genreRepository.findAllById(Mockito.anySet()))
                .thenAnswer(i -> Mocks.genresMock(((Set<?>) i.getArgument(0)).size()));
    }

    @Test
    void shouldAddGenre() throws EntityObjectAlreadyExistsException {
        whenGenreRepositorySaveThenAnswer();
        genreExistsByName(false);

        assertThat(genreService.addGenre(new AddGenreRequest("name"))).isNotNull();
        verify(genreRepository, times(1)).save(Mockito.any(Genre.class));
    }

    @Test
    void shouldNotAddGenreIfAlreadyExists() {
        genreExistsByName(true);

        assertThrows(EntityObjectAlreadyExistsException.class,
                () -> genreService.addGenre(new AddGenreRequest("name")),
                "Genre already exists.");
        verify(genreRepository, never()).save(Mockito.any(Genre.class));
    }

    @Test
    void shouldFindGenreById() throws EntityObjectNotFoundException {
        whenGenreRepositoryFindByIdThenReturn(Optional.of(Mocks.genreMock()));
        
        assertThat(genreService.findGenreById(1L)).isNotNull();
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundById() {
        whenGenreRepositoryFindByIdThenReturn(Optional.empty());

        assertThrows(EntityObjectNotFoundException.class,
                () -> genreService.findGenreById(1L),
                "Genre not found.");

    }

    @Test
    void shouldDeleteGenre() throws EntityObjectNotFoundException {
        genreExistsById(true);

        genreService.deleteGenre(1L);

        verify(genreRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldNotDeleteGenreIfNotExistsById() {
        genreExistsById(false);

        assertThrows(EntityObjectNotFoundException.class,
                () -> genreService.deleteGenre(1L),
                "Genre not found.");

        verify(genreRepository, never()).deleteById(1L);
    }

    @Test
    void shouldFindAllDTO() {
        final int EXPECTED_SIZE = 10;

        whenGenreRepositoryFindAllTheReturn(EXPECTED_SIZE);

        assertThat(genreService.findAllDTO()).hasSize(EXPECTED_SIZE);
    }

    @Test
    void shouldFindAllDTOByNamePhrase() {
        final int EXPECTED_SIZE = 10;

        whenGenreRepositoryFindAllBySpecificationTheReturn(EXPECTED_SIZE);

        assertThat(genreService.findAllDTOByNamePhrase("john")).hasSize(EXPECTED_SIZE);
    }

    @Test
    void shouldFindAllByIdsIn() {
        final Set<Long> ids = Set.of(1L, 2L, 3L);

        whenGenreRepositoryFindAllByIdsInThenAnswer();

        assertThat(genreService.findAllByIdsIn(ids)).hasSize(ids.size());
    }

}
