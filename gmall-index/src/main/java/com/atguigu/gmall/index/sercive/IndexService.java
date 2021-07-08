package com.atguigu.gmall.index.sercive;


import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.index.fegin.GmallPmsClient;
import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import springfox.documentation.spring.web.json.Json;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class IndexService {

    @Autowired
    private GmallPmsClient pmsClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "index:cates";


    public List<PmsCategoryEntity> queryLv1Categories() {

        ResponseVo<List<PmsCategoryEntity>> listResponseVo = this.pmsClient.queryCategory(0L);

        return listResponseVo.getData();
    }

    public List<PmsCategoryEntity> queryLv2WithSubsById(Long pid) {
        //查询缓存
        String json = this.redisTemplate.opsForValue().get(KEY_PREFIX + pid);
        //判断是否为空 不为空则反序列化
        if (!StringUtils.isBlank(json)){
            return JSON.parseArray(json,PmsCategoryEntity.class);
        }

        ResponseVo<List<PmsCategoryEntity>> listResponseVo =this.pmsClient.queryLv2WithSubsByPid(pid);
        List<PmsCategoryEntity> categoryEntities = listResponseVo.getData();

        //查询后添加缓存

        if (CollectionUtils.isEmpty(categoryEntities)){
            //在一定程度上防止了 缓存穿透 （访问路径一样的高并发）
            this.redisTemplate.opsForValue().set(KEY_PREFIX + pid,JSON.toJSONString(categoryEntities),5, TimeUnit.MINUTES);
        }else {
            this.redisTemplate.opsForValue().set(KEY_PREFIX + pid,JSON.toJSONString(categoryEntities),180 + new Random().nextInt(10), TimeUnit.DAYS);

        }


        return categoryEntities;
    }

    public void testLock() {

        //添加分布式锁
        String uuid = UUID.randomUUID().toString();
        Boolean flag = this.redisTemplate.opsForValue().setIfAbsent("Lock", uuid,3, TimeUnit.SECONDS);
        if (!flag){
            try {
                Thread.sleep(100);
                //重试获取锁
                testLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }else {

            String numString = this.redisTemplate.opsForValue().get("num");
            if (StringUtils.isBlank(numString)){
                this.redisTemplate.opsForValue().set("num","1");
            }
            int num = Integer.parseInt(numString);
            this.redisTemplate.opsForValue().set("num", String.valueOf(++num));


            //删除锁
            String script = "if(redis.call('get', KEYS[1]) == ARGV[1]) then return redis.call('del', KEYS[1]) else return 0 end ";
            this.redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Arrays.asList("Lock"), uuid);



//            this.redisTemplate.delete("Lock");
        }


    }
}
