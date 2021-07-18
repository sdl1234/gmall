package com.gateway.gmall.gateway.config;


import com.atguigu.gmall.common.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Slf4j
@ConfigurationProperties(prefix = "auth.jwt")
@Repository
public class JwtProperties {

    private String pubKeyPath;
    private String secret;
    private String cookieName;
    private Integer expire;
    private String unick;

    private PublicKey publicKey;
    private PrivateKey privateKey;


    @PostConstruct
    public void init(){
        try {
            //获取公钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("公钥获取失败！");
            throw new RuntimeException();
        }
    }

}
