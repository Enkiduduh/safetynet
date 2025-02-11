package com.project.safetynet.repository;

import com.project.safetynet.model.Firestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirestationRepository extends JpaRepository<Firestation, Long> {

}
