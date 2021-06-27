package com.itheima.weixin;

import com.github.wxpay.sdk.WXPayUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class weixinUtilTest {

    @Test
    public void test1() throws Exception {
        //生成随机字符串
        String s = WXPayUtil.generateNonceStr(); //生成随机字符串
        System.out.println(s);

        //Map数据转XML格式(无签名)
        Map<String,String> datamap = new HashMap<>();
        datamap.put("id","No.001");
        datamap.put("title","畅购测试");
        datamap.put("money","998");
        String mapToXml = WXPayUtil.mapToXml(datamap);   //Map数据转XML格式
        System.out.println(mapToXml);

        //Map数据转XML格式(有签名)
        String signedXml = WXPayUtil.generateSignedXml(datamap, "itcast");
        System.out.println(signedXml);

        //XML数据转MAP格式
        Map<String, String> xmlToMap = WXPayUtil.xmlToMap(signedXml);
        System.out.println(xmlToMap);
    }

}
