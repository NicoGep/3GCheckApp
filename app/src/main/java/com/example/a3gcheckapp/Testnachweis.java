package com.example.a3gcheckapp;

//The class Testnachweis extends the class Certificate with specific data needed for test certificate.
public class Testnachweis extends Certificate {

    private String testType, testDate;

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

}