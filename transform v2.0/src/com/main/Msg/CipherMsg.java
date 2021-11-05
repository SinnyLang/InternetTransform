package com.main.Msg;

import com.main.InitData;

import static com.main.Util.Long2Bytes;

public class CipherMsg implements Msg {
    private byte[] cipherMsgBytes;
    private byte[] cTextLength;
    public static long no = (long) (Math.random()*Long.MAX_VALUE);

    public CipherMsg(byte[] cipherMsgBytes) {
        this.cTextLength = Long2Bytes(cipherMsgBytes.length);
        byte[] bytes = new byte[cipherMsgBytes.length];
        System.arraycopy(cipherMsgBytes, 12, bytes, 0, cipherMsgBytes.length);
        this.cipherMsgBytes = bytes;
        no = no + 1;
    }

    public CipherMsg(byte[] cipherMsgBytes, long no) {
        CipherMsg.no = no;
        this.cTextLength = Long2Bytes(cipherMsgBytes.length);
        byte[] bytes = new byte[cipherMsgBytes.length];
        System.arraycopy(cipherMsgBytes, 0, bytes, 0, cipherMsgBytes.length);
        this.cipherMsgBytes = bytes;
    }

    public long getNo() {
        return no;
    }

    public byte[] getCipherMsgBytes(){
        byte[] bytes = new byte[cipherMsgBytes.length];
        System.arraycopy(cipherMsgBytes, 0, bytes, 0, bytes.length);
        return bytes;
    }

    @Override
    public byte[] getStream() throws MSGLengthOutOfDefault {
            // 数据不足2048个则用 0 填充
            // 否则抛出消息长度过长的异常
        if (cipherMsgBytes.length+8+4 > InitData.DEFAULT_MSG_LENTGTH)
            throw new MSGLengthOutOfDefault("密文中MSG消息长度超过默认长度");

        // 默认消息长度是2048个字节
        byte[] stream = new byte[InitData.DEFAULT_MSG_LENTGTH];
        byte[] no = Long2Bytes(CipherMsg.no);
        System.arraycopy(no, 0, stream, 0, 8);
        System.arraycopy(cTextLength, 0, stream, 8, 4);
        System.arraycopy(cipherMsgBytes, 0, stream, 8, cipherMsgBytes.length);
        return stream;
    }
}
