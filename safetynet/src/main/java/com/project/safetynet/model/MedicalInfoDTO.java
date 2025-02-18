package com.project.safetynet.model;

import java.util.List;

public class MedicalInfoDTO {
    private List<String> medications;
    private List<String> allergies;

    public MedicalInfoDTO(List<String> medications, List<String> allergies) {
        this.medications = medications;
        this.allergies = allergies;
    }

    public List<String> getMedications() {
        return medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }
}
