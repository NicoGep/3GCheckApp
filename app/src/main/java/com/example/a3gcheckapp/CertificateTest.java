package com.example.a3gcheckapp;

//The class CertificateTest extends the class Certificate with specific data needed for test certificate.
public class CertificateTest extends Certificate {

    private Testtype testType;
    private String testDate;

    public Testtype getTestType() {
        return testType;
    }

    public void setTestType(Testtype testType) {
        this.testType = testType;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

}