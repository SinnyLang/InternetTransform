package com.test;

import com.main.Msg.MSGLengthOutOfDefault;
import com.main.Msg.RSASignMsg;
import com.main.security.RSAMsgProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;

import static org.junit.Assert.*;

public class RSAMsgProviderTest {
    private static int KEY_SIZE = 1024;
    private static String publicKey;
    private static String privateKey;
    private static long time = new Date().getTime();
    @BeforeClass
    public static void before() throws NoSuchAlgorithmException {
        getKeyPair();
    }

    private static void getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        RSAMsgProviderTest.privateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        RSAMsgProviderTest.publicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        System.out.println("公钥："+publicKeyString);
        System.out.println("私钥："+privateKeyString);
    }

    @Test
    public void sign() {

        RSASignMsg signMsg = new RSASignMsg();
        signMsg.setID(1234567890L);
        signMsg.setRandom(9876543210L);
        signMsg.setTime(new Date(time));
        System.out.println();
        System.out.println(signMsg);
        byte[] sign = new byte[0];
        RSAMsgProvider provider = null;
        try {
            provider = new RSAMsgProvider(publicKey, privateKey);
            System.out.println("签名的消息长度 "+signMsg.getStream().length);
            sign = provider.sign(signMsg.getStream());
        } catch (MSGLengthOutOfDefault msgLengthOutOfDefault) {
            msgLengthOutOfDefault.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        System.out.println("验证签名");
        RSASignMsg verifyMsg = new RSASignMsg(provider.verify(sign));
        System.out.println(verifyMsg);
    }

    @Test
    public void cipher() {
    }
}