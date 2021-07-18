package com.atguigu.gmall.auth.service;


import com.atguigu.gmall.auth.config.JwtProperties;
import com.atguigu.gmall.auth.fegign.GmallUmsClient;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.exception.UserException;
import com.atguigu.gmall.common.utils.CookieUtils;
import com.atguigu.gmall.common.utils.IpUtil;
import com.atguigu.gmall.common.utils.JwtUtils;
import com.atguigu.gmall.ums.entity.UmsUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private GmallUmsClient gmallUmsClient;

    @Autowired
    private JwtProperties jwtProperties;


    public void accredit(String loginName, String password, HttpServletRequest request, HttpServletResponse response) {


        try {
            //验证用户
            ResponseVo<UmsUserEntity> userEntityResponseVo = this.gmallUmsClient.queryUser(loginName, password);
            UmsUserEntity userEntity = userEntityResponseVo.getData();

            //判断是否存在
            if (userEntity == null) {
                throw new UserException("用户名或密码错误");
            }

            //添加荷载数据
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId",userEntity.getId());
            map.put("username",userEntity.getUsername());

            //加入用户ip
            String address = IpUtil.getIpAddressAtService(request);
            map.put("ip",address);

            //制作token
            String token = JwtUtils.generateToken(map, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());

            //将token放入cookie
            CookieUtils.setCookie(request,response,this.jwtProperties.getCookieName(),token,this.jwtProperties.getExpire() * 60);

            //用户昵称添加
            CookieUtils.setCookie(request,response,this.jwtProperties.getUnick(),userEntity.getNickname(),this.jwtProperties.getExpire() * 60);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("用户名或密码错误");
        }


    }
}
