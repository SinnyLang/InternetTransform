package com.main;

import com.main.Msg.*;
import com.main.security.AESExecutor;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class SocketWrapper {
    public static final long DEFAULT_PAGE_SIZE = 0x400;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private byte[] key; //  = "0123456789abcdef".getBytes();
    private AESExecutor aesExecutor;

    public SocketWrapper(Socket socket) {
        this.socket = socket;
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("未能从socket中获取数据流");
        }
    }

    public SocketWrapper(String s, int i) {
    }

    public void sendMsg(Msg msg) throws MSGLengthOutOfDefault {
        writeBytes(msg.getStream(), 0, InitData.DEFAULT_MSG_LENTGTH);
//            dos.write(new AESExecutor(AESExecutor.AES).encrypt(msg.getStream(), key));
    }

    public Msg readMsgWrapper(){
        byte[] bytes = new byte[InitData.DEFAULT_MSG_LENTGTH];
        int MsgLen = readBytes(bytes, bytes.length);
        byte[] rcv = new byte[MsgLen];
        System.arraycopy(bytes, 0, rcv, 0, MsgLen);
        MsgWrapper msg = new MsgWrapper(rcv);
        System.out.println(msg);
        return msg;
    }

    public Msg readCipherMsg(){
        byte[] bytes = new byte[InitData.DEFAULT_MSG_LENTGTH];
        int MsgLen = readBytes(bytes, bytes.length);
        byte[] rcv = new byte[MsgLen];
        System.arraycopy(bytes, 0, rcv, 0, MsgLen);
        CipherMsg msg = new CipherMsg(rcv, rcv.length);
//        System.out.println(msg);
        return msg;

//        byte[] bytes = new byte[2048];
//        byte[] decryptStream = new byte[0];
//        try {
//            int MsgLen = dis.read(bytes);
//            decryptStream = new byte[MsgLen];
//            System.arraycopy(bytes, 0, decryptStream, 0, MsgLen);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new CipherMsg(new AESExecutor(AESExecutor.AES).decrypt(decryptStream, key));
    }

    public void writeByte(byte b) {
        try {
            dos.writeByte(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte readByte() {

        byte b = -1;
        try {
            b = (byte) dis.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public void writeLong(long l) {
        try {
            dos.writeLong(l);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long readLong() {
        long l = 0;
        try {
            l = dis.readLong();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return l;
    }

    public void writeString(String s, CharsetByte charset) {
        try {
            dos.write(s.getBytes(charset.getCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readString(CharsetByte charset){
        byte[] bytes = new byte[(int) InitData.DEFAULT_PAGE_SIZE];
        int len = readBytes(bytes, bytes.length);
        String s = null;
        try {
            s = new String(bytes, 0, len, charset.getCharset());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public void writeFile(String filePath) throws IOException {
        //AESExecutor aesExecutor = new AESExecutor(AESExecutor.AES);
        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }

        long fileLength = file.length();

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) InitData.DEFAULT_PAGE_SIZE];
            int len = fis.read(bytes, 8, bytes.length - 8);
//            System.out.println("从文件中读取的数据是：" + Arrays.toString(bytes));

//            dos.writeLong(fileLength);
            // 将文件长度不再单独发送，长度封装在文件中，开头的8个字节
            long l = fileLength;
            for (int i = 0; i < 8; i++) {
                bytes[i] = (byte) (l & 0xff);
                l = l >> 8;
            }
            if (fileLength + 8 <= InitData.DEFAULT_PAGE_SIZE) {
                writeBytes(bytes, 0, (int) (fileLength + 8));
//                System.out.println(System.currentTimeMillis()+"时刻，写入的数据writeBytes：" + Arrays.toString(bytes));
            } else {
                long page = fileLength / InitData.DEFAULT_PAGE_SIZE + 1;
                long count = 1;
                writeBytes(bytes, 0, len + 8);
                // 发送完第一个数据包之后，开始将文件流填充满缓冲区
                len = fis.read(bytes);
                while (len > 0) {
                    writeBytes(bytes, 0, len);
                    len = fis.read(bytes);

                    count++;
                    System.out.printf("%4d %%\r", (int) count * 100 / page);
                }
                // |||||||||||||||||||||||||| \\
                //dos.write(bytes, 0, bytes.length);
                System.out.println("100%");
            }
        }
    }

    public void readFile(String filePath) {
        File file = new File(filePath);
        //if (!file.exists()) {
            // 读取文件从长度
            byte[] bytes = new byte[(int) InitData.DEFAULT_PAGE_SIZE];


            try (FileOutputStream fos = new FileOutputStream(file)) {
                long fileLength = 0;
                int len = readBytes(bytes, bytes.length);
                for (int i = 7; i >= 0; i--) {
                    fileLength = fileLength << 8;
                    // byte强转成long可能会出现负数的情况，强转之前使用0xff且运算可防止
                    fileLength = fileLength | ( 0xffL & bytes[i] );
                }
                System.out.println("文件长度是： "+ fileLength);

                // 从网络流读取文件，如果不存在文件则创建一个
                if (!file.exists()){
                    if (file.createNewFile()) {
                        System.out.println("文件创建失败");
                    }
                }

                // 传输的总字节数 = 文件真实长度 + 额外的8个字节
                if (fileLength + 8 <= InitData.DEFAULT_PAGE_SIZE) {
                    //int len = dis.read(bytes, 8, (int) InitData.DEFAULT_PAGE_SIZE);
                    fos.write(bytes, 8, len - 8);
                } else {
                    // 第一次的数据流因为与后面的数据流不同，所以单独写入文件
                    long schedule = len - 8;
                    fos.write(bytes, 8, (int) schedule);

                    while (schedule < fileLength) {
                        System.out.print("已接收：" + schedule + "字节\r");
                        len = readBytes(bytes, bytes.length);
                        fos.write(bytes, 0, len);
                        schedule += len;
                    }
                    System.out.print("已接收：" + schedule + "字节\r");
                    System.out.println("\n接收完成");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        //} else {
            // 如果要写入的文件早已存在则提示发送端不可写入 -2
            //try {
            //    dos.writeByte((byte) -2);
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
        //}
    }

    public int readBytes(byte[] bytes, int len) {
        // 该地址用于放入接收的数据， 接收的长度
        byte[] rcvBytes = new byte[len + 16];
        byte[] factBytes = new byte[0];
        int l = 0;
        try {
            // 读取的时候要多读取16个字节，
            // 因为读取的块如果是1024位的话，实际加密后的密文长度要多出16字节的填充位
            // 读取的字节比多出的16位还多也是不可以的 会将下一次的数据过早的提取 导致解密失败
            l = dis.read(rcvBytes, 0, len + 16);
//            System.out.println(System.currentTimeMillis()+"时刻，读取的数据rcvBytes：" + Arrays.toString(rcvBytes));

            //System.out.println("收到的原始数据是：" + Base64.getEncoder().encodeToString(bytes));
//            System.out.println("l = "+ l+" ,len = "+len);
            factBytes = new byte[l];
            System.arraycopy(rcvBytes, 0, factBytes, 0, l);
            if (l % 16 != 0){
                System.out.println("readBytes接收到的AES加密后的字节数不是16的倍数");
            }
            // 数据未能填满缓冲区这种情况 需要考虑
//            System.out.println("解密密钥 = "+new String(key)+Arrays.toString(key));
//            System.out.println("实际解密的数据是："+Base64.getEncoder().encodeToString(factBytes));
//            System.out.println("转换成字符是：---\n" + new String(factBytes) + "\n---");
            factBytes = new AESExecutor(AESExecutor.AES).decrypt(factBytes, key);
//            System.out.println("实际解密的数据:factBytes"+ Arrays.toString(factBytes));
            System.arraycopy(factBytes, 0, bytes, 0, factBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return factBytes.length;
    }

    public void writeBytes(byte[] bytes, int off, int len) {
        // 传输的数据所在的地址，数据的开始的地址， 发送的数据的长度
        byte[] sendBytes;
        if (bytes.length > len){ // 发送的数据填不满缓冲区
            sendBytes = new byte[len];
            System.arraycopy(bytes, off, sendBytes, 0, len);
        } else { // 发送的数据可以填满缓冲区
            sendBytes = bytes;
        }
        try {
//            System.out.println("加密密钥 = "+Arrays.toString(key));
            byte[] out = new AESExecutor(AESExecutor.AES).encrypt(sendBytes, key);
//            System.out.println("out.length = "+out.length);
//            System.out.println("发送的字符是：---\n"+new String(bytes)+"\n" + Arrays.toString(bytes) + "\n---");
//            System.out.println("发送的数据是" + Base64.getEncoder().encodeToString(out));
//            System.out.println("转换成字符是：---\n" + new String(out) + "\n---");
            dos.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    private byte[] getKey() {
        return key;
    }

    public void closeStream(Closeable closeable) throws IOException {
        closeable.close();
    }

    public void sendAuthMsg(AuthMsg authMsg) {
        try {
            dos.write(authMsg.getStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthMsg readAuthMsg(){
        byte[] bytes = new byte[8+8+128+128];
        try {
            int len = dis.read(bytes, 0, 8+8+128+128);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AuthMsg(bytes);
    }
}
