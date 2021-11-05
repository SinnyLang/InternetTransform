package com.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {
    public static final List<Worker> workers = new ArrayList<Worker>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("启动端口8000，准备服务");

            int index = 0;
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("连接建立 id = "+ index);
                SocketWrapper socketWrapper = new SocketWrapper(socket);
                workers.add(new Worker(socketWrapper, index++));
                workers.get(index - 1).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            interruptWorkers();
        }
    }

    private static void interruptWorkers() {
        for (int i = 0; i < workers.size(); i++) {
            workers.get(i).interrupt();
        }
    }
}
