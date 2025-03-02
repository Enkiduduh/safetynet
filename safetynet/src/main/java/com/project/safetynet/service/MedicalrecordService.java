package com.project.safetynet.service;

import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.model.Person;
import com.project.safetynet.repository.MedicalrecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalrecordService {

    private final MedicalrecordRepository medicalrecordRepository;

    @Autowired
    public MedicalrecordService(MedicalrecordRepository medicalrecordRepository) {
        this.medicalrecordRepository = medicalrecordRepository;
    }

    public List<Medicalrecord> getAllMedicalrecords() {
        return medicalrecordRepository.findAll();
    }

    @Transactional
    public void deleteMedicalRecordByFirstNameAndLastName(String firstName, String lastName) {
        Optional<Medicalrecord> optionalMedicalrecord = medicalrecordRepository.findByFirstNameAndLastName(firstName, lastName);
        if (optionalMedicalrecord.isEmpty()) {
            System.out.println("Dossier medical non trouvé avec le nom : " + firstName + " " + lastName);
        }  else {
            System.out.println("Dossier medical supprimé pour le nom : " + firstName + " " + lastName);
            medicalrecordRepository.deleteAll(optionalMedicalrecord);
        }
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

    public List<String> recoverMedications(String firstName, String lastName, MedicalrecordRepository medicalrecordRepository) {
        // Trouver le dossier médical correspondant
        Optional<Medicalrecord> optionalMedicalrecord = medicalrecordRepository.findByFirstNameAndLastName(firstName, lastName);
        if (optionalMedicalrecord.isPresent()) {
            return optionalMedicalrecord.get().getMedications();
        } else {
            throw new RuntimeException("Aucun dossier médical trouvé pour " + firstName + " " + lastName);
        }
    }

    public List<String> recoverAllergies(String firstName, String lastName, MedicalrecordRepository medicalrecordRepository) {
        // Trouver le dossier médical correspondant
        Optional<Medicalrecord> optionalMedicalrecord = medicalrecordRepository.findByFirstNameAndLastName(firstName, lastName);
        if (optionalMedicalrecord.isPresent()) {
            return optionalMedicalrecord.get().getAllergies();
        } else {
            throw new RuntimeException("Aucun dossier médical trouvé pour " + firstName + " " + lastName);
        }
    }
}
