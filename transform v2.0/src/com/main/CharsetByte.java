package com.main;

// gbk utf-8 utf-16
public class CharsetByte {
    private String charset;
    private byte code;

    public CharsetByte(String charset, byte code) {
        this.charset = charset;
        this.code = code;
    }

    public byte getCodeByString(String charset){
        if (charset != null) {
            if (this.charset.equals(charset.replace("-", "").toLowerCase())){
                return this.code;
            }
        }
        return -1;
    }

    public boolean isCharsetByCode(byte code){
        return this.code == code;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }
}
