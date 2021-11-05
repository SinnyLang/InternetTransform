package com.main.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAMsgProvider {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAMsgProvider(String publicKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(
                new X509EncodedKeySpec(Base64.getMimeDecoder().decode(publicKey))
        );
        this.privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(
                new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(privateKey))
        );
    }

    public byte[] decode(byte[] decodeMsg){
        return this.decode(decodeMsg, this.privateKey);
    }

    public byte[] encode(byte[] encodeMsg){
        return this.encode(encodeMsg, this.publicKey);
    }

    public byte[] sign(byte[] signMsg){
        return this.encode(signMsg, privateKey);
    }

    public byte[] verify(byte[] veryMsg){
        return this.decode(veryMsg, publicKey);
    }

    public byte[] encode(byte[] signMsg, Key key){
        byte[] endMsg = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE , key);
            endMsg = cipher.doFinal(signMsg);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return endMsg;
    }

    public byte[] decode(byte[] encodeMsg, Key key){
        byte[] endMsg = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            endMsg = cipher.doFinal(encodeMsg);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return endMsg;
    }


}
