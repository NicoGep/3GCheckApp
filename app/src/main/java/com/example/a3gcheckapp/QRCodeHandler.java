package com.example.a3gcheckapp;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.security.Signature;
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

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
//import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

//
public class QRCodeHandler {
    //String qrCodeData = readQR("AppData/1638890268021.png", "UTF-8");

    //Map<String, String> map = parseQRdataToStringMap(qrCodeData);

    //Certificate cert = parseNachweisXMLToCertificate(map.get("nachweis"));

    public QRCodeHandler() throws IOException, SAXException, ParserConfigurationException {
    }


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
                Impfnachweis vaxcertificate = new Impfnachweis();

                String vaxDate;
                Impfstoff vaccine;

                forname = doc.getElementsByTagName("forename").item(0).getTextContent();
                lastname = doc.getElementsByTagName("lastname").item(0).getTextContent();
                birthdate = doc.getElementsByTagName("birthdate").item(0).getTextContent();
                issuedate = doc.getElementsByTagName("issueDate").item(0).getTextContent();
                //expirationDate = doc.getElementsByTagName("expirationDate").item(0).getTextContent();
                vaxDate = doc.getElementsByTagName("vaccinationDate").item(0).getTextContent();
                //vaccine = Impfstoff.valueOf(doc.getElementsByTagName("vaccine").item(0).getTextContent());


                vaxcertificate.setForname(forname);
                vaxcertificate.setLastname(lastname);
                vaxcertificate.setBirthdate(birthdate);
                vaxcertificate.setIssuedate(issuedate);
                //impfnachweis.setExpirationDate(expirationDate);
                vaxcertificate.setVaccDate(vaxDate);
                //impfnachweis.setVaccine(vaccine);

                return vaxcertificate;

            case "genesung":
                Genesenennachweis recoverycertificate = new Genesenennachweis();

                String recDate;

                forname = doc.getElementsByTagName("forename").item(0).getTextContent();
                lastname = doc.getElementsByTagName("lastname").item(0).getTextContent();
                birthdate = doc.getElementsByTagName("birthdate").item(0).getTextContent();
                issuedate = doc.getElementsByTagName("issueDate").item(0).getTextContent();
                //expirationDate = doc.getElementsByTagName("expirationDate").item(0).getTextContent();
                recDate = doc.getElementsByTagName("recDate").item(0).getTextContent();

                recoverycertificate.setForname(forname);
                recoverycertificate.setLastname(lastname);
                recoverycertificate.setBirthdate(birthdate);
                recoverycertificate.setIssuedate(issuedate);
                //genesenennachweis.setExpirationDate(expirationDate);
                recoverycertificate.setRecDate(recDate);

                return recoverycertificate;

            case "test":
                Testnachweis testcertificate = new Testnachweis();

                String testDate;
                //Testtyp testType;

                forname = doc.getElementsByTagName("forename").item(0).getTextContent();
                lastname = doc.getElementsByTagName("lastname").item(0).getTextContent();
                birthdate = doc.getElementsByTagName("birthdate").item(0).getTextContent();
                issuedate = doc.getElementsByTagName("issueDate").item(0).getTextContent();
                //expirationDate = doc.getElementsByTagName("expirationDate").item(0).getTextContent();
                testDate = doc.getElementsByTagName("testDate").item(0).getTextContent();
                //testType = Testtyp.valueOf(doc.getElementsByTagName("testType").item(0).getTextContent());

                testcertificate.setForname(forname);
                testcertificate.setLastname(lastname);
                testcertificate.setBirthdate(birthdate);
                testcertificate.setIssuedate(issuedate);
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


//    public static String readQR(String path, String charset) throws FileNotFoundException, IOException, NotFoundException {
//
//        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
//
//        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//
//        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(path)))));
//
//        Result result = new MultiFormatReader().decode(binaryBitmap);
//
//        return result.getText();
//    }

}


