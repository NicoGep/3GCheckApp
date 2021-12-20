package com.example.a3gcheckapp;

import junit.framework.TestCase;

import org.junit.Assert;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class CheckpointScanTest extends TestCase {

    /**
    public void testCheckExpirationDate() throws IOException, CertificateException, ParserConfigurationException, SAXException {
        String xmlCert = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><certificate type=\"recovered\"><firstName>asd</firstName><lastName>asd</lastName><birthdate>01.12.2021</birthdate><issueDate>15.12.2021 11:16</issueDate><recoveryTestDate>02.12.2021 04:00</recoveryTestDate></certificate>";
        Certificate testCert = QRCodeHandler.parseCertificateXMLToCertificate(xmlCert);

        CheckpointScan checkpointScan = new CheckpointScan();

        Assert.assertFalse(checkpointScan.checkExpirationDate(testCert));
    }
     **/
}