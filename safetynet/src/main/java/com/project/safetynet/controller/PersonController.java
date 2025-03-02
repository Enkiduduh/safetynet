package com.project.safetynet.controller;

import com.project.safetynet.model.*;
import com.project.safetynet.service.DataLoaderService;
import com.project.safetynet.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PersonController {

    private final PersonService personService;

    @Autowired
    private DataLoaderService dataLoaderService; // Service pour récupérer les données

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return dataLoaderService.getPersons();
    }

    @GetMapping("/childAlert")
    public FamilyDTO getChildFromAddress(@RequestParam String address) {
        return personService.getChildFromAddress(address);
    }

    @GetMapping("/fire")
    public CompleteFireDTO getAllPersonsInfosByAddressFire(@RequestParam String address) {
        return personService.getAllPersonsInfosByAddressFire(address);
    }

    @GetMapping("/personInfolastName")
    public List<PersonInfoDTO> getAllPersonsWithName(@RequestParam String lastName) {
        return personService.getAllPersonsWithName(lastName);
    }

    @GetMapping("/communityEmail")
    public List<PersonEmailDTO> getAllPersonsEmail(@RequestParam String city) {
        return personService.getAllPersonsEmail(city);
    }

    @PostMapping("/person")
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        if (!personService.verifyPersonBeforeSaving(person)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Données invalides ou personne déjà existante.");
        }

        personService.savePerson(person);
        return ResponseEntity.ok("Personne ajoutée avec succès." );
    }

    @DeleteMapping("/person")
    public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        List<Person> persons = personService.getPersonByFullName(firstName, lastName);
        if (persons.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personne non trouvée avec le nom : " + firstName + " " + lastName);
        }
        personService.deletePersonByFirstNameAndLastName(firstName, lastName);
        return ResponseEntity.ok("Personne supprimée avec succès : " + firstName + " " + lastName);
    }
}
