package com.project.safetynet.controller;

import com.project.safetynet.model.*;
import com.project.safetynet.service.FirestationService;
import com.project.safetynet.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FirestationController {
    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Test endpoint is working!";
    }


    @GetMapping("/firestation")
    public List<PersonDTO> getPersonsByFirestation(@RequestParam int station) {
        return firestationService.getPersonsByFirestation(station);
    }

    @GetMapping("/phoneAlert")
    public List<PersonPhoneDTO> getPhoneFromPersonByFirestation(@RequestParam int firestation) {
        return firestationService.getPhoneFromPersonByFirestation(firestation);
    }

    @GetMapping("/flood/stations")
    public List<PersonFireDTO> getAllInfoByStation(@RequestParam List<Integer> stationIds) {
        return firestationService.getAllInfoByStation(stationIds);
    }

//    @PostMapping
//    public Firestation addFirestation(@RequestBody Firestation firestation) {
//        System.out.println("firestation added");
//        return firestationService.addFirestation(firestation);
//    }
}
