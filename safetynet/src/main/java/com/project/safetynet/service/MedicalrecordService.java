package com.project.safetynet.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.safetynet.model.Firestation;
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
import java.util.Map;
import java.util.Optional;

import static org.jacoco.core.runtime.AgentOptions.OutputMode.file;

@Service
public class MedicalrecordService {
    private final DataLoaderService dataLoaderService;
    private List<Medicalrecord> medicalrecords;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    public MedicalrecordService(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    @PostConstruct
    public void init() {
        this.medicalrecords = new ArrayList<>(dataLoaderService.getMedicalrecords()); // Charge les données au démarrage
        System.out.println("Medicalrecord list initialized with " + medicalrecords.size() + " medicalrecords.");
    }

    public void addMedicalrecord(Medicalrecord medicalrecord) throws IOException {
        File file = new File("src/main/resources/data.json");
        try {
            // Charger le JSON complet dans une Map
            Map<String, Object> data = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {
            });

            // Récupérer la liste des medicalrecords existantes
            List<Medicalrecord> medicalrecords = objectMapper.convertValue(data.get("medicalrecords"), new TypeReference<List<Medicalrecord>>() {
            });

            medicalrecords.add(medicalrecord);

            ObjectMapper objectMapper = new ObjectMapper();
            // Enregistrer le module pour les types Java 8 (LocalDate, etc.)
            objectMapper.registerModule(new JavaTimeModule());
            // Désactiver l'écriture des dates sous forme de timestamp
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("data.json"), medicalrecords);
                System.out.println("JSON file updated successfully.");
            } catch (IOException e) {
                throw new RuntimeException("Erreur de l'écriture du fichier JSON : " + e.getMessage(), e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);


        }
    }

    public void deleteMedicalrecord(String firstName, String lastName) {
        medicalrecords.removeIf(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));

        ObjectMapper objectMapper = new ObjectMapper();
        // Enregistrer le module pour les types Java 8 (LocalDate, etc.)
        objectMapper.registerModule(new JavaTimeModule());
        // Désactiver l'écriture des dates sous forme de timestamp
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("data.json"), medicalrecords);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de l'écriture du fichier JSON : " + e.getMessage(), e);
        }
    }

    public void updateMedicalrecord(Medicalrecord updateMedicalrecord) throws IOException {
//        File file = new File("src/main/resources/data.json");
//        // Charger le JSON complet dans une Map
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> data = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {
//        });
//
//        // Récupérer la liste des personnes existantes
//        List<Medicalrecord> medicalrecords = objectMapper.convertValue(data.get("medicalrecords"), new TypeReference<List<Medicalrecord>>() {
//        });


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
            throw new RuntimeException("Personne non trouvée avec le prénom " + updateMedicalrecord.getFirstName()
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
