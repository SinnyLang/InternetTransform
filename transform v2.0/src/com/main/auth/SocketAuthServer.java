package com.main.auth;

import com.main.InitData;
import com.main.Msg.AuthMsg;
import com.main.Msg.CipherMsg;
import com.main.Msg.RSACipherMsg;
import com.main.Msg.RSASignMsg;
import com.main.SocketWrapper;
import com.main.Util;
import com.main.security.RSAMsgProvider;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

public class SocketAuthServer {
    private SocketWrapper socketWrapper;
    private String localPR = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANDQhXpZ39Eg2kaW\n" +
            "cVP8Fbvro3d2fu0w6npnOwdFWTe6hKIReSfJU4mERD4ZR7sRp923G9KeZQtNwuEr\n" +
            "TyxFdVNo4pekGP+MPJ6QqihZgL6ah9c//9Q+k0A1R9soeUlrezv2JfQ10uZ9jtrk\n" +
            "X2nzPrRXt0LrfJfRQqPWouwAd8crAgMBAAECgYEAwB5qAFT6OMXaLcGyyQuQz5IQ\n" +
            "lxQd2Yr84Ntjkt/6IuznpNkguDtiMY7Q3rWRqoqnALW80n5LByhnWZXoRRouw4El\n" +
            "5hUlAsMyqa80vAXHhY/xQkQ12zt8Bw/mUuDvxXg/pzFzMLeMS0IJDXZsYO9ovMUY\n" +
            "TKduGp3xWMDLBDxgMTECQQD73bVk59TnmY5z5O+8lZJPxF/P7zY9zAY73k4vvHqb\n" +
            "Znmmb3xP6TP3irNaiNvTxeesLCUY0E+w3X4ZOqqLRBxZAkEA1D3rOv84nERY4zxd\n" +
            "qZ6Lyq+NtnLjr3onIM1D1KjTe6JzlXaC23OYz3kg5W4hwgJvKiK8/UIEZCfljsp9\n" +
            "zt4/IwJAGT3W+A3j4HIbPLIs9JFo4GWl1Ij8vqS72zQkUdRp1Tkn5+40Xy3X728P\n" +
            "GnuszESBO1gRP7NgyTDpek3O2bYxwQJBAI4vwSQwETvXKflp2pGkzqAFxhmBlHld\n" +
            "i1MVUKjqRWvhZC+wSnA8XBY6Xsh1ZJY1zUVcKmI8VPbIJ6jmjSwGhiMCQH4woz1I\n" +
            "WjUeMdmb6qbAhblqbPMK34WHFErGAFTZRmMLHSlzrDOSzQ6vAHmxH1bU9uYmSWaC\n" +
            "Oh0UnQPqniNY//k="; // Util.loadLocalPR();
    private long localID = 0xf0000; // Util.loadLocalId();
    private byte[] key;

    public byte[] getKey() {
        return key;
    }

    public SocketAuthServer(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    public boolean isPassAuthentication(){
        AuthMsg authMsg = socketWrapper.readAuthMsg();
        long remoteID = authMsg.getID();
        String remotePU = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTIfYicNoY9VMlYMLCuoDDpZZY\n" +
                "NmVgz5yFr6ANMkO0Uy1B+DOivpPQnUPQj3ral1AMV6Fxy/MK3Ngln31+FNslUImY\n" +
                "yoobj4UfjJA8mL1yG6LEHKjL+JniA8emp5+HGoAym1uLEPpsIcJGTPyyp9KXLxj9\n" +
                "Jl8GbXB78koB6oi3rwIDAQAB";// Util.loadRemotePU(remoteID);
        RSASignMsg signMsg;
        RSACipherMsg cipherMsg;
        try {
            // ?????????????????????????????????
            RSAMsgProvider provider = new RSAMsgProvider(remotePU, localPR);
            byte[] signByte = provider.verify(authMsg.getFirstPart());
            signMsg = new RSASignMsg(signByte);
            // ???????????????????????????
            byte[] cipherByte = provider.decode(authMsg.getSecondPart());
            cipherMsg = new RSACipherMsg(cipherByte);

            System.out.println("?????????ID " + localID);
            System.out.println("remoteId " + remoteID);
//            System.out.println(signMsg);
//            System.out.println(cipherMsg);

            // ????????????remoteID???????????????????????????????????????????????????
            if (localID == signMsg.getID() &&
                    signMsg.getTime().getTime() == cipherMsg.getTime().getTime()){
//                System.out.println("??????????????????");
                long T = System.currentTimeMillis() - cipherMsg.getTime().getTime();
                System.out.println("??????????????? "+new Date());
                if ( T <= InitData.DEFAULT_AUTH_TIMEOUT){
                    // ????????????????????????????????????????????????remoteID????????????
//                    System.out.println("??????????????????");
                    key = cipherMsg.getKey();
                    socketWrapper.writeByte((byte) 1);
                    return true;
                }
            }

            // ??????remoteID????????????
            socketWrapper.writeByte((byte) -1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
    }
}
