package com.project.safetynet.service;

import com.project.safetynet.model.*;
import com.project.safetynet.repository.FirestationRepository;
import com.project.safetynet.repository.MedicalrecordRepository;
import com.project.safetynet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalrecordService medicalrecordService;
    private final MedicalrecordRepository medicalrecordRepository;

    public PersonService(PersonRepository personRepository, MedicalrecordRepository medicalrecordRepository, FirestationRepository firestationRepository, MedicalrecordService medicalrecordService) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalrecordRepository = medicalrecordRepository;
        this.medicalrecordService = medicalrecordService;
    }


    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public List<PersonEmailDTO> getAllPersonsEmail(String city) {
        System.out.println("Recherche des emails des habitants de: " + city);
        // Trouver les emails des habitants associés à cette ville
        List<String> emails = personRepository.findAllPersonsEmail(city);
        if (emails.isEmpty()) {
            System.out.println("Aucun emails trouvés pour cette ville.");
            return new ArrayList<>();
        }
        List<Person> persons = personRepository.findByEmailIn(emails);
        System.out.println("Emails trouvés: " + emails.size());

        return persons.stream()
                .map(person -> new PersonEmailDTO(person.getEmail()))
                .collect(Collectors.toList());
    }

    public FamilyDTO getChildFromAddress(String address) {
        System.out.println("Recherche des enfants à l'adresse:" + address);
        // Récupérer toutes les personnes vivant à cette adresse
        List<Person> personsAtAddress = personRepository.findByAddress(address);

        // Filtrer la liste pour obtenir celles dont l'âge est inférieur à 18
        List<PersonChildDTO> minors = personsAtAddress.stream()
                .filter(person -> medicalrecordService.calculAge(person.getFirstName(), person.getLastName(), medicalrecordRepository) < 18) // Filtre les enfants
                .map(person -> new PersonChildDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        medicalrecordService.calculAge(person.getFirstName(), person.getLastName(), medicalrecordRepository) // Calcul de l'âge
                ))
                .collect(Collectors.toList());

        // Filtrer la liste pour obtenir celles dont l'âge est supérieur à 18
        List<PersonAdultDTO> majors = personsAtAddress.stream()
                .filter(person -> medicalrecordService.calculAge(person.getFirstName(), person.getLastName(), medicalrecordRepository) > 18) // Filtre les adultes
                .map(person -> new PersonAdultDTO(
                        person.getFirstName(),
                        person.getLastName()
                ))
                .collect(Collectors.toList());

        if (minors.isEmpty()) {
            System.out.println("Aucun mineur à l'adresse:");
            return new FamilyDTO(new ArrayList<>(), new ArrayList<>());
        }

        System.out.println("Enfants trouvés: " + minors.size());
        System.out.println("Adultes trouvés: " + majors.size());
        return new FamilyDTO(minors, majors);
    }


    public List<PersonInfoDTO> getAllPersonsWithName(String lastName) {
        System.out.println("Recherche des personnes avec le nom:" + lastName);
        // Récupérer toutes les personnes ayant ce nom
        List<Person> personsWithName = personRepository.findByLastName(lastName);

        List<PersonInfoDTO> results = personsWithName.stream()
                .map(person -> new PersonInfoDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        medicalrecordService.calculAge(person.getFirstName(), person.getLastName(), medicalrecordRepository),
                        person.getEmail(),
                        medicalrecordService.recoverMedications(person.getFirstName(), person.getLastName(), medicalrecordRepository),
                        medicalrecordService.recoverAllergies(person.getFirstName(), person.getLastName(), medicalrecordRepository)
                ))
                .toList();
        return results;
    }

    public CompleteFireDTO getAllPersonsInfosByAddressFire(String address) {
        System.out.println("Recherche des habitants à l'adresse:" + address);
        // Récupérer toutes les personnes vivant à cette adresse
        List<Person> personsAtAddress = personRepository.findByAddress(address);

        List<Integer> firestationIds = firestationRepository.findStationIdByAddress(address);
        Integer firestationId = firestationIds.stream()
                .distinct() // Supprime les doublons
                .reduce((a, b) -> { throw new IllegalStateException("Plusieurs stationId trouvés !"); })
                .orElse(null);
        System.out.println("StationId:" + firestationId);

        if (personsAtAddress.isEmpty()) {
            System.out.println("Aucun habitant à l'adresse:");
            return new CompleteFireDTO(new ArrayList<>(),firestationId);
        }

        List<PersonFireDTO> persons = personsAtAddress.stream()
                .map(person -> new PersonFireDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getPhone(),
                        medicalrecordService.calculAge(person.getFirstName(), person.getLastName(), medicalrecordRepository),
                        medicalrecordService.recoverMedications(person.getFirstName(), person.getLastName(), medicalrecordRepository),
                        medicalrecordService.recoverAllergies(person.getFirstName(), person.getLastName(), medicalrecordRepository)
                ))
                .toList();

        return new CompleteFireDTO(persons,firestationId);
    }


}
