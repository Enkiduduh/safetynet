package com.project.safetynet.repository;

import com.project.safetynet.model.Firestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FirestationRepository extends JpaRepository<Firestation, Long> {
    @Query("SELECT f.address FROM Firestation f WHERE f.station = :station")
    List<String> findAddressesByStationId(@Param("station") int station);

    @Query("SELECT f.station FROM Firestation f WHERE f.address = :address")
    List<Integer> findStationIdByAddress(@Param("address") String address);

    @Query("SELECT f.address FROM Firestation f WHERE f.station IN :station")
    List<String> findByStationIdIn(@Param("station") List<Integer> stationIds);
}