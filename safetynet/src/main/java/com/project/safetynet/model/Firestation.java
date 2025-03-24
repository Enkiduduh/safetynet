package com.project.safetynet.model;

import lombok.*;


@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Firestation {
    private String address;
    private int station;

    public Firestation(String address, int station) {
        this.address = address;
        this.station = station;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }


}
