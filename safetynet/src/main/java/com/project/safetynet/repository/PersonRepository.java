package com.project.safetynet.repository;

import com.project.safetynet.model.Person;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByAddressIn(List<String> addresses);

    List<Person> findByEmailIn(List<String> emails);

    @Query("SELECT p.email FROM Person p WHERE p.city = :city")
    List<String> findAllPersonsEmail(@Param("city") String city);

    List<Person> findByAddress(String address);

    List<Person> findByLastName(String lastName);

    List<Person> findByFirstNameAndLastName(String firstName, String lastName);
    @Transactional
    void deleteByFirstNameAndLastName(String firstName, String lastName);
}
