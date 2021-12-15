package com.example.a3gcheckapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// The class Certificate includes all data given in all three kinds of certificates.
@RequiresApi(api = Build.VERSION_CODES.O)
public class Certificate {
    public static final DateTimeFormatter STD_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public static final DateTimeFormatter STD_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private String lastName, firstName;
    private LocalDateTime issueDate;
    private LocalDate birthdate;


        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public LocalDate getBirthdate() {
            return birthdate;
        }
        public void setBirthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
        }

        public String getBirthdateAsString() {
            return birthdate.format(Certificate.STD_DATE_FORMAT);;
        }

        public void setBirthdateFromString(String birthdate) {
            this.birthdate = LocalDate.parse(birthdate, Certificate.STD_DATE_FORMAT);
        }

        public String getIssueDateAsString() {
           return issueDate.format(Certificate.STD_DATE_TIME_FORMAT);
        }

        public void setIssueDateFromString(String issueDate) {
            this.issueDate = LocalDateTime.parse(issueDate, Certificate.STD_DATE_TIME_FORMAT);
        }

        public LocalDateTime getIssueDate() {
            return issueDate;
        }

        public void setIssueDate(LocalDateTime issueDate) {
            this.issueDate = issueDate;
        }



    }

