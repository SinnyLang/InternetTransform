package com.main;

import com.main.cmdable.Sendable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitData {
    public static final String CLIENT_FILE_PATH = "D:\\";

    // 初始化Charset所支持的字符集到内存
    public static final List<CharsetByte> charsetList =
            new ArrayList<CharsetByte>(){{
                add(new CharsetByte("gbk", (byte) 0 ));
                add(new CharsetByte("utf8", (byte) 1 ));
                add(new CharsetByte("utf16", (byte) 2));
            }};

    // 初始化客户端命令到内存
    public static final Map<String, Class<? extends Sendable>>
    clientMapDealClass =
            new HashMap<String, Class<? extends Sendable>>(){{
                for(SendCommendEnum s : SendCommendEnum.values()){
                    put(s.getMsg().toLowerCase(), s.getSendableClass());
                }
            }};
    public static final String SERVER_DEFAULT_PATH = "D:\\server\\";
    public static final long DEFAULT_PAGE_SIZE = 0x400;
    public static final int DEFAULT_MSG_LENTGTH = 2048;
    public static final long DEFAULT_AUTH_TIMEOUT = 10000;

    public static CharsetByte findCharsetInList(String s) {
        for (CharsetByte charsetByte : charsetList) {
            if (charsetByte.getCharset().equals(s)) {
                return charsetByte;
            }
        }
        // 找不到对应的字符集就默认使用utf8
        return charsetList.get(1);
    }

    public static Class<? extends Sendable> getClientMapDealClass(String s) {
        return clientMapDealClass.get(s.toLowerCase());
    }
}
