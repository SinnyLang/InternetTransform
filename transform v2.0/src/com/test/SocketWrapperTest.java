package com.test;

import com.main.CharsetByte;
import com.main.InitData;
import com.main.SocketWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketWrapperTest {
    byte[] bytes;
    ServerSocket serverSocket;
    Socket socket;
    SocketWrapper socketWrapper;

    @Before
    public void setUp() throws Exception {
        bytes = new byte[1024];
        serverSocket = new ServerSocket(8000);
        socket = serverSocket.accept();
        socketWrapper = new SocketWrapper(socket);
        socketWrapper.setKey("0123456789abcdef".getBytes(StandardCharsets.UTF_8));
    }

    @After
    public void tearDown() throws Exception {
        socket.close();
        serverSocket.close();
    }

    @Test
    public void writeByte() {
        System.out.println("写入byte：0x45");
        socketWrapper.writeByte((byte) 0x45);
    }

    @Test
    public void readByte() {
        System.out.println("接收到byte数据是："+socketWrapper.readByte());
    }

    @Test
    public void writeLong() {
        System.out.println("写入long数据：0xffffffff1");
        socketWrapper.writeLong(0xffffffff1L);
    }

    @Test
    public void readLong() {
        System.out.printf("接收到的Long数据是:%X", socketWrapper.readLong());
    }

    @Test
    public void writeString() {
        System.out.println("写入的字符串是：ha ha，来看socket.");
        String s = "ha ha，来看socket.";
        socketWrapper.writeString(s, InitData.findCharsetInList("utf8"));
    }

    @Test
    public void readBytes() {
    }

    @Test
    public void writeFile() {
        try {
            //socketWrapper.writeFile("C:\\Users\\Sinny Lang\\Desktop\\a.txt");
            socketWrapper.writeFile("C:\\Users\\Sinny Lang\\Desktop\\aaa.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void writeBytes() {
        System.out.println("写入的bytes是：ha ，快来看socket.");
        socketWrapper.writeBytes("ha ，快来看socket.".getBytes(), 0, 17);
    }

    @Test
    public void close() {
    }

    @Test
    public void closeStream() {
    }
}