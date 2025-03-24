package com.project.safetynet.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.safetynet.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

    @Autowired
    public PersonService(DataLoaderService dataLoaderService, MedicalrecordService medicalrecordService) {
        this.dataLoaderService = dataLoaderService;
        this.medicalrecordService = medicalrecordService;
        this.persons = new ArrayList<>(); // Évite NullPointerException
    }

    @PostConstruct
    public void init() {
        this.persons = new ArrayList<>(dataLoaderService.getPersons()); // Charge les données au démarrage
        System.out.println("Person list initialized with " + persons.size() + " persons.");
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
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
                System.out.println("JSON file updated successfully.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'écriture du fichier JSON : " + e.getMessage(), e);
        }
    }

    public void updatePerson(Person updatedPerson) {
        // Récupérer la liste actuelle des personnes depuis DataLoaderService
        List<Person> persons = dataLoaderService.getPersons();
        System.out.println("persons");

        // Chercher la personne à mettre à jour en se basant sur le prénom et le nom
        Optional<Person> existingPersonOpt = persons.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
                        && p.getLastName().equalsIgnoreCase(updatedPerson.getLastName()))
                .findFirst();

        if (existingPersonOpt.isPresent()) {
            Person existingPerson = existingPersonOpt.get();
            // Mise à jour des champs modifiables
            existingPerson.setAddress(updatedPerson.getAddress());
            existingPerson.setCity(updatedPerson.getCity());
            existingPerson.setZip(updatedPerson.getZip());
            existingPerson.setPhone(updatedPerson.getPhone());
            existingPerson.setEmail(updatedPerson.getEmail());

            // Sauvegarder la liste mise à jour dans le fichier JSON
            dataLoaderService.savePersons(persons);
        } else {
            throw new RuntimeException("Personne introuvée avec le prénom " + updatedPerson.getFirstName()
                    + " et le nom " + updatedPerson.getLastName());
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
                .toList();

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
        System.out.println("Recherche des habitants à l'adresse : " + address);

        // Vérifier les adresses existantes
        dataLoaderService.getPersons().forEach(person -> System.out.println("Adresse trouvée : " + person.getAddress()));

        // Récupérer toutes les personnes vivant à cette adresse
        List<Person> personsAtAddress = dataLoaderService.getPersons().stream()
                .filter(person -> person.getAddress() != null && person.getAddress().equalsIgnoreCase(address))
                .toList();

        System.out.println("Personnes trouvées : " + personsAtAddress.size());

        List<Integer> firestationIds = dataLoaderService.getFirestations().stream()
                .filter(firestation -> firestation.getAddress() != null && firestation.getAddress().equalsIgnoreCase(address))
                .map(Firestation::getStation)
                .collect(Collectors.toList());

        Integer firestationId = firestationIds.stream()
                .distinct()
                .reduce((a, b) -> {
                    throw new IllegalStateException("Plusieurs stationId trouvés !");
                })
                .orElse(null);

        // Ajoute une vérification pour éviter une erreur si aucune station n'est trouvée
        if (firestationId == null) {
            System.out.println("Aucune station de pompiers trouvée pour cette adresse !");
        }

        System.out.println("Station ID (avant validation) : " + firestationId);

        if (personsAtAddress.isEmpty()) {
            System.out.println("Aucun habitant à l'adresse : " + address);
            return new CompleteFireDTO(new ArrayList<>(), firestationId);
        }

        List<PersonFireDTO> persons = personsAtAddress.stream()
                .map(person -> {
                    int age = medicalrecordService != null ? medicalrecordService.calculAge(person) : -1;
                    List<String> medications = medicalrecordService != null ? medicalrecordService.recoverMedications(person) : new ArrayList<>();
                    List<String> allergies = medicalrecordService != null ? medicalrecordService.recoverAllergies(person) : new ArrayList<>();

                    return new PersonFireDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getPhone(),
                            age,
                            medications,
                            allergies
                    );
                })
                .toList();

        return new CompleteFireDTO(persons, firestationId);
    }


}
