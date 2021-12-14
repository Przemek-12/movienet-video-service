package com.video.application.person;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.video.application.exceptions.EntityObjectAlreadyExistsException;
import com.video.application.exceptions.EntityObjectNotFoundException;
import com.video.application.person.dto.AddPersonRequest;
import com.video.application.person.dto.PersonDTO;
import com.video.domain.Person;
import com.video.domain.repository.PersonRepository;
import com.video.domain.specification.PersonSpecification;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDTO addPerson(AddPersonRequest request) throws EntityObjectAlreadyExistsException {
        checkIfPersonAlreadyExists(request);
        return mapToPersonDTO(personRepository.save(Person.create(request.getFirstName(), request.getLastName())));
    }

    public Person findPersonById(Long id) throws EntityObjectNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new EntityObjectNotFoundException(Person.class.getSimpleName()));
    }

    public void deletePerson(Long personId) throws EntityObjectNotFoundException {
        checkIfPersonExistsById(personId);
        personRepository.deleteById(personId);
    }

    public List<Person> findAllByIdsIn(Set<Long> ids) {
        return personRepository.findAllById(ids);
    }

    public List<PersonDTO> findAllDTO() {
        return personRepository.findAll().stream().map(person -> mapToPersonDTO(person)).collect(Collectors.toList());
    }

    public List<PersonDTO> findAllDTOByNamePhrase(String namePhrase) {
        return personRepository.findAll(PersonSpecification.byFullNamePhrase(namePhrase)).stream()
                .map(person -> mapToPersonDTO(person)).collect(Collectors.toList());
    }

    private void checkIfPersonAlreadyExists(AddPersonRequest request) throws EntityObjectAlreadyExistsException {
        if (personRepository.existsByFirstNameAndLastName(request.getFirstName(), request.getLastName())) {
            throw new EntityObjectAlreadyExistsException(Person.class.getSimpleName());
        }
    }

    private void checkIfPersonExistsById(Long personId) throws EntityObjectNotFoundException {
        if (!personRepository.existsById(personId)) {
            throw new EntityObjectNotFoundException(Person.class.getSimpleName());
        }
    }

    private PersonDTO mapToPersonDTO(Person person) {
        return PersonDTO.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .fullName(person.getFullName())
                .build();
    }

}
