package com.project.safetynet.repository;

import com.project.safetynet.model.Medicalrecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalrecordRepository extends JpaRepository<Medicalrecord, Long> {
}
