package com.atguigu.gmall.index.config;


import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.index.fegin.GmallPmsClient;
import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Configuration
public class BloomFilterConfig {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private GmallPmsClient pmsClient;

    private static final String KEY_PREFIX = "index:cates";

    @Bean
    public RBloomFilter bloomFilter(){
        RBloomFilter<Object> bloomFilter = this.redissonClient.getBloomFilter("index:bloom:filter");
        bloomFilter.tryInit(1000,0.03);

        ResponseVo<List<PmsCategoryEntity>> responseVo = this.pmsClient.queryCategory(0L);
        List<PmsCategoryEntity> categoryEntities = responseVo.getData();
        if (!CollectionUtils.isEmpty(categoryEntities)){
            categoryEntities.forEach(pmsCategoryEntity -> {
                bloomFilter.add(KEY_PREFIX + "[" + pmsCategoryEntity.getId() + "]");
            });
        }

        return bloomFilter;
    }
}
