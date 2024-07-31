package com.example.mastg_test0015;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {
    public static byte[] keyBytes ;
    static{
        Crypt.keyBytes= new byte[] {7, 3, 4, 5, 6, 7, 8, 9, 16, 17, 18, 9, 20, 21, 15, 1};

    }
    public Crypt() {
    }

    public String encryptData(String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(Crypt.keyBytes, "AES"); // Hardcoded key
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return new String(Base64.encode(cipher.doFinal(data.getBytes()),0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String decryptData(byte[] encryptedData) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(Crypt.keyBytes, "AES"); // Hardcoded key
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(Base64.decode(encryptedData,0)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
