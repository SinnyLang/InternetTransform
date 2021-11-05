package com.main;

import com.main.Msg.*;
import com.main.auth.SocketAuth;
import com.main.auth.SocketAuthServer;

import java.io.*;
import java.util.Arrays;
import java.util.Date;

public class Worker extends Thread{
    private SocketWrapper socketWrapper;
    private int id;
    private MsgWrapper msg;
    private byte[] key; // 对称密钥

    public Worker(SocketWrapper socketWrapper, int id) {
        this.socketWrapper = socketWrapper;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            // 首先做客户端校验
//            AuthMsg authMsg = socketWrapper.readAuthMsg();
            SocketAuthServer auth = new SocketAuthServer(socketWrapper);
            if (!auth.isPassAuthentication()){
                System.out.println("认证失败");
                return;
            }
            System.out.println("认证成功");

            // 验证成功后设置会话密钥
            socketWrapper.setKey(auth.getKey());

            while (true) {
                if (this.isInterrupted()) {
                    System.out.println("线程[id=" + id + "]被打断，停止处理客户端请求");
                    break;
                }
                msg = (MsgWrapper) socketWrapper.readMsgWrapper();
                byte command = msg.getCmdType();
                if (command == SendCommendEnum.SEND_MESSAGE.getCommandType()) {
                    processSendMessage();
                } else if (command == SendCommendEnum.SEND_FILE.getCommandType()) {
                    processSendFile();
                } else if (command == SendCommendEnum.DOWNLOAD_FILE.getCommandType()) {
                    processDownloadFile();
                } else if (command == -1){
                    break;
                }
            }
        } finally {
            closeStream();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    private void processSendFile(){
//        CharsetByte charset = InitData.charsetList.get(socketWrapper.readByte());
//        long filenameLength = socketWrapper.readLong();
//        byte[] fileName = new byte[(int) filenameLength];
//        socketWrapper.readBytes(fileName, fileName.length);
        // 然后读取编码、文件名称、文件长度

        CharsetByte charset = msg.getCharset();
        long fileNameLength = msg.getFileNameLen();
        String fileName = msg.getFileName();

        System.out.println("服务器收到的文件名是 " + fileName);
        File file = new File(InitData.SERVER_DEFAULT_PATH + fileName);
        try {
            if (!file.exists()){
                socketWrapper.sendMsg(new CipherMsg(new byte[]{22}, MsgWrapper.getNo()));
                socketWrapper.readFile(InitData.SERVER_DEFAULT_PATH + fileName);
            }else {
               socketWrapper.sendMsg(new CipherMsg(new byte[]{-2}, MsgWrapper.getNo()));
            }
        } catch (MSGLengthOutOfDefault msgLengthOutOfDefault) {
            msgLengthOutOfDefault.printStackTrace();
        }
        /**
        // 判断文件是否存在
        File file = new File(InitData.SERVER_DEFAULT_PATH + new String(fileName));
        if (!file.exists()){
            long fileLength = 0;
            try (FileOutputStream fos = new FileOutputStream(file)){
                socketWrapper.writeByte((byte) 1);
                if (file.createNewFile()){
                    System.out.println("文件创建失败");
                }

                // 读取文件大小
                fileLength = socketWrapper.readLong();
                byte[] bytes = new byte[(int) InitData.DEFAULT_PAGE_SIZE];
                if (fileLength <= InitData.DEFAULT_PAGE_SIZE) {
                    socketWrapper.readBytes(bytes, (int) fileLength);
                    fos.write(bytes, 0, (int) fileLength);
                }else {
                    // 创建文件并写入数据
                    long schedule = 0;
                    int len = socketWrapper.readBytes(bytes, bytes.length);
                    while ( schedule < fileLength ){
                        fos.write(bytes, 0, len);
                        schedule += len;
                        len = socketWrapper.readBytes(bytes, bytes.length);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file.length() != fileLength){
                System.out.println("线程id "+id+" 传输错误文件大小不一致");
            }
        }else {
            // 文件不存在则返回 -2
            socketWrapper.writeByte((byte) -2);
        }
         */
    }

    private void processDownloadFile(){
        CharsetByte charset = msg.getCharset();
        long filenameLength = msg.getFileNameLen();
//        byte[] filename = new byte[(int) filenameLength];
//        socketWrapper.readBytes(filename, filename.length);
        String fileName = msg.getFileName();
        // 判断文件是否存在
        File file = null;
        file = new File(InitData.SERVER_DEFAULT_PATH + fileName);
        try {
            if (file.exists()) {
                socketWrapper.sendMsg(new CipherMsg(new byte[]{22}, MsgWrapper.getNo())); // 发送客户端21表明文件存在
                socketWrapper.writeFile(InitData.SERVER_DEFAULT_PATH + fileName);
            }else {
                // 文件不存在
                socketWrapper.sendMsg(new CipherMsg(new byte[]{-2}, MsgWrapper.getNo()));
            }
        } catch (MSGLengthOutOfDefault msgLengthOutOfDefault) {
            msgLengthOutOfDefault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processSendMessage(){
//        CharsetByte charset = InitData.charsetList.get(socketWrapper.readByte());
        System.out.println("服务器收到的Str："+ msg.getFileName());
        MsgWrapper msg = new MsgWrapper();
        msg.setCharset(this.msg.getCharset());
        msg.setFileName(this.msg.getFileName());
        msg.setFileNameLen(this.msg.getFileNameLen());
        msg.setDate(new Date());
        try {
            socketWrapper.sendMsg(msg);
        } catch (MSGLengthOutOfDefault msgLengthOutOfDefault) {
            msgLengthOutOfDefault.printStackTrace();
        }
    }

    private void closeStream(){
        socketWrapper.close();
    }
}
