package com.project.safetynet.service;

import com.project.safetynet.model.*;
import com.project.safetynet.repository.FirestationRepository;
import com.project.safetynet.repository.MedicalrecordRepository;
import com.project.safetynet.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FirestationService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalrecordRepository medicalrecordRepository;
    private final MedicalrecordService medicalrecordService;

    @Autowired
    public FirestationService(FirestationRepository firestationRepository, PersonRepository personRepository, MedicalrecordRepository medicalrecordRepository, MedicalrecordService medicalrecordService) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.medicalrecordRepository = medicalrecordRepository;
        this.medicalrecordService = medicalrecordService;
    }

    public Firestation addFirestation(Firestation firestation) {
        return firestationRepository.save(firestation);
    }

    public List<Firestation> getAllFirestations() {
        return firestationRepository.findAll();
    }

    public List<PersonDTO> getPersonsByFirestation(int station) {
        System.out.println("Recherche des adresses pour la station: " + station);
        // Trouver les adresses associées à cette firestation
        List<String> addresses = firestationRepository.findAddressesByStationId(station);
        if (addresses.isEmpty()) {
            System.out.println("Aucune adresse trouvée pour cette station.");
            return new ArrayList<>();
        }

        System.out.println("Adresses trouvées: " + addresses);
        List<Person> persons = personRepository.findByAddressIn(addresses);

        System.out.println("👥 Personnes trouvées: " + persons.size());
        return persons.stream()
                .map(person -> new PersonDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()))
                .collect(Collectors.toList());
    }

    public List<PersonPhoneDTO> getPhoneFromPersonByFirestation(int station) {
        System.out.println("Recherche des adresses pour la station: " + station);
        // Trouver les numéros de téléphones associés à cette firestation
        List<String> addresses = firestationRepository.findAddressesByStationId(station);
        if (addresses.isEmpty()) {
            System.out.println("Aucune adresse trouvée pour cette station.");
            return new ArrayList<>();
        }

        System.out.println("Adresses trouvées: " + addresses);
        List<Person> persons = personRepository.findByAddressIn(addresses);

        return persons.stream()
                .map(person -> new PersonPhoneDTO(person.getPhone()))
                .toList();
    }

    public Map<String, List<PersonFireDTO>> getAllInfoByStation(List<Integer> stationIds) {
        //Récuperer toutes les adresses désservies par la station
        List<String> addresses = firestationRepository.findByStationIdIn(stationIds);
        if (addresses.isEmpty()) {
            System.out.println("Aucune adresse trouvée pour cette station.");
            return new HashMap<>();
        }
        System.out.println("Adresses trouvées: " + addresses);
        List<Person> persons = personRepository.findByAddressIn(addresses);

        // Créer une map pour regrouper les personnes par adresse
        Map<String, List<PersonFireDTO>> result = new HashMap<>();

        for (Person person : persons) {
            String address = person.getAddress(); // Récupérer l'adresse

            // Transformer la personne en DTO
            PersonFireDTO dto = new PersonFireDTO(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getPhone(),
                    medicalrecordService.calculAge(person.getFirstName(), person.getLastName(), medicalrecordRepository),
                    medicalrecordService.recoverMedications(person.getFirstName(), person.getLastName(), medicalrecordRepository),
                    medicalrecordService.recoverAllergies(person.getFirstName(), person.getLastName(), medicalrecordRepository)
            );

            // Ajouter le DTO à la bonne adresse dans la map
            result.computeIfAbsent(address, k -> new ArrayList<>()).add(dto);
        }
        return result;
    }
}
