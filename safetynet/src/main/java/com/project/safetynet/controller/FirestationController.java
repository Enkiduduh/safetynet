package com.project.safetynet.controller;

import com.project.safetynet.model.*;
import com.project.safetynet.service.DataLoaderService;
import com.project.safetynet.service.FirestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FirestationController {
    private final FirestationService firestationService;

    @Autowired
    private DataLoaderService dataLoaderService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/firestations")
    public List<Firestation> getAllFirestations() {
        return dataLoaderService.getFirestations();
    }

    @PostMapping("/firestation")
    public String addFirestation(@RequestBody Firestation firestation) {
        firestationService.addFirestation(firestation);
        return "Firestation ajoutée avec succès.";
    }

    @PutMapping("/firestation")
    public String updateFirestation(@RequestBody Firestation updatedFirestation) {
        firestationService.updateFirestation(updatedFirestation);
        return "Caserne modifiée avec succès.";
    }

    @DeleteMapping("/firestation")
    public String deletePerson(@RequestParam String address, @RequestParam Integer station) {
        firestationService.deleteFirestation(address, Integer.valueOf(String.valueOf(station)));
        return "Firestation supprimée avec succès : " + address + " " + station;
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


}
