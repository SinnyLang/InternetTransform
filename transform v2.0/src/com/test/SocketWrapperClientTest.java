package com.test;

import com.main.SocketWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

public class SocketWrapperClientTest {

    byte[] bytes;
    SocketWrapper socketWrapper;
    Socket socket;

    @Before
    public void setUp() throws Exception {
        bytes = new byte[1024];
        socket = new Socket(InetAddress.getByName("localhost"), 8000);
        socketWrapper = new SocketWrapper(socket);
        socketWrapper.setKey("0123456789abcdef".getBytes(StandardCharsets.UTF_8));
    }

    @After
    public void tearDown() throws Exception {
        //socket.close();
    }

    @Test
    public void readByte() {
        System.out.println("接收到byte数据是："+socketWrapper.readByte());
    }

    @Test
    public void writeByte() {
        socketWrapper.writeByte((byte) 0x69);
    }

    @Test
    public void readLong() {
        System.out.printf("接收到的Long数据是:%X", socketWrapper.readLong());
    }

    @Test
    public void writeLong() {
        System.out.println("写入long数据：0x123456789");
        socketWrapper.writeLong(0x123456789L);
    }

    @Test
    public void readBytes() {
        StringBuffer stringBuffer = new StringBuffer("读取到的文件或者bytes是：\n");
        long fileLength = socketWrapper.readLong();
        if( fileLength > 1024){
            System.out.println("文件大小是："+fileLength);
            int len = socketWrapper.readBytes(bytes, bytes.length);
            while (len > 0){
                stringBuffer.append(new String(bytes));
                len = socketWrapper.readBytes(bytes, bytes.length);
            }
        }else {
            socketWrapper.readBytes(bytes, bytes.length);
            stringBuffer.append(new String(bytes));
        }
        System.out.println(stringBuffer);
    }

    @Test
    public void writeString() {

    }

    @Test
    public void writeFile() {
    }

    @Test
    public void writeBytes() {
    }

    @Test
    public void close() {
    }

    @Test
    public void closeStream() {
    }

    @Test
    public void readFile() {
        socketWrapper.readFile("C:\\Users\\Sinny Lang\\Desktop\\a.txt");
    }
}