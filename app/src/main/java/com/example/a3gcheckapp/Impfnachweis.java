package com.example.a3gcheckapp;

//The class Impfnachweis extends the class Certificate with specific data needed for vaccination certificate.
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

