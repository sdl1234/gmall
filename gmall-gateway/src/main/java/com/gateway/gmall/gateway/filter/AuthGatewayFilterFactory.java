package com.gateway.gmall.gateway.filter;


import com.atguigu.gmall.common.utils.IpUtil;
import com.atguigu.gmall.common.utils.JwtUtils;
import com.gateway.gmall.gateway.config.JwtProperties;
import com.google.common.net.HttpHeaders;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class AuthGatewayFilterFactory extends
        AbstractGatewayFilterFactory<AuthGatewayFilterFactory.PathConfig> {

    @Autowired
    private JwtProperties  jwtProperties;

    /**
     * 告诉父类自己指定了配置
     */
    public AuthGatewayFilterFactory() {
        super(PathConfig.class);
    }

    @Override
    public GatewayFilter apply(PathConfig config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                System.out.println("自定义过滤器!" + config);

                //获取请求和返回
                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse response = exchange.getResponse();

                //判断请求路径在不在名单中 不在则放行
                String path = request.getURI().getPath();
                if (config.authPaths.stream().allMatch(authPath -> path.indexOf(authPath) == -1)){

                    return chain.filter(exchange);
                }

                //获取token值 同步请求 cookiez中获取 异步请求 头信息中获取
                String token = request.getHeaders().getFirst("token");
                if (StringUtils.isBlank(token)){
                    //头信息中没有 则从cookie中获取
                    MultiValueMap<String, HttpCookie> cookies = request.getCookies();
                    HttpCookie cookiesFirst = cookies.getFirst(jwtProperties.getCookieName());
                    token = cookiesFirst.getValue();
                }
                if (StringUtils.isBlank(token)){
                    //转到登录页面 SEE_OTHER（查看其它-重定向）
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    //设置请求路径信息 LOCATION(位置，地点，路径)
                    response.getHeaders().set(HttpHeaders.LOCATION,"http://sso.gmall.com/toLogin.html?returnUrl=" + request.getURI());
                    return response.setComplete();
                }

                //解析jwt 有异常重定向到登陆页面
                try {
                    Map<String, Object> map = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());

                    //对比ip 不对就返回登录页
                    String ip = map.get("ip").toString();
                    String ipAddressAtGateway = IpUtil.getIpAddressAtGateway(request);
                    if (!StringUtils.equals(ip,ipAddressAtGateway)){
                        //转到登录页面 SEE_OTHER（查看其它-重定向）
                        response.setStatusCode(HttpStatus.SEE_OTHER);
                        //设置请求路径信息 LOCATION(位置，地点，路径)
                        response.getHeaders().set(HttpHeaders.LOCATION,"http://sso.gmall.com/toLogin.html?returnUrl=" + request.getURI());
                        return response.setComplete();
                    }


                    //登录后在过期时间内，将此登录信息发给后续服务
                    request.mutate()
                            .header("userId",map.get("userId").toString())
                            .header("username",map.get("username").toString())
                            .build();
                    //将request转换成exchange
                    exchange.mutate().request(request).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    //转到登录页面 SEE_OTHER（查看其它-重定向）
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    //设置请求路径信息 LOCATION(位置，地点，路径)
                    response.getHeaders().set(HttpHeaders.LOCATION,"http://sso.gmall.com/toLogin.html?returnUrl=" + request.getURI());
                    return response.setComplete();
                }


                return chain.filter(exchange);
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("authPaths");
    }

    @Override
    public ShortcutType shortcutType() {
        return ShortcutType.GATHER_LIST;
    }

    @Data
    public static class PathConfig {
        private List<String> authPaths;
    }
}
