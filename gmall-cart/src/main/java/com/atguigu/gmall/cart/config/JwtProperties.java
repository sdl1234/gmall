package com.atguigu.gmall.cart.config;

import com.atguigu.gmall.common.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PublicKey;

@Data
@Slf4j
@ConfigurationProperties(prefix = "auth.jwt")
@Repository
public class JwtProperties {

    private String pubKeyPath;
    private String cookieName;
    private Integer expireTime;
    private String userKey;

    private PublicKey publicKey;


    @PostConstruct
    public void init(){
        try {
            //获取公私钥
            File pubFile = new File(pubKeyPath);

            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("生成公钥和私钥出错");
            e.printStackTrace();
        }
    }

}
