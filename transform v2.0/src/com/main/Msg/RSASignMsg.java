package com.main.Msg;

import com.main.Util;

import java.util.Arrays;
import java.util.Date;

public class RSASignMsg implements Msg {

    private byte[] random;
    private byte[] time;
    private byte[] ID;

    public void setID(long ID) {
        this.ID = Util.Long2Bytes(ID);
    }

    public void setRandom(long random) {
        this.random = Util.Long2Bytes(random);
    }

    public void setTime(Date time) {
        this.time = Util.Long2Bytes(time.getTime());
    }

    public long getID() {
        return Util.Bytes2Long(ID);
    }

    public long getRandom() {
        return Util.Bytes2Long(random);
    }

    public Date getTime() {
        return new Date(Util.Bytes2Long(time));
    }

    @Override
    public byte[] getStream() throws MSGLengthOutOfDefault {
        byte[] bytes = new byte[24];
        System.arraycopy(random, 0, bytes, 0, 8);
        System.arraycopy(time, 0, bytes, 8, 8);
        System.arraycopy(ID, 0, bytes, 16, 8);
        return bytes;
    }

    public RSASignMsg(byte[] stream) {
        random = new byte[8];
        time = new byte[8];
        ID = new byte[8];
        System.arraycopy(stream, 0, random, 0, 8);
        System.arraycopy(stream, 8, time, 0, 8);
        System.arraycopy(stream, 16, ID, 0, 8);
    }

    public RSASignMsg(){}

    @Override
    public String toString() {
        return "RSASignMsg{" +
                "random=" + Arrays.toString(random) +
                ", time=" + Arrays.toString(time) +
                ", ID=" + Arrays.toString(ID) +
                "}\n" +
                "RSASignMsg{" +
                "random=" + getRandom() +
                ", time=" + getTime() +
                ", ID=" + getID() +
                '}';
    }
}
