package com.atguigu.gmall.scheduled.jobHadler;


import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.scheduled.bean.Cart;
import com.atguigu.gmall.scheduled.mapper.CartMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class CartJobHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CartMapper cartMapper;

    private static final String KEY = "cart:async:exception";
    private static final String KEY_PREFIX = "cart:info:";


    @XxlJob("cartJobHandler")
    public ReturnT<String> executor(String param){
        BoundListOperations<String, String> listOps = this.redisTemplate.boundListOps(KEY);

        //如果redis中没有异常用户
        if (listOps.size() == 0 ){
            return ReturnT.SUCCESS;
        }

        //获取第一个失败用户
        String userId = listOps.rightPop();
        while (StringUtils.isNotEmpty(userId)){
            //先删除
            this.cartMapper.delete(new QueryWrapper<Cart>()
                    .eq("user_id",userId));

            //查看该用户redis中的购物车
            BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(KEY_PREFIX + userId);
            List<Object> cartJsons = hashOps.values();
            //为空 进入下次循环
            if (CollectionUtils.isEmpty(cartJsons)){
                continue;
            }

            //不为空
            cartJsons.forEach(cartJson ->{
                this.cartMapper.insert(JSON.parseObject(cartJson.toString(),Cart.class));
            });

            //下个数据
            userId = listOps.rightPop();

        }
        return ReturnT.SUCCESS;
    }

}





































