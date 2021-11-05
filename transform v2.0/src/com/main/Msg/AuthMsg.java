package com.main.Msg;

import com.main.Util;

public class AuthMsg extends PlaintextMsg {

    public void setID(long ID){
        byte[] id = Util.Long2Bytes(ID);
        System.arraycopy(id, 0, stream, 0, 8);
    }
    public void setFistPart(byte[] bytes){
        System.arraycopy(bytes, 0, stream, 8, 128);
    }
    public void setSecondPart(byte[] bytes){
        System.arraycopy(bytes, 0, stream, 136, 128);
    }

    public long getID(){
        byte[] id = new byte[8];
        System.arraycopy(stream, 0, id, 0, 8);
        return Util.Bytes2Long(id);
    }
    public byte[] getFirstPart(){
        byte[] bytes = new byte[128];
        System.arraycopy(stream, 8, bytes, 0, 128);
        return bytes;
    }
    public byte[] getSecondPart(){
        byte[] bytes = new byte[128];
        System.arraycopy(stream, 136, bytes, 0, 128);
        return bytes;
    }

    @Override
    public byte[] getStream() {
        byte[] bytes = new byte[stream.length + 8];
        System.arraycopy(len, 0, bytes, 0, 8);
        System.arraycopy(stream, 0, bytes, 8, 128+128+8);
        return bytes;
    }

    public AuthMsg(byte[] stream){
        // 解密消息
        this.stream = new byte[128+128+8];
        len = new byte[8];
        System.arraycopy(stream, 0, len, 0, 8);
        System.arraycopy(stream, 8, this.stream, 0, 128+128+8);
    }

    public AuthMsg() {
        len = Util.Long2Bytes(8+128+128);
        stream = new byte[8+128+128];
    }
}
