package com.project.safetynet.controller;

import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.model.Person;
import com.project.safetynet.service.DataLoaderService;
import com.project.safetynet.service.MedicalrecordService;
import com.project.safetynet.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MedicalrecordController {
    private final MedicalrecordService medicalrecordService;
    private final PersonService personService;

    public MedicalrecordController(MedicalrecordService medicalrecordService, PersonService personService) {
        this.medicalrecordService = medicalrecordService;
        this.personService = personService;
    }

    @Autowired
    private DataLoaderService dataLoaderService;


    @GetMapping("/medicalrecords")
    public List<Medicalrecord> getAllMedicalrecords() {
        return dataLoaderService.getMedicalrecords();
    }

    @PostMapping("/medicalrecord")
    public String addPerson(@RequestBody Medicalrecord medicalrecord) {
        medicalrecordService.addMedicalrecord(medicalrecord);
        return "Dossier ajouté avec succès.";
    }

    @PutMapping("/medicalrecord")
    public String updateMedicalrecord(@RequestBody Medicalrecord updatedMedicalrecord) {
        medicalrecordService.updateMedicalrecord(updatedMedicalrecord);
        return "Dossier modifié avec succès.";
    }

    @DeleteMapping("/medicalrecord")
    public String deteleMedicalrecord(@RequestParam String firstName, @RequestParam String lastName) {
        medicalrecordService.deleteMedicalrecord(firstName, lastName);
        return "Dossier supprimé avec succès : " + firstName + " " + lastName;
    }
}
