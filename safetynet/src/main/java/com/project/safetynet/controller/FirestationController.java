package com.project.safetynet.controller;

import com.project.safetynet.model.Firestation;
import com.project.safetynet.model.Person;
import com.project.safetynet.model.PersonDTO;
import com.project.safetynet.service.FirestationService;
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

//    @GetMapping("/firestationsAll")
//    public List<Firestation> getAllFirestation() {
//        return firestationService.getAllFirestation();
//    }

    @GetMapping("/firestation")
    public List<PersonDTO> getPersonsByFirestation(@RequestParam int station) {
        return firestationService.getPersonsByFirestation(station);
    }


//    @PostMapping
//    public Firestation addFirestation(@RequestBody Firestation firestation) {
//        System.out.println("firestation added");
//        return firestationService.addFirestation(firestation);
//    }
}
