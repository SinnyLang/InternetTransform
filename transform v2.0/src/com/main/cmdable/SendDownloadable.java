package com.main.cmdable;

import com.main.CharsetByte;
import com.main.InitData;
import com.main.Msg.CipherMsg;
import com.main.Msg.MSGLengthOutOfDefault;
import com.main.Msg.MsgWrapper;
import com.main.SendCommendEnum;
import com.main.SocketWrapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class SendDownloadable implements Sendable{
    private SocketWrapper socketWrapper;
    private String[] token;

    public SendDownloadable(SocketWrapper socketWrapper, String[] token) {
        this.socketWrapper = socketWrapper;
        this.token = token;
    }

    @Override
    public byte getCommandType() {
        return SendCommendEnum.DOWNLOAD_FILE.getCommandType();
    }

    @Override
    public void doTask() throws IOException {
//        byte cmd = getCommandType();
//        socketWrapper.writeByte(cmd);

        CharsetByte charset = InitData.findCharsetInList(token[1]);
        // 读取文件名称
//        byte[] bytes = token[2].getBytes(charset.getCharset());

        // 发送要 下载 的文件名称对应的字节流长度
//        socketWrapper.writeByte(charset.getCode());
//        socketWrapper.writeLong(bytes.length);
//        socketWrapper.writeBytes(bytes, 0 ,bytes.length);

        MsgWrapper msg = new MsgWrapper();
        msg.setCmdType(getCommandType());
        msg.setCharset(charset);
        msg.setFileNameLen(token[2].length());
        msg.setFileName(token[2]);
        msg.setDate(new Date(System.currentTimeMillis()));
        try {
            socketWrapper.sendMsg(msg);
        } catch (MSGLengthOutOfDefault msgLengthOutOfDefault) {
            msgLengthOutOfDefault.printStackTrace();
        }
        byte isExist = ((CipherMsg)socketWrapper.readCipherMsg()).getCipherMsgBytes()[0];
//        System.out.println("downloadFile 时 收到的文件是否存在的值是： " + isExist);
        if ( isExist > 0){
            // 文件存在可以下载
            socketWrapper.readFile(InitData.CLIENT_FILE_PATH + token[2]);
        } else {
            // 文件不存在
            throw new FileNotFoundException(token[2]);
        }
    }
}
