package com.project.safetynet.controller;

import com.project.safetynet.model.Person;
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

//  @GetMapping
//  public List<Person> getAllPersons() {
//      return personService.getAllPersons();
//  }

    @GetMapping("/test2")
    public String testEndpoint() {
        return "Test endpoint is working!";
    }

    @PostMapping
    public Person addPerson(@RequestBody Person person){
        System.out.println("person added");
        return personService.savePerson(person);
    }


}
