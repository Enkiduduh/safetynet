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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataLoaderService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalrecordRepository medicalrecordRepository;


    @Autowired
    public DataLoaderService(FirestationRepository firestationRepository,
                             PersonRepository personRepository,
                             MedicalrecordRepository medicalRecordRepository) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.medicalrecordRepository = medicalRecordRepository;
    }

    private List<Person> persons;
    private List<Firestation> firestations;
    private List<Medicalrecord> medicalrecords;

    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    public List<Firestation> getFirestations() {
        return firestationRepository.findAll();
    }

    public List<Medicalrecord> getMedicalrecords() {
        return medicalrecordRepository.findAll();
    }

    @PostConstruct
    public void init() {
        if (personRepository.count() == 0 && firestationRepository.count() == 0 && medicalrecordRepository.count() == 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json")) {

                if (inputStream == null) {
                    throw new RuntimeException("❌ Fichier data.json introuvable ! Vérifiez qu'il est bien placé dans `resources/`.");
                }
                DataWrapper data = objectMapper.readValue(inputStream, DataWrapper.class);
//                this.persons = data.getPersons();  // Chargement des données
//                this.firestations = data.getFirestations();
//                this.medicalrecords = data.getMedicalrecords();
                System.out.println("✅ Données chargées avec succès !");

                personRepository.saveAll(data.getPersons()); // Sauvegarde dans la base de données
                firestationRepository.saveAll(data.getFirestations());
                medicalrecordRepository.saveAll(data.getMedicalrecords());
                System.out.println("✅ Données sauvegardées en BDD avec succès !");

            } catch (IOException e) {
                throw new RuntimeException("❌ Erreur de lecture du fichier JSON : " + e.getMessage(), e);
            }
        }
    }

}
