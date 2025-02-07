package com.project.safetynet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity // Indique que cette classe correspond à une table en base de données
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    // Getters et setters (générés avec Lombok)


}
