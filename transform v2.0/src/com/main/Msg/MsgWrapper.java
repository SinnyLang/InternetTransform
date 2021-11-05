package com.main.Msg;

import com.main.CharsetByte;
import com.main.InitData;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

import static com.main.Util.Bytes2Long;
import static com.main.Util.Long2Bytes;

public class MsgWrapper implements Msg{
    private byte cmdType;
    private byte charset;
    private byte[] fileNameLen;
    private byte[] date;
    private static long no = (long) (Math.random() * Long.MAX_VALUE);
    private byte[] fileName;

    public MsgWrapper(){
        no = no + 1;
    }

    public MsgWrapper(byte[] stream){
        no = no + 1;
        cmdType = stream[0];
        charset = stream[1];
        fileNameLen = new byte[8];
        System.arraycopy(stream, 2, fileNameLen, 0, 8);
        date = new byte[8];
        System.arraycopy(stream, 10, date, 0,8);
        byte[] noBytes = new byte[8];
        System.arraycopy(stream, 18, noBytes, 0, 8);
        no = Bytes2Long(noBytes);
        fileName = new byte[(int) Bytes2Long(fileNameLen)];
        System.arraycopy(stream, 26, fileName, 0, (int) Bytes2Long(fileNameLen));
//        System.arraycopy(stream, 26, fileName, 0, 12);
    }

    public byte getCmdType() {
        return cmdType;
    }

    public void setCmdType(byte cmdType) {
        this.cmdType = cmdType;
    }

    public CharsetByte getCharset() {
        return InitData.charsetList.get(charset);
    }

    public void setCharset(CharsetByte charset) {
        this.charset = charset.getCode();
    }

    public long getFileNameLen() {
        return Bytes2Long(fileNameLen);
    }

    public void setFileNameLen(long fileNameLen) {
        this.fileNameLen = Long2Bytes(fileNameLen);
    }

    public String getFileName() {
        String s = null;
        try {
            s = new String(fileName, getCharset().getCharset());
            System.out.println(s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public void setFileName(String fileName) {
        try {
            this.fileName = fileName.getBytes(getCharset().getCharset());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return new Date(Bytes2Long(date));
    }

    public void setDate(Date date) {
        this.date = Long2Bytes(date.getTime());
    }

    public static long getNo() {
        return no;
    }

    public static void setNo(long no) {
        MsgWrapper.no = no;
    }

    @Override
    public byte[] getStream() throws MSGLengthOutOfDefault{
//        byte[] bytes = new byte[(int) (1+1+8+Bytes2Long(fileNameLen)+8+8)];
//        byte[] bytes = new byte[(int) (1+1+8+12+8+8)];
        if (26+Bytes2Long(fileNameLen) > InitData.DEFAULT_MSG_LENTGTH)
            throw new MSGLengthOutOfDefault("文件名称太长，以至于单次发送的消息长度大于2048字节");
        byte[] bytes = new byte[InitData.DEFAULT_MSG_LENTGTH];

        bytes[0] = cmdType;
        bytes[1] = charset;
        System.arraycopy(fileNameLen,    0, bytes, 2, 8);
        System.arraycopy(date,           0, bytes, 10,8);
        byte[] noBytes = Long2Bytes(no);
        System.arraycopy(Long2Bytes(no), 0, bytes, 18,8);
        System.arraycopy(fileName,       0, bytes, 26, (int) Bytes2Long(fileNameLen));
//        System.arraycopy(fileName,       0, bytes, 26, 12);
        return bytes;
    }



    @Override
    public String toString() {
        return  "cmdType=" + cmdType +
                ", charset=" + charset +
                ", fileNameLen=" + Arrays.toString(fileNameLen) +
                ", date=" + Arrays.toString(date) +
                ", no=" + Arrays.toString(Long2Bytes(no)) +
                ", fileName=" + Arrays.toString(fileName) +
                '}';
    }

    public String to2String() {
        return "MsgWrapper{" +
                "cmdType=" + cmdType +
                ", charset=" + charset +
                ", fileNameLen=" + Bytes2Long(fileNameLen) +
                ", no=" + no +
                ", date=" + new Date((Bytes2Long(date))) +
                ", fileName=" + new String(fileName) +
                '}';
    }
}
