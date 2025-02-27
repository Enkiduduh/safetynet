package com.project.safetynet.controller;

import com.project.safetynet.model.*;
import com.project.safetynet.service.FirestationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FirestationController {
    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
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
    public Map<String, List<PersonFireDTO>> getAllInfoByStation(@RequestParam List<Integer> stationIds) {
        return firestationService.getAllInfoByStation(stationIds);
    }

//    @PostMapping
//    public Firestation addFirestation(@RequestBody Firestation firestation) {
//        System.out.println("firestation added");
//        return firestationService.addFirestation(firestation);
//
//    }
}
