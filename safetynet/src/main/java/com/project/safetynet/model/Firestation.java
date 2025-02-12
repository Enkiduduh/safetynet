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
    private Long id;
    private String address;
    private int station;


    public int getStation() {
        return station;
    }
}
