package com.atguigu.gmall.auth;

import com.atguigu.gmall.common.utils.JwtUtils;
import com.atguigu.gmall.common.utils.RsaUtils;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    private static final String pubKeyPath = "D:\\atguigu\\rsa\\rsa.pub";
    private static final String priKeyPath = "D:\\atguigu\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        //生成公钥私钥  （路径，路径，盐）
        RsaUtils.generateKey(pubKeyPath, priKeyPath,"234" );
    }

    @BeforeEach
    //在运行之前读取公钥私钥
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }


    @Test
    //添加测试用户
    public void testGenerateToken() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        Map<String, Object> map =new HashMap<>();
        map.put("id", "11");
        map.put("username", "liuyan");
        //生成token  （用户数据， 私钥， 授权时间）
        String token =JwtUtils.generateToken(map,privateKey, 2);
        System.out.println("token = " + token);

    }


    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6IjExIiwidXNlcm5hbWUiOiJsaXV5YW4iLCJleHAiOjE2MjYzOTgyMTF9.CDzw0q77-a14mX9HdgvtV1jtkgEtI66wJqcLLhBP1H59X2D5-9IXI1HAYG51HALSYdohbQHuDfyWoKO0CeIDPQTSwUScKehLri1Yzjf5-Li-OMJaX3Jebx7s8sa82GfDB5640VpGnR9FAE1SmlxaKeKHH8S8faKoVsWxFmj_7OcOFkmo9aaDeeYTHAWHlZCsDYVD0ooZJ1sY3HYxfDLh68c7DLGyTS_q4xulfd0TRZa5F3cvQqaWNwerpJjQ4-fTsgdFGnp0e-lg-uW2ue4V9bpc_j_MK44GIXAublyQ6lEY2qOt7Q78JmF5PEFfldrxBXVI2Gzi5G9aDqkMSZPyWw";
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        // 解析token
        Map<String, Object> map = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + map.get("id"));
        System.out.println("userName: " + map.get("username"));



    }


}
