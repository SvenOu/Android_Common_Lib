package com.sv.common.web.client;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class SSLSocketFactoryEx {
    private static final String TAG = SSLSocketFactoryEx.class.getSimpleName();
    private SSLContext sslContext;
    private boolean isAcceptAllCertificate = true;

    public SSLSocketFactoryEx() {
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (isAcceptAllCertificate) {
            iniSSLContextAcceptAll();
        } else {
            iniSSLContextWithCertificates();
        }
    }
    private void iniSSLContextAcceptAll() {
        TrustManager tm = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {

            }
        };
        try {
            sslContext.init(null, new TrustManager[]{tm}, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private void iniSSLContextWithCertificates() {
        try {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // From https://www.washington.edu/itconnect/security/ca/load-der.crt
            //TODO: use my certificate
            InputStream caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                Log.i(TAG, "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }
            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            //TODO: use my certificate password
            keyStore.load(null, null);
            //TODO: use my certificate alias name
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            // Create an SSLContext that uses our TrustManager
            sslContext.init(null, tmf.getTrustManagers(), null);

        } catch (CertificateException | IOException | KeyStoreException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        assert sslContext != null;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }
}
