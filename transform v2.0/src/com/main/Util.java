package com.main;

public class Util {
    public static long Bytes2Long(byte[] bytes){
        long rtn = 0;
        for (int i = 7; i >= 0; i--) {
            rtn <<= 8;
            rtn = rtn | (0xffL & bytes[i]);
        }
        return rtn;
    }

    public static byte[] Long2Bytes(long l){
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (l & 0xff);
            l = l >> 8;
        }
        return bytes;
    }

    public static String loadLocalPU() {
        return null;
    }

    public static String loadLocalPR() {
        return null;
    }

    public static String loadRemotePU(long remoteID) {
        return null;
    }

//    public static long loadRemoteId(long remoteID) {
//        return 0;
//    }

    public static long loadLocalId() {
        return 0xf0000;
    }

    public static long loadServerId() {
        return 0xf0000;
    }
}
