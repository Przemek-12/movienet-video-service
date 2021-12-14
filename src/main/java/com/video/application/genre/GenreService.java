package com.video.application.genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.video.application.exceptions.EntityObjectAlreadyExistsException;
import com.video.application.exceptions.EntityObjectNotFoundException;
import com.video.application.genre.dto.AddGenreRequest;
import com.video.application.genre.dto.GenreDTO;
import com.video.domain.Genre;
import com.video.domain.repository.GenreRepository;
import com.video.domain.specification.GenreSpecification;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public GenreDTO addGenre(AddGenreRequest request) throws EntityObjectAlreadyExistsException {
        checkIfGenreAlreadyExists(request);
        return mapToGenrenDTO(genreRepository.save(Genre.create(request.getName())));
    }

    public Genre findGenreById(Long id) throws EntityObjectNotFoundException {
        return genreRepository.findById(id)
                .orElseThrow(() -> new EntityObjectNotFoundException(Genre.class.getSimpleName()));
    }

    public void deleteGenre(Long genreId) throws EntityObjectNotFoundException {
        checkIfGenreExistsById(genreId);
        genreRepository.deleteById(genreId);
    }

    public List<GenreDTO> findAllDTO() {
        return genreRepository.findAll().stream().map(genre -> mapToGenrenDTO(genre)).collect(Collectors.toList());
    }

    public List<GenreDTO> findAllDTOByNamePhrase(String namePhrase) {
        return genreRepository.findAll(GenreSpecification.byNamePhrase(namePhrase)).stream()
                .map(genre -> mapToGenrenDTO(genre)).collect(Collectors.toList());
    }

    public List<Genre> findAllByIdsIn(Set<Long> ids) {
        return genreRepository.findAllById(ids);
    }

    private void checkIfGenreAlreadyExists(AddGenreRequest request) throws EntityObjectAlreadyExistsException {
        if (genreRepository.existsByName(request.getName())) {
            throw new EntityObjectAlreadyExistsException(Genre.class.getSimpleName());
        }
    }

    private void checkIfGenreExistsById(Long genreId) throws EntityObjectNotFoundException {
        if (!genreRepository.existsById(genreId)) {
            throw new EntityObjectNotFoundException(Genre.class.getSimpleName());
        }
    }

    private GenreDTO mapToGenrenDTO(Genre genre) {
        return GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

}
