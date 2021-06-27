package com.changgou.token;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/*****
 * @Author: www.itheima
 * @Date: 2019/7/7 13:48
 * @Description: com.changgou.token
 *  使用公钥解密令牌数据
 ****/
public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiaXRoZWltYSIsImlkIjoiMSIsImF1dGhvcml0aWVzIjpbImFkbWluIiwib2F1dGgiXX0.SDT62YJXN0o5e3xZ9ZdcgzzDyhXPHsBQxlDLaQwn8NTI-KMNovBVMyxqYogJy_BZ1hQfwT18HVTvaLo6yhxlh2VbxqI5K9IqJ26M4AeiDKTEg9GcnJKTFiHF4xz0COZ80TyXcTcxw22ikYkAT2G6rbaU32TfzYUpf13UbyxYuy60n_iHSE0dm_hJEKmX13Z7h6rta7bn2tadEDyOvoJ9jWIjXK5yjbetOQu_JXNuUstBQ2w12lYqwj5RT31ZcoqlRIseC9hPk8u8ygSBIEabVKtUiazaShE3H9MP83z0b47CemhkRarvZJl3oPqHyMbvQCAKq6iL3uv-0W-2RxLOXg";

        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAioH4ejxvYfoy5cvKCoonXFTKWU/dsPw2tuJQWjaW3m3OYzNSC2rxwQr+Fura00iStpGCBrs1piPNW7QVY6yyckjW9WkbVC/x9L/fGJTyGAjjw4nEr6wJOMdInME868jOD2DOQT2MTqWGypYOZbu3Q5zejRrz7WeFa/o+quCos9vJ3jcnhyWznausSnuXhmtaTwlIa9ia1SWghHaANGS1H68bf9uOtqA+80qbCHcDXjdRtZRXxhJ3jnCDbv9Vwqv+t67OroHaX8ErqE5foMGP5hcKQ52rmOyj8ijV/k/65zkvPNhf4eIcSC9z7VrhuJY30JjDxnW+MUqRdbesVS8WzwIDAQAB-----END PUBLIC KEY-----";
        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容 载荷
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
