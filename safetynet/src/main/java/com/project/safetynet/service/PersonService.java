package com.project.safetynet.service;

import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.model.Person;
import com.project.safetynet.model.PersonEmailDTO;
import com.project.safetynet.repository.MedicalrecordRepository;
import com.project.safetynet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final MedicalrecordRepository medicalrecordRepository;


    public PersonService(PersonRepository personRepository, MedicalrecordRepository medicalrecordRepository) {
        this.personRepository = personRepository;
        this.medicalrecordRepository = medicalrecordRepository;
    }

    public int calculAge(String firstName, String lastName, MedicalrecordRepository medicalrecordRepository) {
        LocalDate currentDate = LocalDate.now();

        // Trouver le dossier médical correspondant
        Optional<Medicalrecord> optionalMedicalrecord = medicalrecordRepository.findByFirstNameAndLastName(firstName, lastName);

        if (optionalMedicalrecord.isPresent()) {
            LocalDate birthdate = optionalMedicalrecord.get().getBirthdate();
            return Period.between(birthdate, currentDate).getYears();
        } else {
            throw new RuntimeException("Aucun dossier médical trouvé pour " + firstName + " " + lastName);
        }
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

    public List<Person> getChildFromAddress(String address) {
        System.out.println("Recherche des enfants à l'adresse:" + address);
        // Récupérer toutes les personnes vivant à cette adresse
        List<Person> personsAtAddress = personRepository.findByAddress(address);

        // Filtrer la liste pour obtenir celles dont l'âge est inférieur à 18
        List<Person> minors = personsAtAddress.stream()
                .filter(person -> calculAge(person.getFirstName(), person.getLastName(), medicalrecordRepository) < 18)
                .collect(Collectors.toList());

        System.out.println("👶 Enfants trouvés: " + minors.size());
        return minors;
    }
}
