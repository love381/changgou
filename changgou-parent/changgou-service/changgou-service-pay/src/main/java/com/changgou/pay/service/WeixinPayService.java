package com.changgou.pay.service;

import java.util.Map;

public interface WeixinPayService {
    /***
     * 检测支付状态
     */
    Map queryPayStatus(String outtradeno);

    /***
     * 创建二维码操作
     */
    Map createNative(Map<String,String> parameterMap);

    /**
     * 关闭订单
     * @param outTradeNo    商户订单号
     * @return
     * @throws Exception
     */
    Map<String, String> closeOrder(String outTradeNo) throws Exception;
}
