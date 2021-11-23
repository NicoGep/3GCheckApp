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
    public void parseXML(String xml){
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            //InputStream is = getAssets().open("data.xml");
            //String xml = "<nachweis>    <lastname>Hoeckh</lastname>    <forname>Celine</forname>    <erstelldatum>01-02-2000</erstelldatum> <lastname>Hoeckh</lastname>    <forname>Celine</forname>    <erstelldatum>01-02-2000</erstelldatum> </nachweis>";
            InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is,null);
            processParsing(parser);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<Certificate> certificates = new ArrayList<>();
        int eventType = parser.getEventType();
        Certificate currentCertificate = null;

        while(eventType!= XmlPullParser.END_DOCUMENT) {
            String var = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    var = parser.getName();

                    if("nachweis".equals(var)){
                        currentCertificate = new Certificate();
                        certificates.add(currentCertificate);
                    } else if (currentCertificate != null) {
                        if ("lastname".equals(var)){
                            currentCertificate.setLastname(parser.nextText());
                        } else if ("forname".equals(var)){
                            currentCertificate.setForname(parser.nextText());
                        } else if ("erstelldatum".equals(var)) {
                            currentCertificate.setErstelldatum(parser.nextText());
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }
        MainActivity main = new MainActivity();
        main.printCertificates(certificates);
    }


}
