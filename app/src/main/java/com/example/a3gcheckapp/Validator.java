package com.example.a3gcheckapp;

import android.content.res.AssetManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

public class Validator {

    public static boolean isValid(String qrString) throws FileNotFoundException, CertificateException {
        boolean state = false;
        X509Certificate QRCertificate = createCertificate(qrString);

        X509Certificate ClientCert = loadCertificate("/data/data/com.example.a3gcheckapp/files/Certificates/caCert.crt");


        if(validateCertificate(QRCertificate) && verifyCertificate(ClientCert, QRCertificate)) {
           return true;
        } else return false;

    }

    public static X509Certificate loadCertificate(String path) throws FileNotFoundException, CertificateException {
        FileInputStream fInput = new FileInputStream(path);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) f.generateCertificate(fInput);

        return cert;
    }


    public static boolean validateSignatur(){


        return true;
    }

    public static X509Certificate createCertificate(String qrString) throws FileNotFoundException, CertificateException {
        InputStream stream = new ByteArrayInputStream(qrString.getBytes(StandardCharsets.UTF_8));
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) f.generateCertificate(stream);

        return cert;
    }



    /**
     * Checks whether the given certificate is currently valid.
     * @param certToValidate The certificate to validate.
     * @return Returns true, if the certificate is valid; false if it isn't.
     */

    public static boolean validateCertificate(X509Certificate certToValidate) {

        try {
            certToValidate.checkValidity();
            return true;
        } catch(CertificateExpiredException | CertificateNotYetValidException e) {
            return false;
        }
    }



    /**
     * Verifies a certificate.
     * @param trustedCert Trusted certificate (CA cert) with which you want to verify.
     * @param certToVerify Certificate that is to be verified.
     * @return Returns true, if the verification succeeds; returns false if it doesn't.
     */
    public static boolean verifyCertificate(X509Certificate trustedCert, X509Certificate certToVerify) {

        PublicKey trustedPublicKey = trustedCert.getPublicKey();

        try {

            certToVerify.verify(trustedPublicKey);

            return true;

        } catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException e) {
            return false;
        }
    }


}
