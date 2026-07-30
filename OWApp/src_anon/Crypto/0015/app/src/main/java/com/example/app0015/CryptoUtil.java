package com.example.app0015;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
public class CryptoUtil {
    // Metodo per generare una chiave statica
    public static SecretKey getStaticKey() {
        String key = "1234567890123456"; // Chiave statica (16 caratteri per AES-128)
        return new SecretKeySpec(key.getBytes(), "AES");
    }

    // Metodo per cifrare i dati
    public static String encrypt(String data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
    }
}