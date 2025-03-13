package com.project.safetynet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "firestations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Firestation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String address;
    private int station;

    public int getStation() {
        return station;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }


}
