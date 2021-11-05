package com.main;

import com.main.cmdable.SendDownloadable;
import com.main.cmdable.SendFileable;
import com.main.cmdable.SendMsgable;
import com.main.cmdable.Sendable;

public enum SendCommendEnum {
    SEND_MESSAGE((byte) 10, "sendMessage"),
    SEND_FILE((byte) 11, "sendFile"),
    DOWNLOAD_FILE((byte) 12, "downloadFile");

    public byte commandType;
    public String msg;

    SendCommendEnum(byte commandType, String msg) {
        this.commandType = commandType;
        this.msg = msg;
    }

    public byte getCommandType() {
        return commandType;
    }

    public String getMsg() {
        return msg;
    }

    //根据收到的命令选择对应的处理类
    public Class<? extends Sendable> getSendableClass(){
        Class s = null;
        if(this.msg.equalsIgnoreCase("sendMessage")){
            s = SendMsgable.class;
        } else if (this.msg.equalsIgnoreCase("sendFile")){
            s = SendFileable.class;
        }else{
            s = SendDownloadable.class;
        }
        return s;
    }
}
