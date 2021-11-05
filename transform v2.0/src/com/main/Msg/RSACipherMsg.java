package com.main.Msg;

import com.main.Util;

import java.util.Arrays;
import java.util.Date;

public class RSACipherMsg implements Msg {
    private byte[] random;
    private byte[] time;
    private byte[] key;

    public void setKey(byte[] key) {
        this.key = new byte[16];
        System.arraycopy(key, 0, this.key, 0 ,16);
    }

    public void setRandom(long random) {
        this.random = Util.Long2Bytes(random);
    }

    public void setTime(Date time) {
        this.time = Util.Long2Bytes(time.getTime());
    }

    public byte[] getKey() {
        byte[] bytes = new byte[16];
        System.arraycopy(key, 0, bytes, 0, 16);
        return bytes;
    }

    public long getRandom() {
        return Util.Bytes2Long(random);
    }

    public Date getTime() {
        return new Date(Util.Bytes2Long(time));
    }

    @Override
    public byte[] getStream() throws MSGLengthOutOfDefault {
        byte[] bytes = new byte[32];
        System.arraycopy(random, 0, bytes, 0, 8);
        System.arraycopy(time, 0, bytes, 8, 8);
        System.arraycopy(key, 0, bytes, 16, 16);
        return bytes;
    }

    public RSACipherMsg(byte[] stream) {
        random = new byte[8];
        time = new byte[8];
        key = new byte[16];
        System.arraycopy(stream, 0, random, 0, 8);
        System.arraycopy(stream, 8, time, 0, 8);
        System.arraycopy(stream, 16, key, 0, 16);
    }

    public RSACipherMsg() {
    }

    @Override
    public String toString() {
        return "RSACipherMsg{" +
                "random=" + Arrays.toString(random) +
                ", time=" + Arrays.toString(time) +
                ", key=" + Arrays.toString(key) +
                '}'+"\n"+
                "RSACipherMsg{"+
                "random=" + getRandom() +
                ", time=" + getTime() +
                ", key=" + Arrays.toString(getKey()) +
                '}';
    }
}
