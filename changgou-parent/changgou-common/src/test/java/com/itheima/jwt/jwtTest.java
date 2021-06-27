package com.itheima.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class jwtTest {
    @Test
    public void testCreateJwt(){
        JwtBuilder builder= Jwts.builder()
                .setId("888")             //设置唯一编号
                .setSubject("小白")       //设置主题  可以是JSON数据
                .setIssuedAt(new Date())  //设置签发日期
                .setExpiration(new Date(System.currentTimeMillis()+3600000)) //过期时间：一小时
                .signWith(SignatureAlgorithm.HS256,"itcast");//设置签名 使用HS256算法，并设置盐SecretKey(字符串)
        //自定义载荷（playload）
        Map<String,Object> info = new HashMap<String, Object>();
        info.put("company","黑马");
        info.put("address","北京");
        info.put("money",35000);
        builder.addClaims(info); //添加载荷

        //构建 并返回一个字符串
        System.out.println(builder.compact());
    }

    /***
     * 解析Jwt令牌数据
     */
    @Test
    public void testParseJwt(){
        String compactJwt="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE2MjEwNjQyNTAsImV4cCI6MTYyMTA2Nzg1MCwiYWRkcmVzcyI6IuWMl-S6rCIsIm1vbmV5IjozNTAwMCwiY29tcGFueSI6Ium7kemprCJ9.1rcNtjX5aCP1sj66YKQHwjyuJV1luqH7QXCPIIek1dw";
        Claims claims = Jwts.parser().
                setSigningKey("itcast").    //秘钥(盐)
                parseClaimsJws(compactJwt). //签证信息
                getBody();                  //解析后的信息
        System.out.println(claims);
    }

}
