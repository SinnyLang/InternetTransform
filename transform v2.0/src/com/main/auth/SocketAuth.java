package com.main.auth;

import com.main.Msg.AuthMsg;
import com.main.Msg.MSGLengthOutOfDefault;
import com.main.Msg.RSACipherMsg;
import com.main.Msg.RSASignMsg;
import com.main.SocketWrapper;
import com.main.Util;
import com.main.security.RSAMsgProvider;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Random;

public class SocketAuth {
    private SocketWrapper socketWrapper;
    public static long id = 0x10001; // Util.loadLocalId();
    public static long serverId = 0xf0000; // Util.loadServerId();
    private String clientPU ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTIfYicNoY9VMlYMLCuoDDpZZY\n" +
            "NmVgz5yFr6ANMkO0Uy1B+DOivpPQnUPQj3ral1AMV6Fxy/MK3Ngln31+FNslUImY\n" +
            "yoobj4UfjJA8mL1yG6LEHKjL+JniA8emp5+HGoAym1uLEPpsIcJGTPyyp9KXLxj9\n" +
            "Jl8GbXB78koB6oi3rwIDAQAB";// Util.loadLocalPU();
    private String clientPR = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJMh9iJw2hj1UyVg\n" +
            "wsK6gMOlllg2ZWDPnIWvoA0yQ7RTLUH4M6K+k9CdQ9CPetqXUAxXoXHL8wrc2CWf\n" +
            "fX4U2yVQiZjKihuPhR+MkDyYvXIbosQcqMv4meIDx6ann4cagDKbW4sQ+mwhwkZM\n" +
            "/LKn0pcvGP0mXwZtcHvySgHqiLevAgMBAAECgYBYM1iwpsQniBEy7AF06Ia+QiqR\n" +
            "cPJvCaYEAhdJMeOiWuZbkKWrnuFPgqcfhJOaLHEBJRsT87Ybwe4VB7ob9fa0TBWE\n" +
            "RzYEjbuGbCOCkvpzcSY55Qi5SqUFaoVLhQiHG3wTTi2GwMP/c1hsRNP4nxIKFT3P\n" +
            "AwSxNf9E3a6avxlbGQJBAMMWcWTuybTM8itdG7mQb+WUE51JnC2hSFSqGW74Tlh/\n" +
            "vVK2PDgBziANWRnhNnkm3Jgu9rc/x+/Cb6jUfLX9ebUCQQDBEm0BEtjWisWPX6Rg\n" +
            "psycplpBLXq4PDEkVG/Z0sgAlOq+BAb3StzW9IHU1i7SgjNP+TnE+Wjra/f2oRP4\n" +
            "MnpTAkEAtVH2ssj90H1r18ICMsZ8OfUXBemUrxRoFcjDOtCugLK5VFbsQWHsIv5F\n" +
            "AjjAWHo2LXv48vjk64LcJR7Zioy8wQJAbZCOSUivwvhXaqiE+NBPVypUF56+gKSv\n" +
            "4oUEQGGMXEAmApT4eweB0X5J20V8prt6cQzmyrAa6VaJDe2VDolevwJBAJfVxkE5\n" +
            "jtCwq24WSITT7AqbMy8oUExhUabMeZ/LiK4Brv8sBOLEZkzhe/BsFw6MSl7Seo4Z\n" +
            "6GNXo4btO4XR2Qg="; // Util.loadLocalPR();
    private String serverPU = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQ0IV6Wd/RINpGlnFT/BW766N3\n" +
            "dn7tMOp6ZzsHRVk3uoSiEXknyVOJhEQ+GUe7EafdtxvSnmULTcLhK08sRXVTaOKX\n" +
            "pBj/jDyekKooWYC+mofXP//UPpNANUfbKHlJa3s79iX0NdLmfY7a5F9p8z60V7dC\n" +
            "63yX0UKj1qLsAHfHKwIDAQAB";//Util.loadRemotePU(serverId);
    private RSAMsgProvider provider;
    private AuthMsg authMsg;
    private byte[] key; // 对称密钥

    public SocketAuth(SocketWrapper s){
        socketWrapper = s;
        authMsg = new AuthMsg();
        try {
            provider = new RSAMsgProvider(serverPU, clientPR);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public boolean isPassAuthentication(){
        Date time = new Date();
        long random = (long) (Math.random()*Long.MAX_VALUE);
        authMsg.setID(id);

        // 设置消息的第一部分
        RSASignMsg signMsg = new RSASignMsg();
        signMsg.setTime(time);
        signMsg.setRandom(random);
        signMsg.setID(serverId);

        // 设置消息的第二部分
        RSACipherMsg cipherMsg = new RSACipherMsg();
        cipherMsg.setRandom(random);
        cipherMsg.setTime(time);
        // 生成16字节对称密钥
        genKey();

        cipherMsg.setKey(key);

        try {
            // 装填两部分消息
            authMsg.setFistPart(provider.sign(signMsg.getStream()));
            authMsg.setSecondPart(provider.encode(cipherMsg.getStream()));
            System.out.println("客户端ID+"+id);
//            System.out.println(signMsg);
//            System.out.println(cipherMsg);


            // 发送验证消息
            socketWrapper.sendAuthMsg(authMsg);
        } catch (MSGLengthOutOfDefault msgLengthOutOfDefault) {
            msgLengthOutOfDefault.printStackTrace();
        }

        // 大于零则表示认证成功
        return socketWrapper.readByte() > 0;
    }

    private void genKey() {
        Random r = new Random();
        key = new byte[16];
        r.nextBytes(key);
    }

    public byte[] getKey() {
        return key;
    }
}
