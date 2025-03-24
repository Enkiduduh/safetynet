package com.project.safetynet.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.safetynet.model.DataWrapper;
import com.project.safetynet.model.Firestation;
import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.model.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DataLoaderService {

    private List<Person> persons;
    private List<Firestation> firestations;
    private List<Medicalrecord> medicalrecords;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Medicalrecord> getMedicalrecords() {
        return medicalrecords;
    }

    // Ajoute ce champ pour permettre la configuration du chemin du fichier
    private String filePath = "src/main/resources/data.json";

    public Map<String, Object> getDataFromJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(filePath);
            return objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement du fichier JSON : " + e.getMessage(), e);
        }
    }

    public void savePersons(List<Person> persons) {
        try {
            // On suppose que le fichier JSON contient un objet global avec une clé "persons"
            Map<String, Object> data = getDataFromJson(); // méthode pour charger l'ensemble des données
            data.put("persons", persons);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier JSON : " + e.getMessage(), e);
        }
    }

    public void saveFirestations(List<Firestation> firestations) {
        try {
            Map<String, Object> data = getDataFromJson();
            data.put("firestations", firestations);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier JSON : " + e.getMessage(), e);
        }
    }

    public void saveMedicalrecords(List<Medicalrecord> medicalrecords) {
        try {
            Map<String, Object> data = getDataFromJson();
            data.put("medicalrecords", medicalrecords);

            // Enregistre le module pour gérer LocalDate
            objectMapper.registerModule(new JavaTimeModule());
            // Désactive l'écriture des dates comme timestamps pour respecter ton format "MM/dd/yyyy"
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier JSON : " + e.getMessage(), e);
        }
    }

    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json")) {
            if (inputStream == null) {
                throw new RuntimeException("Fichier data.json introuvable ! Vérifiez qu'il est bien placé dans `resources/`.");
            }
            DataWrapper data = objectMapper.readValue(inputStream, DataWrapper.class);
            System.out.println("Data loaded: " + data.getPersons().size() + " persons");
            this.persons = data.getPersons();  // Chargement des données
            this.firestations = data.getFirestations();
            this.medicalrecords = data.getMedicalrecords();

        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier JSON : " + e.getMessage(), e);
        }
    }


}
