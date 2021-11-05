package com.main.security;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AESExecutor {
    public static final String AES = "AES";

    private String encryptionType;

    public AESExecutor(String encryptionType) {
        this.encryptionType = encryptionType;
    }

    public byte[] encrypt(byte[] m, byte[] key) {
        byte[] bytes = null;
        try {
            Cipher cipher = Cipher.getInstance(encryptionType);
            SecretKey secretKey = new SecretKeySpec(key, encryptionType);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            bytes = cipher.doFinal(m);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public byte[] decrypt(byte[] c, byte[] key)  {
        byte[] bytes = null;
        try {
            Cipher cipher = Cipher.getInstance(encryptionType);
            SecretKey secretKey = new SecretKeySpec(key, encryptionType);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(c);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return bytes;
    }

}
