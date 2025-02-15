package com.project.safetynet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

//@AllArgsConstructor
//@Data
public class PersonEmailDTO {
    private String email;


    public PersonEmailDTO(String email) {
        this.email = email;
    }

    public String getEmail(){
        return email;
    }
}

