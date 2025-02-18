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

    @PostMapping
    public Person addPerson(@RequestBody Person person) {
        System.out.println("person added");
        return personService.savePerson(person);
    }


}
