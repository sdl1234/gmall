package com.atguigu.gmall.cart.interceptor;

import com.atguigu.gmall.cart.config.JwtProperties;
import com.atguigu.gmall.cart.entity.UserInfo;
import com.atguigu.gmall.common.utils.CookieUtils;
import com.atguigu.gmall.common.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@Component
@Repository
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;


    //定义新线程(局部变量)
    private static final ThreadLocal<UserInfo> THREAD_LOCAL =new ThreadLocal<>();


    @Override
    //前置方法
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserInfo userInfo =new UserInfo();

        //获取登录头信息（userKey）
        String userKey = CookieUtils.getCookieValue(request, jwtProperties.getUserKey());
        if (StringUtils.isEmpty(userKey)) {
             userKey = UUID.randomUUID().toString();
             CookieUtils.setCookie(request,response,jwtProperties.getUserKey(),userKey,jwtProperties.getExpireTime());
        }
        userInfo.setUserKey(userKey);

        //获取登录的头信息 （token）
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        if (StringUtils.isNotBlank(token)){
            try {
                //解析jwt
                Map<String, Object> map = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
                //获取token解析后的map中的userId
                Long userId = Long.valueOf(map.get("userId").toString());
                userInfo.setUserId(userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //放入线程
        THREAD_LOCAL.set(userInfo);
        return true;
    }


    //创建对线程的get方法
    public static UserInfo getUserInfo(){
        return THREAD_LOCAL.get();
    }


    /**
     * 释放线程资源
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //释放此线程，让线程回归线程池
        THREAD_LOCAL.remove();
    }
}
