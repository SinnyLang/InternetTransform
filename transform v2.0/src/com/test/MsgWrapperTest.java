package com.test;

import com.main.CharsetByte;
import com.main.Msg.MSGLengthOutOfDefault;
import com.main.Msg.MsgWrapper;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

public class MsgWrapperTest {

    @Test
    public void getStream() throws MSGLengthOutOfDefault {
        MsgWrapper msg = new MsgWrapper();
        msg.setCmdType((byte) 0x5);
        msg.setCharset(new CharsetByte("utf-8", (byte) 1));
        msg.setFileNameLen(7);
        msg.setFileName(new String("abc.txt".getBytes(), StandardCharsets.UTF_8));
        msg.setDate(new Date(System.currentTimeMillis()));
//        MsgWrapper.setNo(99);
        System.out.println(msg);
        System.out.println(Arrays.toString(msg.getStream()));
        try {
            MsgWrapper msgWrapper = new MsgWrapper(msg.getStream());
            System.out.println(msgWrapper);
            System.out.println(Arrays.toString(msgWrapper.getStream()));
        } catch (MSGLengthOutOfDefault msgLengthOutOfDefault) {
            msgLengthOutOfDefault.printStackTrace();
        }
    }

    @Test
    public void bytes2Long() {
        MsgWrapper msg = new MsgWrapper();
        long r = (long) (Math.random()*Long.MAX_VALUE);
        System.out.print(r+" 转换后 ");
        //测试时可以将下面这两种方法设为public 当前已经设为私有
//        System.out.println(msg.Bytes2Long(msg.Long2Bytes(r)));
    }

}