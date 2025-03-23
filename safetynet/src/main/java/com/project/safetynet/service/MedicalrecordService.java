package com.project.safetynet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.model.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

    public void addMedicalrecord(Medicalrecord medicalrecord) {
        medicalrecords.add(medicalrecord);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("data.json"), medicalrecords);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de l'écriture du fichier JSON : " + e.getMessage(), e);
        }
    }

    public void deleteMedicalrecord(String firstName, String lastName) {
        medicalrecords.removeIf(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("data.json"), medicalrecords);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de l'écriture du fichier JSON : " + e.getMessage(), e);
        }
    }

    public void updateMedicalrecord(Medicalrecord updateMedicalrecord) {
        // Récupérer la liste actuelle des dossiers depuis DataLoaderService
        List<Medicalrecord> medicalrecords = dataLoaderService.getMedicalrecords();

        // Chercher le dossier à mettre à jour en se basant sur le prénom et le nom
        Optional<Medicalrecord> existingMedicalrecordOpt = medicalrecords.stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(updateMedicalrecord.getFirstName())
                        && m.getLastName().equalsIgnoreCase(updateMedicalrecord.getLastName()))
                .findFirst();

        if (existingMedicalrecordOpt.isPresent()) {
            Medicalrecord existingMedicalrecord = existingMedicalrecordOpt.get();
            // Mise à jour des champs modifiables
            existingMedicalrecord.setBirthdate(updateMedicalrecord.getBirthdate());
            existingMedicalrecord.setAllergies(updateMedicalrecord.getAllergies());
            existingMedicalrecord.setMedications(updateMedicalrecord.getMedications());

            // Sauvegarder la liste mise à jour dans le fichier JSON
            dataLoaderService.saveMedicalrecords(medicalrecords);
        } else {
            throw new RuntimeException("Personne introuvée avec le prénom " + updateMedicalrecord.getFirstName()
                    + " et le nom " + updateMedicalrecord.getLastName());
        }
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
