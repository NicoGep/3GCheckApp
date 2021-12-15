package com.example.a3gcheckapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;

//The class CertificateRecovery extends the class Certificate with specific data needed for proof of recovery certificate.
public class CertificateRecovery extends Certificate {

    private LocalDateTime testDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTestDateAsString() {
        return testDate.format(Certificate.STD_DATE_TIME_FORMAT);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTestDateFromString(String testDate) {
        this.testDate = LocalDateTime.parse(testDate, Certificate.STD_DATE_TIME_FORMAT);
    }

    public LocalDateTime getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDateTime testDate) {
        this.testDate = testDate;
    }

}
