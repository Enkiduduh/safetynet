package com.project.safetynet.model;

import java.util.List;

public class PersonFireDTO {
    private String lastName;
    private String firstName;
    private Integer age;
    private String phone;
    private List<String> medications;
    private List<String> allergies;
//    private Integer firestation;

    public PersonFireDTO(String lastName,
                         String firstName,
                         String phone,
                         Integer age,
                         List<String> medications,
                         List<String> allergies
//                         Integer firestation
    ) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
//        this.firestation = firestation;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getAge() {
        return age;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public List<String> getMedications() {
        return medications;
    }

//    public Integer getFirestation() {
//        return firestation;
//    }
}
