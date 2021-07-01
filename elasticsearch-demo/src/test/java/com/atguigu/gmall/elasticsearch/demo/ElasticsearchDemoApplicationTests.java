package com.atguigu.gmall.elasticsearch.demo;

import com.atguigu.gmall.elasticsearch.demo.impl.UserRepository;
import com.atguigu.gmall.elasticsearch.demo.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.util.JacksonFeature;
import com.fasterxml.jackson.annotation.JsonKey;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class ElasticsearchDemoApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test

    void testRestClient() throws IOException {
        SearchSourceBuilder sourceBuilder =new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder =QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("title","小米手机").operator(Operator.OR));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(2000d).lte(4000d));
        sourceBuilder.query(boolQueryBuilder);
        //排序
        sourceBuilder.sort("price",SortOrder.DESC);
        //分页
        sourceBuilder.from(0);
        sourceBuilder.size(2);
        //高亮
        sourceBuilder.highlighter(new HighlightBuilder().field("title").preTags("<em>").postTags("</em>"));
        //结果过滤
//        sourceBuilder.fetchSource();
        //聚合
        sourceBuilder.aggregation(AggregationBuilders.terms("brandAgg").field("brand"));


        System.out.println(sourceBuilder);
        SearchRequest searchRequest = new SearchRequest(new String[]{"goods"},sourceBuilder);
        SearchResponse response = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response);



        //获取查询结果集
        SearchHits hits = response.getHits();
        System.out.println("总记录数：" + hits.getTotalHits());
        SearchHit[] hitsHits = hits.getHits();
        Arrays.stream(hitsHits).map(hitsHit ->{
            String json = hitsHit.getSourceAsString();
            try {
                Map<String, HighlightField> highlightFields = hitsHit.getHighlightFields();
                HighlightField title = highlightFields.get("title");
                Text fragment = title.fragments()[0];
                User user = MAPPER.readValue(json,User.class);
                user.setTitle(fragment.toString());
                return user;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()).forEach(System.out::println);

        Aggregations aggregations = response.getAggregations();
        ParsedStringTerms brandAgg = (ParsedStringTerms)aggregations.get("brandAgg");
        brandAgg.getBuckets().forEach(bucket ->{
            System.out.println("===================================");
            System.out.println(bucket.getKeyAsString());
            System.out.println(bucket.getDocCount());
        });

    }


    @Test
    void testNative(){
        NativeSearchQueryBuilder queryBuilder =new NativeSearchQueryBuilder();
        //条件查询 使用QueryBuilders的 matchQuery方法 并使用.operator(Operator.or)使or来拼接搜索的字符
        queryBuilder.withQuery(QueryBuilders.matchQuery("title","小米手机").operator(Operator.OR));
        //添加过滤条件 QueryBuilders.rangeQuery
        queryBuilder.withFilter(QueryBuilders.rangeQuery("price").gte(2000d).lte(4000d));
        //添加排序
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        //分页
        queryBuilder.withPageable(PageRequest.of(0,2));

        //高亮 测试发现此方法当前并无作用
//        queryBuilder.withHighlightBuilder(new HighlightBuilder().field("title").preTags("<em>").postTags("</em>"));

        //结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","title","images"},null));

        //聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brandAgg").field("brand"));


        AggregatedPage<User> page = (AggregatedPage<User>) this.userRepository.search(queryBuilder.build());
        System.out.println("一共命中了： " + page.getTotalElements());
        System.out.println("页数：" + page.getTotalPages());
        page.forEach(System.out::println);
        //输出聚合信息 将数据结果的返回值类型更改为Page中的方法接口AggregatePage
        ParsedStringTerms brandAgg = (ParsedStringTerms)page.getAggregation("brandAgg");
        List<? extends Terms.Bucket> buckets = brandAgg.getBuckets();
        buckets.forEach(bucket ->{
            System.out.println("==========================");
            System.out.println(bucket.getKey());
            System.out.println(bucket.getDocCount());

        });
    }


    @Test
    void contextLoads() {
//        this.restTemplate.createIndex(User.class);
//        this.restTemplate.putMapping(User.class);
        System.out.println("=================================");

        this.userRepository.save(new User(21L,"黑莓手机","htt[://jd.com.123456",2000d,true,100L,"黑莓","手机"));
        this.userRepository.save(new User(31L,"华为手机","htt[://jd.com.123456",3001d,true,101L,"华为","手机"));
        this.userRepository.save(new User(41L,"华为电脑","htt[://jd.com.123456",4002d,true,102L,"华为","电脑"));
        this.userRepository.save(new User(51L,"华为电视","htt[://jd.com.123456",5003d,true,103L,"华为","电视"));
        this.userRepository.save(new User(61L,"小米手机","htt[://jd.com.123456",6004d,true,104L,"小米","手机"));
        this.userRepository.save(new User(71L,"小米电视","htt[://jd.com.123456",3005d,true,105L,"小米","电脑"));
        this.userRepository.save(new User(81L,"小米电脑","htt[://jd.com.123456",5006d,true,106L,"小米","电视"));
        this.userRepository.save(new User(91L,"apple手机","htt[://jd.com.123456",9007d,true,107L,"apple","手机"));

        this.userRepository.findByTitleLike("小米").forEach(System.out::println);
        System.out.println("-===============================================-");
        this.userRepository.findByPriceBetween(2000d,10000d).forEach(System.out::println);
    }

}
