package com.example.a3gcheckapp;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

//import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

//import com.google.zxing.client.j2se.BufferedImageLuminanceSource;


//
public class QRCodeHandler {

    public QRCodeHandler() throws IOException, SAXException, ParserConfigurationException {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static Certificate parseCertificateXMLToCertificate(String certificateXML) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(certificateXML));

        Document doc = db.parse(inputSource);

        Element rootElement = doc.getDocumentElement();

        if(!rootElement.getNodeName().equals("nachweis"))
            throw new IOException("Unerwartetes XML Dokument");

        String type = rootElement.getAttribute("type");

        String forname, lastname, birthdate, issuedate, expirationDate;

        switch(type) {

            case "impfung":
                CertificateVaccination vaxcertificate = new CertificateVaccination();

                String vaxDate;
                Vaccine vaccine;

                forname = doc.getElementsByTagName("forename").item(0).getTextContent();
                lastname = doc.getElementsByTagName("lastname").item(0).getTextContent();
                birthdate = doc.getElementsByTagName("birthdate").item(0).getTextContent();
                issuedate = doc.getElementsByTagName("issueDate").item(0).getTextContent();
                //expirationDate = doc.getElementsByTagName("expirationDate").item(0).getTextContent();
                vaxDate = doc.getElementsByTagName("vaccinationDate").item(0).getTextContent();
                //vaccine = Vaccine.valueOf(doc.getElementsByTagName("vaccine").item(0).getTextContent());


                vaxcertificate.setFirstName(forname);
                vaxcertificate.setLastName(lastname);
                vaxcertificate.setBirthdate(birthdate);
                vaxcertificate.setIssueDate(issuedate);
                //impfnachweis.setExpirationDate(expirationDate);
                vaxcertificate.setVaccinationDate(vaxDate);
                //impfnachweis.setVaccine(vaccine);

                return vaxcertificate;

            case "genesung":
                CertificateRecovery recoverycertificate = new CertificateRecovery();

                String recDate;

                forname = doc.getElementsByTagName("forename").item(0).getTextContent();
                lastname = doc.getElementsByTagName("lastname").item(0).getTextContent();
                birthdate = doc.getElementsByTagName("birthdate").item(0).getTextContent();
                issuedate = doc.getElementsByTagName("issueDate").item(0).getTextContent();
                //expirationDate = doc.getElementsByTagName("expirationDate").item(0).getTextContent();
                recDate = doc.getElementsByTagName("recDate").item(0).getTextContent();

                recoverycertificate.setFirstName(forname);
                recoverycertificate.setLastName(lastname);
                recoverycertificate.setBirthdate(birthdate);
                recoverycertificate.setIssueDate(issuedate);
                //genesenennachweis.setExpirationDate(expirationDate);
                recoverycertificate.setTestDate(recDate);

                return recoverycertificate;

            case "test":
                CertificateTest testcertificate = new CertificateTest();

                String testDate;
                //Testtype testType;

                forname = doc.getElementsByTagName("forename").item(0).getTextContent();
                lastname = doc.getElementsByTagName("lastname").item(0).getTextContent();
                birthdate = doc.getElementsByTagName("birthdate").item(0).getTextContent();
                issuedate = doc.getElementsByTagName("issueDate").item(0).getTextContent();
                //expirationDate = doc.getElementsByTagName("expirationDate").item(0).getTextContent();
                testDate = doc.getElementsByTagName("testDate").item(0).getTextContent();
                //testType = Testtype.valueOf(doc.getElementsByTagName("testType").item(0).getTextContent());

                testcertificate.setFirstName(forname);
                testcertificate.setLastName(lastname);
                testcertificate.setBirthdate(birthdate);
                testcertificate.setIssueDate(issuedate);
                //testnachweis.setExpirationDate(expirationDate);
                testcertificate.setTestDate(testDate);
                //testnachweis.setTestType(testType);

                return testcertificate;

            default: return null;
        }


    }

     static Map<String, String> parseQRdataToStringMap(String inputXML) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(inputXML));

        Document doc = db.parse(inputSource);

        if(!doc.getDocumentElement().getNodeName().equals("qrcode"))
            throw new IOException("Unerwartetes XML Dokument");


        Map<String, String> result = new HashMap<String, String>();

        String certif, signatur, certificate;

        certif = doc.getElementsByTagName("nachweis").item(0).getTextContent();
        signatur = doc.getElementsByTagName("signatur").item(0).getTextContent();
        certificate = doc.getElementsByTagName("certificate").item(0).getTextContent();

        result.put("nachweis", certif);
        result.put("signatur", signatur);
        result.put("certificate", certificate);


        return result;
    }
}


