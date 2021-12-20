package com.example.a3gcheckapp;

import junit.framework.TestCase;

import org.junit.Assert;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class QRCodeHandlerTest extends TestCase {

    public void testParseCertificateXMLToCertificate() throws ParserConfigurationException, IOException, SAXException {
        Certificate testCertificate = QRCodeHandler.parseCertificateXMLToCertificate("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><certificate type=\"recovered\"><firstName>asd</firstName><lastName>asd</lastName><birthdate>01.12.2021</birthdate><issueDate>15.12.2021 11:16</issueDate><recoveryTestDate>02.12.2021 04:00</recoveryTestDate></certificate>");

        Assert.assertEquals("asd", testCertificate.getFirstName());
        Assert.assertNotEquals("Bernd", testCertificate.getFirstName());
    }

    public void testParseQRdataToStringMap() throws ParserConfigurationException, IOException, SAXException {
        Map<String, String> testMap = QRCodeHandler.parseQRdataToStringMap("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><qrCode><certificate>&lt;?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?&gt;&lt;certificate type=\"recovered\"&gt;&lt;firstName&gt;asd&lt;/firstName&gt;&lt;lastName&gt;asd&lt;/lastName&gt;&lt;birthdate&gt;01.12.2021&lt;/birthdate&gt;&lt;issueDate&gt;15.12.2021 11:16&lt;/issueDate&gt;&lt;recoveryTestDate&gt;02.12.2021 04:00&lt;/recoveryTestDate&gt;&lt;/certificate&gt;</certificate><signature>DirqPElx8tahYahAJTeWD0PEs/4M7hmmrVgQfU6OtuE6NidnL3Tm/fFQf/OKWcEIJ1LshbIk5+XZ4D27rNhT7gzyzZyaONE6KYUnA7z86JC/wZoSCRwBYBGyZl1OtkTszmm7j9Nf9d3w6d0r0RtRfB3e93UYo/3iDEWW7e1yZtVoNZgz+2UHxnZPqPDB1tQ7N3bXd3NvcPwUPsiokqTFFfDdyHpeN0vDwKrAJfhreDfnIwod7HzGQ2kPNzPCiX2awvx0Ce/nCw+FaeU7BWpzYj40YRaaiKbQNHeOuqQSgoUaNKlNzzUxpxlq80hrJPnMhUuv5PVfSTQHnuRMXrxE/w==</signature><x509>-----BEGIN CERTIFICATE----- MIIDejCCAmKgAwIBAgIGAX21VctBMA0GCSqGSIb3DQEBCwUAMH8xCzAJBgNVBAYT AkRFMRgwFgYDVQQIDA9SaGVpbmxhbmQtUGZhbHoxHjAcBgNVBAcMFUx1ZHdpZ3No YWZlbiBhbSBSaGVpbjEMMAoGA1UECgwDSFdHMRMwEQYDVQQLDApTRSBQcm9qZWt0 MRMwEQYDVQQDDAozR0NoZWNrQXBwMB4XDTIxMTIxMzE5NDcxOFoXDTI2MTIxMzE5 NDcxOFowfTEQMA4GA1UEAwwHTmljb2xhczENMAsGA1UECwwEQXJ6dDENMAsGA1UE CgwEQXJ6dDEkMCIGA1UEBwwbTmV1c3RhZHQgYW4gZGVyIFdlaW5zdHJhc3NlMRgw FgYDVQQIDA9SaGVpbmxhbmQtUGZhbHoxCzAJBgNVBAYTAkRFMIIBIjANBgkqhkiG 9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh9k7Iqa2n/OpU3iONLt5+wwXab7dbse6fhBq rpscyowgoHpzLwqSEY/dJNE+w9+rWMOgXVI81PwHB7W9W9x/diPkGh5dY92cvF10 wp7CqlYVepyBpS4IEPPQYe7YP9SUPN5aqOwJnP9YEDnfUkWz2CXX0v7pO3UXy57s yp7rl3jxbbfXmtxuNvKo5UtYXlDnQbVwpvL/YE1hqA4UosGouq+uKii93pD8v0EY Aq30B8zSd6/qo1KoeMg7azxwud4V6DQscIdjbVjp80ARMD87iJrZY8GoLNx8zZC8 K0Hkbdxrjuq/hu5TkSuiGQf3zE+YO8nGkqV0AohA8kDP61NCcQIDAQABMA0GCSqG SIb3DQEBCwUAA4IBAQB5UVkAn6rREtt1ia3tqX/hSCDZmYpecUwuGXT6mhdgFaif qXodtqDHfAGNW4bJaXUzOcudi0T+HY2P4U7NDIeOki2SjTG9k/ehizdyPyM0ohqm Er8A3fFdmcMrbS16ChJM00ZiowZXVjq4KycwKksvzysUZUJIPPwZyeDwUPDicTcQ BXU8waGLYi5aQySEPhZYj7mjrTQ+b9qSAgU6hGtpVs2hfITMQapoZChIiIcg7LRG nKPCZNoFDWFsMwuFDHmdl06cQGEHXXAMDQF60q7U77Fa7G77WkVJ8q6lou9ods2y uWAyfkZUReG0m1CHP3qI/NsaUklgUBg3mBk+ZhG9 -----END CERTIFICATE----- </x509></qrCode>");

        Assert.assertTrue(testMap.size() == 3);
    }
}