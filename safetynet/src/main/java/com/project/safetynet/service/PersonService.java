package com.project.safetynet.service;

import com.project.safetynet.model.Person;
import com.project.safetynet.model.PersonEmailDTO;
import com.project.safetynet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;


    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public List<PersonEmailDTO> getAllPersonsEmail(String city) {
        System.out.println("Recherche des emails des habitants de: " + city);
        // Trouver les emails des habitants associés à cette ville
        List<String> emails = personRepository.findAllPersonsEmail(city);
        if (emails.isEmpty()) {
            System.out.println("Aucun emails trouvés pour cette ville.");
            return new ArrayList<>();
        }
        List<Person> persons = personRepository.findByEmailIn(emails);
        System.out.println("Emails trouvés: " + emails.size());

        return persons.stream()
                .map(person -> new PersonEmailDTO(person.getEmail()))
                .collect(Collectors.toList());
    }
}
