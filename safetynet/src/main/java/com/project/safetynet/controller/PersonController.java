package com.project.safetynet.controller;

import com.project.safetynet.model.Person;
import com.project.safetynet.model.PersonEmailDTO;
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


//    @GetMapping("/childAlert")
//    public List<Person> getChildByFamily(@RequestParam String address) {
//        return personService.getChildByFamily(address);
//    }
    @GetMapping("/childAlert")
    public List<Person> getChildFromAddress(@RequestParam String address){
        return personService.getChildFromAddress(address);
    }


//    @GetMapping("/phoneAlert")
//    public List<Person> getPhoneFromPersonByFirestation(@RequestParam int station) {
//        return personService.getPhoneFromPersonByFirestation(station);
//    }
//
//    @GetMapping("/fire")
//    public List<Person> getAllPersonsInfosByAddress(@RequestParam String address) {
//        return personService.getAllPersonsInfosByAddress(address);
//    }

//    @GetMapping("/personInfolastName")



    @GetMapping("/communityEmail")
    public List<PersonEmailDTO> getAllPersonsEmail(@RequestParam String city) {
        return personService.getAllPersonsEmail(city);
    }

    @PostMapping
    public Person addPerson(@RequestBody Person person){
        System.out.println("person added");
        return personService.savePerson(person);
    }


}
