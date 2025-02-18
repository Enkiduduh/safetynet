package com.project.safetynet.service;

import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.repository.MedicalrecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalrecordService {

    private final MedicalrecordRepository medicalrecordRepository;

    public MedicalrecordService(MedicalrecordRepository medicalrecordRepository) {
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
