package com.project.safetynet.controller;

import com.project.safetynet.model.Medicalrecord;
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

//    @DeleteMapping("/medicalrecord")
//    public ResponseEntity<String> deleteMedicalrecord(@RequestParam String firstName, @RequestParam String lastName) {
//        Optional<Medicalrecord> optionalMedicalrecord = medicalrecordRepository.findByFirstNameAndLastName(firstName, lastName);
//        if (optionalMedicalrecord.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dossier medical non trouvé avec le nom : " + firstName + " " + lastName);
//        }
//        medicalrecordService.deleteMedicalRecordByFirstNameAndLastName(firstName, lastName);
//        return ResponseEntity.ok("Dossier medical supprimé avec succès pour: " + firstName + " " + lastName);
//    }
}
