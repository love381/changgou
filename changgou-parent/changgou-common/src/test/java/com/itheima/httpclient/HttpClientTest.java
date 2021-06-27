package com.itheima.httpclient;

import entity.HttpClient;
import org.junit.Test;

import java.io.IOException;

public class HttpClientTest {
    /***
     *发送Htpp/Https请求
     *发送指定参数
     *可以获取响应的结果
     */
    @Test
    public void test() throws IOException {
        //准备url
        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        //创建对象
        HttpClient httpClient = new HttpClient(url);
        //要发送的XML数据
        String xml = "<xml><name>占山</name></xml>";
        httpClient.setXmlParam(xml);
        //https/http
        httpClient.setHttps(true);
        //发送请求，->POST
        httpClient.post();
        //获取响应数据
        String result = httpClient.getContent();
        System.out.println(result);
    }
}
