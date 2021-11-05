package com.test;

import com.main.Msg.MSGLengthOutOfDefault;
import com.main.Msg.Msg;
import com.main.Msg.MsgWrapper;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TestSocket {
    public static void main(String[] args) {
//        byte[] bytes = new byte[1024];
//        DataInputStream dis;
//        try {
//            Socket socket = new Socket(InetAddress.getByName("localhost"), 8000);
//            System.out.println("建立连接");
//            dis = new DataInputStream(socket.getInputStream());
//            System.out.println("接收到的长度："+dis.read(bytes));
//            System.out.println("接收到的字符："+new String(bytes));
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Msg m = new MsgWrapper("abcdefghigklnmopqrstuvwxyz1234567890-=".getBytes());
        try {
            System.out.println(new String(m.getStream()));
        } catch (MSGLengthOutOfDefault msgLengthOutOfDefault) {
            msgLengthOutOfDefault.printStackTrace();
        }
    }
}
