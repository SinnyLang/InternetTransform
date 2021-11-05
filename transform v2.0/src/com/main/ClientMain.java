package com.main;

import com.main.auth.SocketAuth;
import com.main.cmdable.Sendable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        SocketWrapper socketWrapper;
        Socket socket = new Socket(InetAddress.getByName("localhost"), 8000);
        socketWrapper = new SocketWrapper(socket);

        // 验证身份
        SocketAuth auth = new SocketAuth(socketWrapper);
        if (!auth.isPassAuthentication()){
            System.out.println("认证失败");
            return;
        }
        System.out.println("认证成功");
        socketWrapper.setKey(auth.getKey());

        try{
            while (line != null){
                if("by".equals(line)){
                    socketWrapper.close();
                    break;
                }
                System.out.println(line);
                LineProcessor process = new LineProcessor(line);
                String[] token = process.getToken();

                Class<? extends Sendable> clazz = InitData.getClientMapDealClass(token[0]);
                Sendable sendable = clazz.getConstructor( SocketWrapper.class, String[].class).newInstance(socketWrapper, token);

                sendable.doTask();
                line = sc.nextLine();
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
