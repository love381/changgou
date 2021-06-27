package com.changgou.order.controller;

import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import entity.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/***
 * 购物车操作
 */
@RestController
@RequestMapping(value = "/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    /***
     * 购物车列表
     * @return
     */
    @GetMapping(value = "/list")
    public Result<List<OrderItem>> list() {
        //用户的令牌信息-》解析令牌信息-》username
        //Authentication authentication= SecurityContextHolder.getContext().getAuthentication()
        //OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        //String tokenValue = details.getTokenValue();
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        System.out.println(userInfo);
        String username = userInfo.get("username");

        //获取用户登录名.
        //String username = "szitheima";  //写死了！
        //查询购物车列表
        List<OrderItem> orderItems = cartService.list(username);
        return new Result<List<OrderItem>>(true, StatusCode.OK, "购物车列表查询成功!",orderItems);
    }


        /***
         * 加入购物车
         * 1.加入购物车数量
         * 2.商品ID
         */
    @GetMapping("/add")
    public Result add(Integer number, Long id){
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        System.out.println(userInfo);
        String username = userInfo.get("username");

        cartService.add(number,id,username);
        return new Result(true, StatusCode.OK,"加入购物车成功");
    }
}
