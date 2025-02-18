package com.project.safetynet.model;

import java.util.List;

public class CompleteFireDTO {
    private List<PersonFireDTO> persons;
    private Integer firestationId;

    public CompleteFireDTO(List<PersonFireDTO> persons, Integer firestationId){
        this.persons = persons;
        this.firestationId = firestationId;
    }

    public List<PersonFireDTO> getPersons() {
        return persons;
    }

    public Integer getFirestationId() {
        return firestationId;
    }
}
