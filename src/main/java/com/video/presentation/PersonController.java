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
import com.video.application.person.PersonService;
import com.video.application.person.dto.AddPersonRequest;
import com.video.application.person.dto.PersonDTO;

@RequestMapping("/person")
@RestController
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public PersonDTO addPerson(@RequestBody AddPersonRequest request) {
        try {
            return personService.addPerson(request);
        } catch (EntityObjectAlreadyExistsException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @DeleteMapping
    public void deletePerson(@RequestParam Long personId) {
        try {
            personService.deletePerson(personId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @GetMapping("/all")
    public List<PersonDTO> findAllDTO() {
        return personService.findAllDTO();
    }

    @GetMapping("/all/name-phrase")
    public List<PersonDTO> findAllDTOByNamePhrase(@RequestParam String namePhrase) {
        return personService.findAllDTOByNamePhrase(namePhrase);
    }

}
