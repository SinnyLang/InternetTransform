package com.main.Msg;

public interface Msg {
    // 以byte流的形式得到的msg中的所有数据
    // 返回的byte流长度的是固定长度的
    byte[] getStream() throws MSGLengthOutOfDefault ;

}
