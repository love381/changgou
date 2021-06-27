package com.itheima.base64;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Test {

    /***
     * 加密测试
     */
    @Test
    public void testEncode() throws UnsupportedEncodingException {
        byte[] encode = Base64.getEncoder().encode("abcdefg".getBytes());

        String str = new String(encode,"UTF-8");

        System.out.println("加密后的密文:" + str);
    }

    /***
     * 解密测试
     */
    @Test
    public void testDecode() throws UnsupportedEncodingException {
        String str = "YWJjZGVmZw==";
        byte[] decode = Base64.getDecoder().decode(str);
        String string = new String(decode,"UTF-8");
        System.out.println("解密后的密文:" + string);
    }
}
