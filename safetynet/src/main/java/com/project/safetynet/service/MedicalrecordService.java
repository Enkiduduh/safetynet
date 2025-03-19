package com.project.safetynet.service;

import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.model.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalrecordService {
    private final DataLoaderService dataLoaderService;
    private List<Medicalrecord> medicalrecords;

    @Autowired
    public MedicalrecordService(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    @PostConstruct
    public void init() {
        this.medicalrecords = new ArrayList<>(dataLoaderService.getMedicalrecords()); // Charge les données au démarrage
        System.out.println("Medicalrecord list initialized with " + medicalrecords.size() + " medicalrecords.");
    }

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

        System.out.println("Date de naissance introuvable pour : " + person.getFirstName() + " " + person.getLastName());
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
