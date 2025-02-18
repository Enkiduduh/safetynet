package com.project.safetynet.model;

public class PersonChildDTO {
    private final String firstName;
    private final String lastName;
    private final Integer age;

    public PersonChildDTO(String firstName, String lastName, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Getters et Setters
    public String getLastName() {
        return lastName;
    }

    public String getFirstName()  {
        return firstName;
    }

    public Integer getAge(){
        return age;
    }
}
