package com.project.safetynet.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.safetynet.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirestationService {

    private final DataLoaderService dataLoaderService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MedicalrecordService medicalrecordService;
    private List<Firestation> firestations;

    @Autowired
    public FirestationService(DataLoaderService dataLoaderService, MedicalrecordService medicalrecordService) {
        this.dataLoaderService = dataLoaderService;
        this.firestations = new ArrayList<>();
        this.medicalrecordService = medicalrecordService;
    }

    @PostConstruct
    public void init() {
        this.firestations = new ArrayList<>(dataLoaderService.getFirestations()); // Charge les données au démarrage
        System.out.println("Person list initialized with " + firestations.size() + " persons.");
    }

    public List<Firestation> getFirestations() {
        List<Firestation> firestations = dataLoaderService.getFirestations();
        System.out.println("Firestation list size: " + (firestations != null ? firestations.size() : "null"));
        return firestations;
    }

    public void addFirestation(Firestation firestation) {
        File file = new File("src/main/resources/data.json");
        try {
            // Charger le JSON complet dans une Map
            Map<String, Object> data = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {
            });

            // Récupérer la liste des personnes existantes
            List<Firestation> firestations = objectMapper.convertValue(data.get("firestations"), new TypeReference<List<Firestation>>() {
            });

            if (firestation != null) {
                firestations.add(firestation);
                System.out.println("Person added: " + firestation.getAddress() + " " + firestation.getStation());

                // Mettre à jour la liste des firestations dans le JSON complet
                data.put("firestations", firestations);

                // Réécrire tout le JSON (en conservant les persons et medicalrecords)
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
                System.out.println("JSON file updated successfully.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'écriture du fichier JSON : " + e.getMessage(), e);
        }
    }

    public void updateFirestation(Firestation updateFirestation) {
        // Récupérer la liste actuelle des casernes depuis DataLoaderService
        List<Firestation> firestations = dataLoaderService.getFirestations();

        // Chercher la caserne à mettre à jour en se basant sur l'adresse
        Optional<Firestation> existingFirestationOpt = firestations.stream()
                .filter(m -> m.getAddress().equalsIgnoreCase(updateFirestation.getAddress()))
                .findFirst();

        if (existingFirestationOpt.isPresent()) {
            Firestation existingFirestation = existingFirestationOpt.get();
            // Mise à jour des champs modifiables
            existingFirestation.setStation(updateFirestation.getStation());

            // Sauvegarder la liste mise à jour dans le fichier JSON
            dataLoaderService.saveFirestations(firestations);
        } else {
            throw new RuntimeException("Caserne non trouvée avec l'adresse " + updateFirestation.getAddress());
        }
    }

    public void deleteFirestation(String address, int station) {
        File file = new File("src/main/resources/data.json");

        try {
            // Charger le JSON complet dans une Map
            Map<String, Object> data = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {
            });

            // Modifier uniquement la partie "firestations"
            List<Map<String, Object>> firestations = (List<Map<String, Object>>) data.get("firestations");

            if (firestations != null) {
                firestations.removeIf(p ->
                        address.equals(p.get("address")) && station == (Integer) p.get("station")
                );
            }

            // Réécrire le JSON complet avec les données mises à jour
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            System.out.println("JSON file updated successfully.");

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'écriture du fichier JSON : " + e.getMessage(), e);
        }
    }



    public List<PersonDTO> getPersonsByFirestation(int station) {
        System.out.println("Recherche des adresses pour la station: " + station);

        // Trouver les adresses associées à cette firestation
        List<String> addresses = dataLoaderService.getFirestations().stream()
                .filter(firestation -> firestation.getStation() == station)
                .map(Firestation::getAddress)
                .collect(Collectors.toList());

        if (addresses.isEmpty()) {
            System.out.println("Aucune adresse trouvée pour cette station.");
            return new ArrayList<>();
        }

        System.out.println("Adresses trouvées: " + addresses);

        List<Person> persons = dataLoaderService.getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress())) //  Remplacement de personRepository
                .collect(Collectors.toList());

        System.out.println("👥 Personnes trouvées: " + persons.size());
        return persons.stream()
                .map(person -> new PersonDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()))
                .collect(Collectors.toList());
    }

    public List<PersonPhoneDTO> getPhoneFromPersonByFirestation(int station) {
        System.out.println("Recherche des adresses pour la station: " + station);

        // Trouver les numéros de téléphones associés à cette firestation
        List<String> addresses = dataLoaderService.getFirestations().stream()
                .filter(firestation -> firestation.getStation() == station)
                .map(Firestation::getAddress)
                .collect(Collectors.toList());

        if (addresses.isEmpty()) {
            System.out.println("Aucune adresse trouvée pour cette station.");
            return new ArrayList<>();
        }

        System.out.println("Adresses trouvées: " + addresses);

        // Trouver les personnes vivant aux adresses associées
        List<Person> persons = dataLoaderService.getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress())) //  Remplacement de personRepository
                .collect(Collectors.toList());

        return persons.stream()
                .map(person -> new PersonPhoneDTO(person.getPhone()))
                .distinct() // Evite les doublons
                .toList();
    }

    public Map<String, List<PersonFireDTO>> getAllInfoByStation(List<Integer> stationIds) {
        //Récuperer toutes les adresses désservies par la station

        List<String> addresses = dataLoaderService.getFirestations().stream()
                .filter(firestation -> stationIds.contains(firestation.getStation()))
                .map(Firestation::getAddress)
                .toList();

        if (addresses.isEmpty()) {
            System.out.println("Aucune adresse trouvée pour cette station.");
            return new HashMap<>();
        }
        System.out.println("Adresses trouvées: " + addresses);

        List<Person> persons = dataLoaderService.getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress())) //  Remplacement de personRepository
                .toList();

        // Créer une map pour regrouper les personnes par adresse
        Map<String, List<PersonFireDTO>> result = new HashMap<>();

        for (Person person : persons) {
            String address = person.getAddress(); // Récupérer l'adresse

            // Transformer la personne en DTO
            PersonFireDTO dto = new PersonFireDTO(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getPhone(),
                    medicalrecordService.calculAge(person),
                    medicalrecordService.recoverMedications(person),
                    medicalrecordService.recoverAllergies(person)
            );

            // Ajouter le DTO à la bonne adresse dans la map
            result.computeIfAbsent(address, k -> new ArrayList<>()).add(dto);
        }
        return result;
    }


}
