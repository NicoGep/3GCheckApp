package com.example.a3gcheckapp;

public class Impfnachweis extends Certificate {

    private String vaccine, vaccDate;

    public String getVaccine() {
        return vaccine;
    }
    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getVaccDate() {
        return vaccDate;
    }
    public void setVaccDate(String vaccDate) {
        this.vaccDate = vaccDate;
    }

}

