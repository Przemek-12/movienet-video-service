package com.video.presentation;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.video.application.exceptions.EntityObjectAlreadyExistsException;
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

    @GetMapping("/all")
    public List<GenreDTO> findAllDTO() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        getExtraInfo(SecurityContextHolder.getContext().getAuthentication());
        return genreService.findAllDTO();
    }

    @GetMapping("/all/name-phrase")
    public List<GenreDTO> findAllDTOByNamePhrase(@RequestParam String namePhrase) {
        return genreService.findAllDTOByNamePhrase(namePhrase);
    }

    public Map<String, Object> getExtraInfo(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();

        System.out.println(jwt.getTokenValue());
        System.out.println(jwt.getClaims());

        System.out.println(auth);
        System.out.println(auth.getCredentials());
        System.out.println(auth.getPrincipal().toString());
        System.out.println(auth.getDetails());
        System.out.println(auth.getDetails());
        WebAuthenticationDetails oauthDetails = (WebAuthenticationDetails) auth.getDetails();
        return null;
    }
}
