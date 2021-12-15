package com.example.a3gcheckapp;

import android.content.res.AssetManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class Validator {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isValid(String x509, String certificate, String signature) throws FileNotFoundException, CertificateException, UnsupportedEncodingException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        boolean state = false;
        X509Certificate QRCertificate = createCertificate(x509);

        X509Certificate caCert = loadCertificate("/data/data/com.example.a3gcheckapp/files/Certificates/caCert.crt");


        if(validateCertificate(QRCertificate) && verifyCertificate(caCert, QRCertificate) && validateSignature(certificate, signature, QRCertificate)) {
           return true;
        } else return false;

    }

    public static X509Certificate loadCertificate(String path) throws FileNotFoundException, CertificateException {
        FileInputStream fInput = new FileInputStream(path);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) f.generateCertificate(fInput);

        return cert;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean validateSignature(String data, String sigToValidate, X509Certificate certForValidation) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(certForValidation);
        sig.update(data.getBytes("UTF-8"));
        byte[] untrustedSignatureBytes = Base64.getDecoder().decode(sigToValidate);
        return sig.verify(untrustedSignatureBytes);
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
