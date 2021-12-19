package com.example.a3gcheckapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

//The class CertificateTest extends the class Certificate with specific data needed for test certificate.
@RequiresApi(api = Build.VERSION_CODES.O)
public class CertificateTest extends Certificate {

    private Testtype testType;
    private LocalDateTime testDate;

    public Testtype getTestType() {
        return testType;
    }

    public void setTestType(Testtype testType) {
        this.testType = testType;
    }

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