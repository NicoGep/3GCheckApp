package com.example.a3gcheckapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

/**
 * The class CertificateVaccination extends the class Certificate with specific data needed for vaccination certificate.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class CertificateVaccination extends Certificate {

    private LocalDateTime vaccinationDate;
    private Vaccine vaccine;
    private int vaccinationCount;

    public int getVaccinationCount() {
        return vaccinationCount;
    }

    public void setVaccinationCount(int vaccinationCount) {
        this.vaccinationCount = vaccinationCount;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public LocalDateTime getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(LocalDateTime vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getVaccinationDateAsString() {
		return vaccinationDate.format(Certificate.STD_DATE_TIME_FORMAT);
	}
	@RequiresApi(api = Build.VERSION_CODES.O)
    public void setVaccinationDateFromString(String vaccinationDate) {
		this.vaccinationDate = LocalDateTime.parse(vaccinationDate, Certificate.STD_DATE_TIME_FORMAT);
	}



}

