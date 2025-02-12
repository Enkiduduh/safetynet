package com.project.safetynet.service;

import com.project.safetynet.model.DataWrapper;
import com.project.safetynet.model.Firestation;
import com.project.safetynet.model.Person;
import com.project.safetynet.model.PersonDTO;
import com.project.safetynet.repository.FirestationRepository;
import com.project.safetynet.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FirestationService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;

    public FirestationService(FirestationRepository firestationRepository, PersonRepository personRepository) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
    }

    public List<Firestation> getAllFirestation() {
        System.out.println("Test log actif ?");
        return firestationRepository.findAll();
    }

    public Firestation addFirestation(Firestation firestation) {
        return firestationRepository.save(firestation);
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
}
