package com.project.safetynet.model;

import java.util.List;

public class PersonInfoDTO {
    private String lastName;
    private String firstName;
    private String address;
    private Integer age;
    private String email;
    private List<String> medications;
    private List<String> allergies;

    public PersonInfoDTO(String lastName, String firstName, String address, Integer age, String email, List<String> medications, List<String> allergies) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
        this.age = age;
        this.email = email;
        this.medications = medications;
        this.allergies = allergies;
    }

    public String getLastName(){
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
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

}


