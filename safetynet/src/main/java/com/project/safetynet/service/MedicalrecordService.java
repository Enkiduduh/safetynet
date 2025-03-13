package com.project.safetynet.service;

import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.model.Person;
import com.project.safetynet.repository.MedicalrecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalrecordService {
    private final DataLoaderService dataLoaderService;
    private final MedicalrecordRepository medicalrecordRepository;
    private final PersonService personService;

    @Autowired
    public MedicalrecordService(DataLoaderService dataLoaderService, MedicalrecordRepository medicalrecordRepository, PersonService personService) {
        this.dataLoaderService = dataLoaderService;
        this.medicalrecordRepository = medicalrecordRepository;
        this.personService = personService;
    }

    public List<Medicalrecord> getAllMedicalrecords() {
        return medicalrecordRepository.findAll();
    }

//    @Transactional
//    public void deleteMedicalRecordByFirstNameAndLastName(String firstName, String lastName) {
//        Optional<Medicalrecord> optionalMedicalrecord = medicalrecordRepository.findByFirstNameAndLastName(firstName, lastName);
//        if (optionalMedicalrecord.isEmpty()) {
//            System.out.println("Dossier medical non trouvé avec le nom : " + firstName + " " + lastName);
//        }  else {
//            System.out.println("Dossier medical supprimé pour le nom : " + firstName + " " + lastName);
//            medicalrecordRepository.deleteAll(optionalMedicalrecord);
//        }
//    }



    public int calculAge(Person person) {
        // Récupérer le dossier médical de la personne
        Optional<Medicalrecord> recordOpt = dataLoaderService.getMedicalrecords().stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                        record.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst();

        if (recordOpt.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String birthdate = recordOpt.get().getBirthdate().format(formatter);
            LocalDate birthDate = LocalDate.parse(birthdate, formatter);
            return Period.between(birthDate, LocalDate.now()).getYears();
        }

        System.out.println("⚠️ Date de naissance introuvable pour : " + person.getFirstName() + " " + person.getLastName());
        return 0;
    }


    public List<String> recoverMedications(Person person) {
        // Récupérer le dossier médical de la personne
        Optional<Medicalrecord> recordOpt = dataLoaderService.getMedicalrecords().stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                        record.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst();
        if (recordOpt.isPresent()) {
            return recordOpt.get().getMedications();
        } else {
            throw new RuntimeException("Aucun dossier médical trouvé pour " + person.getFirstName() + " " + person.getLastName());
        }
    }

    public List<String> recoverAllergies(Person person) {
        // Récupérer le dossier médical de la personne
        Optional<Medicalrecord> recordOpt = dataLoaderService.getMedicalrecords().stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                        record.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst();
        if (recordOpt.isPresent()) {
            return recordOpt.get().getAllergies();
        } else {
            throw new RuntimeException("Aucun dossier médical trouvé pour " + person.getFirstName() + " " + person.getLastName());
        }
    }


}
