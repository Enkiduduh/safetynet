package com.project.safetynet.controller;

import com.project.safetynet.model.*;
import com.project.safetynet.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons")
    public List<Person> getPersons() {
        return personService.getPersons();
    }

    @PostMapping("/person")
    public Person  addPerson(@RequestBody Person person) {
        personService.addPerson(person);
        return person;
    }

    @PutMapping("/person")
    public Person updatePerson(@RequestBody Person updatedPerson) {
        personService.updatePerson(updatedPerson);
        return updatedPerson;
    }

    @DeleteMapping("/person")
    public void deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        personService.deletePerson(firstName, lastName);
    }

    @GetMapping("/childAlert")
    public FamilyDTO getChildFromAddress(@RequestParam String address) {
        return personService.getChildFromAddress(address);
    }

    @GetMapping("/fire")
    public CompleteFireDTO getAllPersonsInfosByAddressFire(@RequestParam String address) {
//        return personService.getAllPersonsInfosByAddressFire(address);
        try {
            return personService.getAllPersonsInfosByAddressFire(address);
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/personInfoLastName")
    public List<PersonInfoDTO> getAllPersonsWithName(@RequestParam String lastName) {
        return personService.getAllPersonsWithName(lastName);
    }

    @GetMapping("/communityEmail")
    public List<String> getAllPersonsEmail(@RequestParam String city) {
        return personService.getAllPersonsEmail(city);
    }


}
