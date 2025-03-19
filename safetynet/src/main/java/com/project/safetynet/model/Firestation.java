package com.project.safetynet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "firestations")
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Firestation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String address;
    private int station;

    public Firestation(String address, int station) {
        this.address = address;
        this.station = station;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }


}
