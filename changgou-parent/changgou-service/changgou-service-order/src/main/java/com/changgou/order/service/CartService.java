package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;

import java.util.List;

public interface CartService {
    /***
     * 加入购物车
     */
    void add(Integer number, Long id, String username);

    /***
     *购物车集合查询
     * @param username 用户登录名
     */
    List<OrderItem> list (String username) ;

}
