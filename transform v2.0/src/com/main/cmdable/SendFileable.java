package com.main.cmdable;

import com.main.CharsetByte;
import com.main.InitData;
import com.main.Msg.CipherMsg;
import com.main.Msg.MSGLengthOutOfDefault;
import com.main.Msg.MsgWrapper;
import com.main.SendCommendEnum;
import com.main.SocketWrapper;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class SendFileable implements Sendable {
    private SocketWrapper socketWrapper;
    private String[] token;

    public SendFileable(SocketWrapper socketWrapper, String[] token) {
        this.socketWrapper = socketWrapper;
        this.token = token;
    }

    @Override
    public byte getCommandType() {
        return SendCommendEnum.SEND_FILE.getCommandType();
    }

    @Override
    public void doTask() throws IOException {
//        socketWrapper.writeByte(getCommandType());

        CharsetByte charsetByte = InitData.findCharsetInList(token[1]);
        byte[] bytes = token[2].getBytes(charsetByte.getCharset());

        // 发送文件名对应的字符流
//        socketWrapper.writeByte(charsetByte.getCode());
//        socketWrapper.writeLong(bytes.length);
//        socketWrapper.writeBytes(bytes, 0 ,bytes.length);

        MsgWrapper msg = new MsgWrapper();
        msg.setCmdType(getCommandType());
        msg.setCharset(charsetByte);
        msg.setFileNameLen(bytes.length);
        msg.setFileName(new String(bytes, charsetByte.getCharset()));
        msg.setDate(new Date(System.currentTimeMillis()));
        try {
            socketWrapper.sendMsg(msg);
        } catch (MSGLengthOutOfDefault msgLengthOutOfDefault) {
            msgLengthOutOfDefault.printStackTrace();
        }

        // 看看接收端响应，文件是否已经存在
        byte isExist = ((CipherMsg)socketWrapper.readCipherMsg()).getCipherMsgBytes()[0];
        if ( isExist > 0 ) {
            socketWrapper.writeFile(InitData.CLIENT_FILE_PATH +
                    File.separator + token[2]);
        }else {
            socketWrapper.close();
            throw new SendFileAlreadyExistException(token[2]);
        }
//        socketWrapper.close();
    }
}
