package com.project.safetynet.model;

import java.util.List;

public class FamilyDTO {
    private List<PersonChildDTO> children;
    private List<PersonAdultDTO> adults;

    public FamilyDTO(List<PersonChildDTO> children, List<PersonAdultDTO> adults) {
        this.children = children;
        this.adults = adults;
    }

    public List<PersonChildDTO> getChildren(){
        return children;
    }

    public List<PersonAdultDTO> getAdults(){
        return adults;
    }
}
