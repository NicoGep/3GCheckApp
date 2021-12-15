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

    //XML field names
    public static final String XML_CERT_ROOT_ELEMENT = "certificate";
    public static final String XML_CERT_ROOT_ATTRIBUTE = "type";
    public static final String XML_CERT_TYPE_ATTRIBUTE_VACCINATED = "vaccinated";
    public static final String XML_CERT_TYPE_ATTRIBUTE_RECOVERED = "recovered";
    public static final String XML_CERT_TYPE_ATTRIBUTE_TESTED = "tested";

    public static final String XML_CERT_FIRST_NAME = "firstName";
    public static final String XML_CERT_LAST_NAME = "lastName";
    public static final String XML_CERT_BIRTHDATE = "birthdate";
    public static final String XML_CERT_ISSUE_DATE = "issueDate";

    public static final String XML_CERT_VACCINATION_DATE = "vaccinationDate";
    public static final String XML_CERT_VACCINE = "vaccine";
    public static final String XML_CERT_VACCINATION_COUNT = "vaccinationCount";

    public static final String XML_CERT_RECOVERY_TEST_DATE = "recoveryTestDate";

    public static final String XML_CERT_TEST_DATE = "testDate";
    public static final String XML_CERT_TEST_TYPE = "testType";


    public static final String XML_QRCODE_ROOT_ELEMENT = "qrCode";

    public static final String XML_QRCODE_CERTIFICATE = "certificate";
    public static final String XML_QRCODE_SIGNATURE = "signature";
    public static final String XML_QRCODE_X509 = "x509";

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

        String type = rootElement.getAttribute(XML_CERT_ROOT_ATTRIBUTE);

        Certificate cert;


        switch(type) {

            case XML_CERT_TYPE_ATTRIBUTE_VACCINATED:
                cert = new CertificateVaccination();

                String vaccDate;
                Vaccine vaccine;
                int vaccinationCount;

                vaccDate = doc.getElementsByTagName(XML_CERT_VACCINATION_DATE).item(0).getTextContent();
                vaccine = Vaccine.valueOf(doc.getElementsByTagName(XML_CERT_VACCINE).item(0).getTextContent());
                vaccinationCount = Integer.parseInt(doc.getElementsByTagName(XML_CERT_VACCINATION_COUNT).item(0).getTextContent());

                ((CertificateVaccination) cert).setVaccinationDateFromString(vaccDate);
                ((CertificateVaccination) cert).setVaccine(vaccine);
                ((CertificateVaccination) cert).setVaccinationCount(vaccinationCount);

                break;

            case XML_CERT_TYPE_ATTRIBUTE_RECOVERED:
                cert = new CertificateRecovery();

                String recDate;

                recDate = doc.getElementsByTagName(XML_CERT_RECOVERY_TEST_DATE).item(0).getTextContent();

                ((CertificateRecovery) cert).setTestDateFromString(recDate);

                break;

            case XML_CERT_TYPE_ATTRIBUTE_TESTED:
                cert = new CertificateTest();

                String testDate;
                Testtype testType;

                testDate = doc.getElementsByTagName(XML_CERT_TEST_DATE).item(0).getTextContent();
                testType = Testtype.valueOf(doc.getElementsByTagName(XML_CERT_TEST_TYPE).item(0).getTextContent());

                ((CertificateRecovery) cert).setTestDateFromString(testDate);
                ((CertificateTest) cert).setTestType(testType);

                break;

            default: return null;

        }

        String forname, lastname, birthdate, issueDate;

        forname = doc.getElementsByTagName(XML_CERT_FIRST_NAME).item(0).getTextContent();
        lastname = doc.getElementsByTagName(XML_CERT_LAST_NAME).item(0).getTextContent();
        birthdate = doc.getElementsByTagName(XML_CERT_BIRTHDATE).item(0).getTextContent();
        issueDate = doc.getElementsByTagName(XML_CERT_ISSUE_DATE).item(0).getTextContent();


        cert.setFirstName(forname);
        cert.setLastName(lastname);
        cert.setBirthdateFromString(birthdate);
        cert.setIssueDateFromString(issueDate);

        return cert;
    }

     static Map<String, String> parseQRdataToStringMap(String inputXML) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(inputXML));

        Document doc = db.parse(inputSource);

         if(!doc.getDocumentElement().getNodeName().equals(XML_QRCODE_ROOT_ELEMENT))
             throw new IOException("Unerwartetes XML Dokument");


         Map<String, String> result = new HashMap<String, String>();

         String nachweis, signatur, certificate;

         nachweis = doc.getElementsByTagName(XML_QRCODE_CERTIFICATE).item(0).getTextContent();
         signatur = doc.getElementsByTagName(XML_QRCODE_SIGNATURE).item(0).getTextContent();
         certificate = doc.getElementsByTagName(XML_QRCODE_X509).item(0).getTextContent();

         result.put(XML_QRCODE_CERTIFICATE, nachweis);
         result.put(XML_QRCODE_SIGNATURE, signatur);
         result.put(XML_QRCODE_X509, certificate);


         return result;
    }
}


