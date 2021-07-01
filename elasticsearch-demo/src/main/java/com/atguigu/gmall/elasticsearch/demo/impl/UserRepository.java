package com.atguigu.gmall.elasticsearch.demo.impl;

import com.atguigu.gmall.elasticsearch.demo.user.User;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserRepository extends ElasticsearchRepository<User,Long> {

    List<User> findByPriceBetween(Double price1, Double price2);
    List<User> findByTitleLike(String title);

}
