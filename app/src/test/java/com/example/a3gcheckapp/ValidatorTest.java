package com.example.a3gcheckapp;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

public class ValidatorTest extends TestCase {

    public void testIsValid() {
    }

    public void testValidateSignatur() {

    }

    public void testValidateCertificate() throws CertificateException, FileNotFoundException {
        X509Certificate qrCert = Validator.createCertificate("-----BEGIN CERTIFICATE-----\n" +
                "MIIDejCCAmKgAwIBAgIGAX21VctBMA0GCSqGSIb3DQEBCwUAMH8xCzAJBgNVBAYT\n" +
                "AkRFMRgwFgYDVQQIDA9SaGVpbmxhbmQtUGZhbHoxHjAcBgNVBAcMFUx1ZHdpZ3No\n" +
                "YWZlbiBhbSBSaGVpbjEMMAoGA1UECgwDSFdHMRMwEQYDVQQLDApTRSBQcm9qZWt0\n" +
                "MRMwEQYDVQQDDAozR0NoZWNrQXBwMB4XDTIxMTIxMzE5NDcxOFoXDTI2MTIxMzE5\n" +
                "NDcxOFowfTEQMA4GA1UEAwwHTmljb2xhczENMAsGA1UECwwEQXJ6dDENMAsGA1UE\n" +
                "CgwEQXJ6dDEkMCIGA1UEBwwbTmV1c3RhZHQgYW4gZGVyIFdlaW5zdHJhc3NlMRgw\n" +
                "FgYDVQQIDA9SaGVpbmxhbmQtUGZhbHoxCzAJBgNVBAYTAkRFMIIBIjANBgkqhkiG\n" +
                "9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh9k7Iqa2n/OpU3iONLt5+wwXab7dbse6fhBq\n" +
                "rpscyowgoHpzLwqSEY/dJNE+w9+rWMOgXVI81PwHB7W9W9x/diPkGh5dY92cvF10\n" +
                "wp7CqlYVepyBpS4IEPPQYe7YP9SUPN5aqOwJnP9YEDnfUkWz2CXX0v7pO3UXy57s\n" +
                "yp7rl3jxbbfXmtxuNvKo5UtYXlDnQbVwpvL/YE1hqA4UosGouq+uKii93pD8v0EY\n" +
                "Aq30B8zSd6/qo1KoeMg7azxwud4V6DQscIdjbVjp80ARMD87iJrZY8GoLNx8zZC8\n" +
                "K0Hkbdxrjuq/hu5TkSuiGQf3zE+YO8nGkqV0AohA8kDP61NCcQIDAQABMA0GCSqG\n" +
                "SIb3DQEBCwUAA4IBAQB5UVkAn6rREtt1ia3tqX/hSCDZmYpecUwuGXT6mhdgFaif\n" +
                "qXodtqDHfAGNW4bJaXUzOcudi0T+HY2P4U7NDIeOki2SjTG9k/ehizdyPyM0ohqm\n" +
                "Er8A3fFdmcMrbS16ChJM00ZiowZXVjq4KycwKksvzysUZUJIPPwZyeDwUPDicTcQ\n" +
                "BXU8waGLYi5aQySEPhZYj7mjrTQ+b9qSAgU6hGtpVs2hfITMQapoZChIiIcg7LRG\n" +
                "nKPCZNoFDWFsMwuFDHmdl06cQGEHXXAMDQF60q7U77Fa7G77WkVJ8q6lou9ods2y\n" +
                "uWAyfkZUReG0m1CHP3qI/NsaUklgUBg3mBk+ZhG9\n" +
                "-----END CERTIFICATE-----\n");

        Assert.assertTrue(Validator.validateCertificate(qrCert));
    }

    public void testVerifyCertificate() throws FileNotFoundException, CertificateException {
        X509Certificate qrCert = Validator.createCertificate("-----BEGIN CERTIFICATE-----\n" +
                "MIIDejCCAmKgAwIBAgIGAX21VctBMA0GCSqGSIb3DQEBCwUAMH8xCzAJBgNVBAYT\n" +
                "AkRFMRgwFgYDVQQIDA9SaGVpbmxhbmQtUGZhbHoxHjAcBgNVBAcMFUx1ZHdpZ3No\n" +
                "YWZlbiBhbSBSaGVpbjEMMAoGA1UECgwDSFdHMRMwEQYDVQQLDApTRSBQcm9qZWt0\n" +
                "MRMwEQYDVQQDDAozR0NoZWNrQXBwMB4XDTIxMTIxMzE5NDcxOFoXDTI2MTIxMzE5\n" +
                "NDcxOFowfTEQMA4GA1UEAwwHTmljb2xhczENMAsGA1UECwwEQXJ6dDENMAsGA1UE\n" +
                "CgwEQXJ6dDEkMCIGA1UEBwwbTmV1c3RhZHQgYW4gZGVyIFdlaW5zdHJhc3NlMRgw\n" +
                "FgYDVQQIDA9SaGVpbmxhbmQtUGZhbHoxCzAJBgNVBAYTAkRFMIIBIjANBgkqhkiG\n" +
                "9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh9k7Iqa2n/OpU3iONLt5+wwXab7dbse6fhBq\n" +
                "rpscyowgoHpzLwqSEY/dJNE+w9+rWMOgXVI81PwHB7W9W9x/diPkGh5dY92cvF10\n" +
                "wp7CqlYVepyBpS4IEPPQYe7YP9SUPN5aqOwJnP9YEDnfUkWz2CXX0v7pO3UXy57s\n" +
                "yp7rl3jxbbfXmtxuNvKo5UtYXlDnQbVwpvL/YE1hqA4UosGouq+uKii93pD8v0EY\n" +
                "Aq30B8zSd6/qo1KoeMg7azxwud4V6DQscIdjbVjp80ARMD87iJrZY8GoLNx8zZC8\n" +
                "K0Hkbdxrjuq/hu5TkSuiGQf3zE+YO8nGkqV0AohA8kDP61NCcQIDAQABMA0GCSqG\n" +
                "SIb3DQEBCwUAA4IBAQB5UVkAn6rREtt1ia3tqX/hSCDZmYpecUwuGXT6mhdgFaif\n" +
                "qXodtqDHfAGNW4bJaXUzOcudi0T+HY2P4U7NDIeOki2SjTG9k/ehizdyPyM0ohqm\n" +
                "Er8A3fFdmcMrbS16ChJM00ZiowZXVjq4KycwKksvzysUZUJIPPwZyeDwUPDicTcQ\n" +
                "BXU8waGLYi5aQySEPhZYj7mjrTQ+b9qSAgU6hGtpVs2hfITMQapoZChIiIcg7LRG\n" +
                "nKPCZNoFDWFsMwuFDHmdl06cQGEHXXAMDQF60q7U77Fa7G77WkVJ8q6lou9ods2y\n" +
                "uWAyfkZUReG0m1CHP3qI/NsaUklgUBg3mBk+ZhG9\n" +
                "-----END CERTIFICATE-----\n");

        X509Certificate caCert = Validator.createCertificate("-----BEGIN CERTIFICATE-----\n" +
                "MIID3zCCAsegAwIBAgIUVJe3nccp1n/8Mk7GF1ntN5EO5QwwDQYJKoZIhvcNAQEL\n" +
                "BQAwfzELMAkGA1UEBhMCREUxGDAWBgNVBAgMD1JoZWlubGFuZC1QZmFsejEeMBwG\n" +
                "A1UEBwwVTHVkd2lnc2hhZmVuIGFtIFJoZWluMQwwCgYDVQQKDANIV0cxEzARBgNV\n" +
                "BAsMClNFIFByb2pla3QxEzARBgNVBAMMCjNHQ2hlY2tBcHAwHhcNMjExMjEzMTcz\n" +
                "NjIyWhcNMjIwMTEyMTczNjIyWjB/MQswCQYDVQQGEwJERTEYMBYGA1UECAwPUmhl\n" +
                "aW5sYW5kLVBmYWx6MR4wHAYDVQQHDBVMdWR3aWdzaGFmZW4gYW0gUmhlaW4xDDAK\n" +
                "BgNVBAoMA0hXRzETMBEGA1UECwwKU0UgUHJvamVrdDETMBEGA1UEAwwKM0dDaGVj\n" +
                "a0FwcDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALSvf70ZxZ4UE3fN\n" +
                "3ScXUt777bgFeBLHtYSv3IMgUaWu26+woazXi9p3/KPxlniH1pVLXOEagW9+XS/2\n" +
                "Vh4UdKRNFEMIAtFyy7DJlHEYqZY+ZKgKywX+6tUIoMvRbQw0DJClMuvWzI+cZund\n" +
                "42pcA5jelJFLlLvl516eEYsNhnJrA7feSEkmUMdeG+PACDTy5rNsDetuAGvqEMHn\n" +
                "5tQVuwHYZT+d9CVcbySxQn56uoSTOZI50Ulxyn4kM+hcqkgmY3RrXTJ58a3O04ay\n" +
                "eNzqQJWi+XVX+rbMWaJO5VAC8k9FZWVjOMDf4irwMv1rsNAADTefdDMh2TGY4lLm\n" +
                "tJgprm0CAwEAAaNTMFEwHQYDVR0OBBYEFGBheaD+aDCCej5U2fhgJaJ1xhzzMB8G\n" +
                "A1UdIwQYMBaAFGBheaD+aDCCej5U2fhgJaJ1xhzzMA8GA1UdEwEB/wQFMAMBAf8w\n" +
                "DQYJKoZIhvcNAQELBQADggEBACRdzaAFEUCWZ7+r0XmJP+BTQWMbuRF12epuGThy\n" +
                "lXBBH1V1oZsHd5E1fDu5llTywCjLphlOE4N7V9ws0PAq8TXDbDRzaPt8Zr7OiQMz\n" +
                "xtFdzbyiUggADcLt6Tca/ESgO+iic9rHBFp2ThdzdDP3+5vGmjiCfHooL51FZA/1\n" +
                "xwyaGEQPO3q9s0lezF6vbu+Ak7EOwCnow2jO6fKTyIZBSGrHDEcu9WvKl3NG4QHu\n" +
                "i0HdNKjppzZSVbkBziS1kyTT+dm3R336BslaIWwqpqgNI1zF8HGRdaAilNca/g6B\n" +
                "xYz9pvWcm6Py9t92ZpwM82RmU5K9TREHdvboi1jjQej2XvE=\n" +
                "-----END CERTIFICATE-----");


        Assert.assertTrue(Validator.verifyCertificate(caCert, qrCert));

    }
}