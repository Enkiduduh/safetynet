package com.project.safetynet.controller;

import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.service.DataLoaderService;
import com.project.safetynet.service.MedicalrecordService;
import com.project.safetynet.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    public Medicalrecord addMedicalrecord(@RequestBody Medicalrecord medicalrecord) {
        try {
            medicalrecordService.addMedicalrecord(medicalrecord);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return medicalrecord;
    }

    @PutMapping("/medicalrecord")
    public Medicalrecord updateMedicalrecord(@RequestBody Medicalrecord updatedMedicalrecord) {
        try {
            medicalrecordService.updateMedicalrecord(updatedMedicalrecord);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return updatedMedicalrecord;
    }

    @DeleteMapping("/medicalrecord")
    public void deteleMedicalrecord(@RequestParam String firstName, @RequestParam String lastName) {
        medicalrecordService.deleteMedicalrecord(firstName, lastName);
    }
}
