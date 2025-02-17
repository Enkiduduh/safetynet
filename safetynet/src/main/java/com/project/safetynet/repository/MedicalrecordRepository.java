package com.project.safetynet.repository;

import com.project.safetynet.model.Medicalrecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalrecordRepository extends JpaRepository<Medicalrecord, Long> {
    Optional<Medicalrecord> findByFirstNameAndLastName(String firstName, String lastName);
}
