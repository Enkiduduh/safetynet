package com.project.safetynet.repository;

import com.project.safetynet.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // Pas besoin d'implémenter, JpaRepository fournit déjà des méthodes
}
