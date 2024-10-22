package com.example.doctorcare.security.jwt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.hibernate.annotations.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.doctorcare.utils.Const;

import io.jsonwebtoken.io.IOException;

@Component
public class KeyManger {
    private static PublicKey cachedPublicKey;
    private static PrivateKey cachedPrivateKey;

    private static final Logger logger = LoggerFactory.getLogger(KeyManger.class);

    public synchronized static PublicKey getPublicKey() throws KeyStoreException, IOException,
            UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, java.io.IOException {
        if (cachedPublicKey == null) {
            KeyStore keyStore = KeyStore.getInstance(Const.KEY.KEY_STORE_CERTIFICATES);
            keyStore.load(new FileInputStream(Const.KEY.FILE_KEYTOOL_NAME), Const.KEY.KEYTOOL_PASSWORD.toCharArray());
            Certificate cert = keyStore.getCertificate(Const.KEY.KEYTOOL_ALIAS);
            cachedPublicKey = cert.getPublicKey();
        }
        return cachedPublicKey;
    }

    /*
     * Open terminal and create keytool : keytool -list -rfc -keystore
     * mykeystore.jks -alias mykey -storepass 123456 -file public_key.pem
     * 
     * 
     * Next create public_key.pem : keytool -export -alias mykeystore -keystore
     * mykeystore.jks -file public_key.pem
     * 
     * mykeystore.jdk is a name of file. and 123456 is a password.
     */
    public PrivateKey getPrivateKey() throws KeyStoreException, IOException,
            UnrecoverableKeyException,
            NoSuchAlgorithmException, CertificateException,
            FileNotFoundException, java.io.IOException {
        KeyStore keyStore = KeyStore.getInstance(Const.KEY.KEY_STORE_CERTIFICATES);
        keyStore.load(new FileInputStream(Const.KEY.FILE_KEYTOOL_NAME),
                Const.KEY.KEYTOOL_PASSWORD.toCharArray());

        cachedPrivateKey = (PrivateKey) keyStore.getKey(Const.KEY.KEYTOOL_ALIAS,
                Const.KEY.KEYTOOL_PASSWORD.toCharArray());

        Certificate cert = keyStore.getCertificate(Const.KEY.KEYTOOL_ALIAS);
        PublicKey publicKey = cert.getPublicKey(); //
        logger.info("The method getPrivateKey was running and the public is :" + publicKey.toString());

        return cachedPrivateKey;
    }
}
