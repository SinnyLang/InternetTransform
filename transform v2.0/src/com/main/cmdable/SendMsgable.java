package com.main.cmdable;

import com.main.CharsetByte;
import com.main.InitData;
import com.main.Msg.MSGLengthOutOfDefault;
import com.main.Msg.MsgWrapper;
import com.main.SendCommendEnum;
import com.main.SocketWrapper;

import java.io.IOException;
import java.util.Date;

public class SendMsgable implements Sendable {
    private SocketWrapper socketWrapper;

    public SendMsgable(SocketWrapper socketWrapper, String[] token) {
        this.socketWrapper = socketWrapper;
        this.token = token;
    }

    private String[] token;

    @Override
    public byte getCommandType() {
        return SendCommendEnum.SEND_MESSAGE.getCommandType();
    }

    @Override
    public void doTask() throws IOException {
        CharsetByte charsetByte = InitData.findCharsetInList(token[1]);
//        socketWrapper.writeByte(getCommandType());
//        socketWrapper.writeByte(charsetByte.getCode());
//        socketWrapper.writeString(token[2], charsetByte);

        MsgWrapper msg = new MsgWrapper();
        msg.setCmdType(getCommandType());
        msg.setCharset(charsetByte);
        msg.setFileNameLen(token[2].length());
        msg.setFileName(token[2]);
        msg.setDate(new Date(System.currentTimeMillis()));
        try {
            socketWrapper.sendMsg(msg);
        } catch (MSGLengthOutOfDefault msgLengthOutOfDefault) {
            msgLengthOutOfDefault.printStackTrace();
        }

        MsgWrapper rcvMsg = (MsgWrapper) socketWrapper.readMsgWrapper();
        System.out.println(rcvMsg.getFileName());
    }
}
