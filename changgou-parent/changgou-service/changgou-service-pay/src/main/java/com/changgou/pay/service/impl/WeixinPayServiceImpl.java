package com.changgou.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.config.MyWXPayConfig;
import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    //应用ID
    @Value("${weixin.appid}")
    private String appid;
    //商户号
    @Value("${weixin.partner}")
    private String partner;
    //秘钥
    @Value("${weixin.partnerkey}")
    private String partnerkey;
    //支付回调地址
    @Value("${weixin.notifyurl}")
    private String notifyurl;

    /***
     * 检测支付状态
     */
    @Override
    public Map queryPayStatus(String outtradeno) {

        try {
            //封装参数
            Map<String,String> paramsMap = new HashMap<>();
            paramsMap.put("appid",appid);//应用ID
            paramsMap.put("mch_id",partner);//商户号
            paramsMap.put("out_trade_no",outtradeno); //商户订单号
            paramsMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串
            paramsMap.put("notify_url",notifyurl); //支付回调地址
            //MAP转XML字符串，携带签名
            String xmlparameters = WXPayUtil.generateSignedXml(paramsMap, partnerkey);

            //URL地址
            String url = "https://api.mch.weixin.qq.com/pay/orderquery";
            HttpClient httpClient = new HttpClient(url);
            //提交方式
            httpClient.setHttps(true);
            //提交参数
            httpClient.setXmlParam(xmlparameters);
            //执行请求
            httpClient.post();
            //获取返回的数据
            String result = httpClient.getContent();
            //返回数据转成Map
            Map<String,String> resultMap = WXPayUtil.xmlToMap(result);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 创建二维码操作
     */
    @Override
    public Map createNative(Map<String, String> parameterMap) {

        try {
            //封装参数
            Map<String,String> paramsMap = new HashMap<>();
            paramsMap.put("appid",appid);//应用ID
            paramsMap.put("mch_id",partner);//商户号
            paramsMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串
            paramsMap.put("body","畅购商城商品！");//商品描述
            paramsMap.put("out_trade_no",parameterMap.get("outtradeno")); //商户订单号
            paramsMap.put("total_fee",parameterMap.get("totalfee")); //标价金额
            paramsMap.put("spbill_create_ip","127.0.0.1"); //终端IP
            paramsMap.put("notify_url",notifyurl); //支付回调地址
            paramsMap.put("trade_type","NATIVE");
            //获取自定义数据
            String exchange = parameterMap.get("exchange");
            String routingkey = parameterMap.get("routingkey");
            HashMap<String, String> attachMap = new HashMap<>();
            attachMap.put("exchange",exchange);
            attachMap.put("routingkey",routingkey);
            //如果是秒杀订单，需要传入username
            String username = parameterMap.get("username");
            if (!StringUtils.isEmpty(username)) {
                attachMap.put("username",username);
            }
            String attach = JSON.toJSONString(attachMap);
            paramsMap.put("attach",attach);

            //MAP转XML字符串，携带签名
            String xmlparameters = WXPayUtil.generateSignedXml(paramsMap, partnerkey);

            //URL地址
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            HttpClient httpClient = new HttpClient(url);
            //提交方式
            httpClient.setHttps(true);
            //提交参数
            httpClient.setXmlParam(xmlparameters);
            //执行请求
            httpClient.post();
            //获取返回的数据
            String result = httpClient.getContent();
            //返回数据转成Map
            Map<String,String> resultMap = WXPayUtil.xmlToMap(result);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 根据订单号关闭订单
     * @param outTradeNo    商户订单号
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> closeOrder(String outTradeNo) throws Exception {
        Map<String,String> map = new HashMap<>(16);
        map.put("out_trade_no",outTradeNo); //商户订单号
        MyWXPayConfig config = new MyWXPayConfig(appid,partner, partnerkey);
        WXPay wxpay = new WXPay(config,notifyurl);
        return wxpay.closeOrder(map);
    }

}
