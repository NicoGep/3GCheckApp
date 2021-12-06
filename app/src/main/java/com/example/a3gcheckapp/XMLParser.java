package com.example.a3gcheckapp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class XMLParser {
    public Certificate parseXML(String xml){
        XmlPullParserFactory parserFactory;
        Certificate certificate = null;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            //InputStream is = getAssets().open("data.xml");
            //String xml = "<nachweis>    <lastname>Hoeckh</lastname>    <forname>Celine</forname>    <erstelldatum>01-02-2000</erstelldatum> <lastname>Hoeckh</lastname>    <forname>Celine</forname>    <erstelldatum>01-02-2000</erstelldatum> </nachweis>";
            InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is,null);
            certificate = processParsing(parser);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return certificate;
    }

    private Certificate processParsing(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<Certificate> certificates = new ArrayList<>();
        int eventType = parser.getEventType();
        Certificate currentCertificate = null;
        String tag = "";
        String text = "";

        while(eventType!= XmlPullParser.END_DOCUMENT) {
            tag = parser.getName();;

            switch (eventType) {
                case XmlPullParser.START_TAG:

                    if (tag.equals("nachweis")) {
                        if (parser.getAttributeValue(null, "type").equals("impfung")) {
                            //currentCertificate = new Impfnachweis();
                            currentCertificate = new Certificate();
                            certificates.add(currentCertificate);
//                        } else if (parser.getAttributeValue(null, "type").equals("genesen")) {
//                            currentCertificate = new Genesenennachweis();
//                            certificates.add(currentCertificate);
//                        } else if (parser.getAttributeValue(null, "type").equals("test")) {
//                            currentCertificate = new Testnachweis();
//                            certificates.add(currentCertificate);
                       }
                        break;
                    }

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    switch (tag) {
                        case "lastname":
                            currentCertificate.setLastname(text);
                            break;
                        case "forname":
                            currentCertificate.setForname(text);
                            break;
                        case "birthdate":
                            currentCertificate.setBirthdate(text);
                            break;
                        case "erstelldatum":
                            currentCertificate.setErstelldatum(text);
                            break;
//                        case "vaccine":
//                            currentCertificate.setVaccine(text);
//                            break;
//                        case "vaccDate":
//                            currentCertificate.setVaccDate(text);
//                            break;
//                        case "recDate":
//                            currentCertificate.setRecDate(text);
//                            break;
//                        case "testType":
//                            currentCertificate.setTestType(text);
//                            break;
//                        case "testDate":
//                            currentCertificate.setTestDate(text);
//                            break;
                    }
                    break;
            }
            eventType = parser.next();
        }
//        MainActivity main = new MainActivity();
//        main.printCertificates(certificates);
        return currentCertificate;
    }


}
