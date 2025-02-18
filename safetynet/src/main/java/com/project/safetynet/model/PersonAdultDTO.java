package com.project.safetynet.model;

public class PersonAdultDTO {
    private  String firstName;
    private  String lastName;

    public PersonAdultDTO(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }
}
