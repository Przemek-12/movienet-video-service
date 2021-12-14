package com.video.presentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.video.application.exceptions.EntityObjectAlreadyExistsException;
import com.video.application.exceptions.EntityObjectNotFoundException;
import com.video.application.genre.GenreService;
import com.video.application.genre.dto.AddGenreRequest;
import com.video.application.genre.dto.GenreDTO;

@RequestMapping("/genre")
@RestController
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    public GenreDTO addGenre(@RequestBody AddGenreRequest request) {
        try {
            return genreService.addGenre(request);
        } catch (EntityObjectAlreadyExistsException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @DeleteMapping
    public void deleteGenre(@RequestParam Long genreId) {
        try {
            genreService.deleteGenre(genreId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @GetMapping("/all")
    public List<GenreDTO> findAllDTO() {
        return genreService.findAllDTO();
    }

    @GetMapping("/all/name-phrase")
    public List<GenreDTO> findAllDTOByNamePhrase(@RequestParam String namePhrase) {
        return genreService.findAllDTOByNamePhrase(namePhrase);
    }

}
