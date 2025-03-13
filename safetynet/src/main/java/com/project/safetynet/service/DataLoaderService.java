package com.project.safetynet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.safetynet.model.DataWrapper;
import com.project.safetynet.model.Firestation;
import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.model.Person;
import com.project.safetynet.repository.FirestationRepository;
import com.project.safetynet.repository.MedicalrecordRepository;
import com.project.safetynet.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DataLoaderService {

    private List<Person> persons;
    private List<Firestation> firestations;
    private List<Medicalrecord> medicalrecords;

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void addFirestation(Firestation firestation) {
        firestations.add(firestation);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("data.json"), firestations);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de l'écriture du fichier JSON : " + e.getMessage(), e);
        }
    }

    public void deleteFirestations(String address, Integer station) {
        firestations.removeIf(p -> p.getAddress().equals(address) && Objects.equals(p.getStation(), station));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("data.json"), firestations);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de l'écriture du fichier JSON : " + e.getMessage(), e);
        }
    }

    public List<Medicalrecord> getMedicalrecords() {
        return medicalrecords;
    }

    public void addMedicalrecord(Medicalrecord medicalrecord) {
        medicalrecords.add(medicalrecord);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("data.json"), medicalrecords);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de l'écriture du fichier JSON : " + e.getMessage(), e);
        }
    }

    public void deleteMedicalrecord(String firstName, String lastName) {
        medicalrecords.removeIf(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("data.json"), medicalrecords);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de l'écriture du fichier JSON : " + e.getMessage(), e);
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
            System.out.println("Données chargées avec succès !");

        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier JSON : " + e.getMessage(), e);
        }
    }


}
