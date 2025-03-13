package com.project.safetynet.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.safetynet.model.*;
import com.project.safetynet.repository.FirestationRepository;
import com.project.safetynet.repository.MedicalrecordRepository;
import com.project.safetynet.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
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
import java.util.stream.Collectors;

@Service
public class PersonService {
    private List<Person> persons;
    private final DataLoaderService dataLoaderService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MedicalrecordService medicalrecordService;
    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;


    @Autowired
    public PersonService(DataLoaderService dataLoaderService, PersonRepository personRepository, MedicalrecordRepository medicalrecordRepository, FirestationRepository firestationRepository, MedicalrecordService medicalrecordService) {
        this.dataLoaderService = dataLoaderService;
        this.medicalrecordService = medicalrecordService;
        this.persons = new ArrayList<>(); // Évite NullPointerException
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
    }


    @PostConstruct
    public void init() {
        this.persons = new ArrayList<>(dataLoaderService.getPersons()); // Charge les données au démarrage
        System.out.println("Person list initialized with " + persons.size() + " persons.");
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public List<Person> getPersons() {
        List<Person> persons = dataLoaderService.getPersons();
        System.out.println("Person list size: " + (persons != null ? persons.size() : "null"));
        return persons;
    }

    public void addPerson(Person person) {
        File file = new File("src/main/resources/data.json");
        try {
            // Charger le JSON complet dans une Map
            Map<String, Object> data = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {
            });

            // Récupérer la liste des personnes existantes
            List<Person> persons = objectMapper.convertValue(data.get("persons"), new TypeReference<List<Person>>() {
            });

            if (person != null) {
                persons.add(person);
                System.out.println("Person added: " + person.getFirstName() + " " + person.getLastName());

                // Mettre à jour la liste des personnes dans le JSON complet
                data.put("persons", persons);

                // Réécrire tout le JSON (en conservant les firestations et medicalrecords)
                objectMapper.writeValue(file, data);
                System.out.println("JSON file updated successfully.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'écriture du fichier JSON : " + e.getMessage(), e);
        }
    }

    public void deletePerson(String firstName, String lastName) {
        File file = new File("src/main/resources/data.json");

        try {
            // Charger le JSON complet dans une Map
            Map<String, Object> data = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {
            });

            // Modifier uniquement la partie "persons"
            List<Map<String, Object>> persons = (List<Map<String, Object>>) data.get("persons");
            if (persons != null) {
                persons.removeIf(p -> firstName.equals(p.get("firstName")) && lastName.equals(p.get("lastName")));
            }

            // Réécrire le JSON complet avec les données mises à jour
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            System.out.println("JSON file updated successfully.");

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'écriture du fichier JSON : " + e.getMessage(), e);
        }
    }


    public boolean verifyPersonBeforeSaving(Person person) {
        if (person.getFirstName() == null || person.getFirstName().isBlank() ||
                person.getLastName() == null || person.getLastName().isBlank() ||
                person.getAddress() == null || person.getAddress().isBlank() ||
                person.getPhone() == null || !person.getPhone().matches("^\\d{10}$") ||
                person.getEmail() == null || !person.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {

            System.out.println("Échec de la validation : Informations manquantes ou invalides.");
            return false;
        }

        // Vérifie si la personne existe déjà en base
        List<Person> existingPersons = personRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
        if (!existingPersons.isEmpty()) {
            System.out.println("Échec de la validation : Cette personne existe déjà en base.");
            return false;
        }
        return true;
    }

    public List<Person> getPersonByFullName(String firstName, String lastName) {
        return personRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Transactional
    public void deletePersonByFirstNameAndLastName(String firstName, String lastName) {
        List<Person> persons = personRepository.findByFirstNameAndLastName(firstName, lastName);

        if (persons.isEmpty()) {
            System.out.println("Aucune personne trouvée à supprimer.");
        } else {
            System.out.println("Suppression des personnes : " + persons);
            personRepository.deleteAll(persons);
        }
    }

    public List<String> getAllPersonsEmail(String city) {
        System.out.println("Recherche des emails des habitants de: " + city);
        // Trouver les emails des habitants associés à cette ville
        List<String> emails = dataLoaderService.getPersons().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city)) // Filtrer par ville
                .map(Person::getEmail) // Extraire l'email
                .collect(Collectors.toList()); // Collecter les emails dans une liste
        System.out.println("Emails trouvés: " + emails.size());

        if (emails.isEmpty()) {
            System.out.println("Aucun emails trouvés pour cette ville.");
            return new ArrayList<>();
        }
//        List<Person> persons = personRepository.findByEmailIn(emails);
//        System.out.println("Emails trouvés: " + emails.size());

//        return persons.stream()
//                .map(person -> new PersonEmailDTO(person.getEmail()))
//                .collect(Collectors.toList());
//        List<PersonEmailDTO>
        return emails;
    }

    public FamilyDTO getChildFromAddress(String address) {
        System.out.println("Recherche des enfants à l'adresse:" + address);

        // Récupérer toutes les personnes vivant à cette adresse
        List<Person> personsAtAddress = dataLoaderService.getPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        // Liste des mineurs (âge < 18)
        List<PersonChildDTO> minors = personsAtAddress.stream()
                .filter(person -> {
                    int age = medicalrecordService.calculAge(person);
                    return age < 18;
                }).map(person -> new PersonChildDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        medicalrecordService.calculAge(person)
                ))
                .collect(Collectors.toList());

        // Liste des adultes (âge >= 18)
        List<PersonAdultDTO> majors = personsAtAddress.stream()
                .filter(person -> medicalrecordService.calculAge(person) >= 18)
                .map(person -> new PersonAdultDTO(
                        person.getFirstName(),
                        person.getLastName()
                ))
                .collect(Collectors.toList());

        if (minors.isEmpty()) {
            System.out.println("Aucun mineur trouvé à l'adresse:" + address);
            return new FamilyDTO(new ArrayList<>(), new ArrayList<>());
        }

        System.out.println("Enfants trouvés: " + minors.size());
        System.out.println("Adultes trouvés: " + majors.size());
        return new FamilyDTO(minors, majors);
    }

    public List<PersonInfoDTO> getAllPersonsWithName(String lastName) {
        System.out.println("Recherche des personnes avec le nom:" + lastName);
        // Récupérer toutes les personnes ayant ce nom
        List<Person> personsWithName = dataLoaderService.getPersons().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());

        List<PersonInfoDTO> results = personsWithName.stream()
                .map(person -> new PersonInfoDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        medicalrecordService.calculAge(person),
                        person.getEmail(),
                        medicalrecordService.recoverMedications(person),
                        medicalrecordService.recoverAllergies(person)
                ))
                .toList();
        return results;
    }

    public CompleteFireDTO getAllPersonsInfosByAddressFire(String address) {
        System.out.println("Recherche des habitants à l'adresse:" + address);
        // Récupérer toutes les personnes vivant à cette adresse
        List<Person> personsAtAddress = dataLoaderService.getPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        List<Integer> firestationIds = dataLoaderService.getFirestations().stream()
                .filter(firestation -> firestation.getAddress().equalsIgnoreCase(address))
                .map(firestation -> firestation.getId().intValue()) // Conversion Long -> Integer
                .toList();

        Integer firestationId = firestationIds.stream()
                .distinct() // Supprime les doublons
                .reduce((a, b) -> {
                    throw new IllegalStateException("Plusieurs stationId trouvés !");
                })
                .orElse(null);
        System.out.println("StationId:" + firestationId);

        if (personsAtAddress.isEmpty()) {
            System.out.println("Aucun habitant à l'adresse:");
            return new CompleteFireDTO(new ArrayList<>(), firestationId);
        }

        List<PersonFireDTO> persons = personsAtAddress.stream()
                .map(person -> new PersonFireDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getPhone(),
                        medicalrecordService.calculAge(person),
                        medicalrecordService.recoverMedications(person),
                        medicalrecordService.recoverAllergies(person)
                ))
                .toList();

        return new CompleteFireDTO(persons, firestationId);
    }


}
