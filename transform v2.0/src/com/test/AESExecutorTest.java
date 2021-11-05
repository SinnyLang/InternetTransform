package com.test;

import com.main.security.AESExecutor;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

import static org.junit.Assert.*;

public class AESExecutorTest {
    private AESExecutor aesExecutor = new AESExecutor(AESExecutor.AES);
    private String key = "0123456789abcdef";
    private String m = "asd啊手动阀。，,ds手动 ss";
    private static byte[] c = null;
    @Test
    public void encrypt() {
        byte[] bytes = aesExecutor.encrypt(m.getBytes(), key.getBytes());
        System.out.println("加密后的数据长度："+bytes.length);
        c = bytes;
        System.out.println(new String(c));
    }

    @Test
    public void decrypt() throws InterruptedException {
        String mingWen = null;
        Thread.sleep(2000);
        System.out.println("密文是："+new String(c));
        mingWen = new String(aesExecutor.decrypt(c, key.getBytes()));
        System.out.println(mingWen);
    }
}