package com.atguigu.gmall.search.service;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.pms.entity.PmsBrandEntity;
import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import com.atguigu.gmall.search.pojo.Goods;
import com.atguigu.gmall.search.pojo.SearchParamVo;
import com.atguigu.gmall.search.pojo.SearchResponseAttrVo;
import com.atguigu.gmall.search.pojo.SearchResponseVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public SearchResponseVo  search(SearchParamVo paramVo) {

        try {
            SearchRequest searchRequest =new SearchRequest(
                    new String[]{"goods"}, Objects.requireNonNull(buildDsl(paramVo)));
            SearchResponse searchResponse = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
             SearchResponseVo responseVo = this.parseResult(searchResponse);
             responseVo.setPageNum(paramVo.getPageNum());
             responseVo.setPageSize(paramVo.getPageSize());
             return  responseVo;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private SearchResponseVo parseResult(SearchResponse response) {
        SearchResponseVo responseVo =new SearchResponseVo();

        SearchHits hits = response.getHits();
        //总命中的记录数
        responseVo.setTotal(hits.getTotalHits());

        SearchHit[] hitsHits = hits.getHits();
         List<Goods> goodsList =Stream.of(hitsHits).map(hitsHit ->{

             //获取hitsHits中的Goods数据
             String goodJson = hitsHit.getSourceAsString();
             //反序列化json
             Goods goods = JSON.parseObject(goodJson,Goods.class);

             //获取高亮的title覆盖普通title
             Map<String, HighlightField> highlightFields = hitsHit.getHighlightFields();
             HighlightField highlightField = highlightFields.get("title");
             String highlightTitle = highlightField.getFragments()[0].toString();
             goods.setTitle(highlightTitle);
            return goods;
         }).collect(Collectors.toList());
        responseVo.setGoodsList(goodsList);

        //聚合结果解析
        Map<String, Aggregation> aggregationMap = response.getAggregations().asMap();
        ParsedLongTerms brandIdAgg = (ParsedLongTerms) aggregationMap.get("brandIdAgg");
        List<? extends Terms.Bucket> buckets = brandIdAgg.getBuckets();
        if (!CollectionUtils.isEmpty(buckets)){
            List<PmsBrandEntity> brands = buckets.stream().map(bucket ->{
                PmsBrandEntity brandEntity =new PmsBrandEntity();

                long brandId = ((Terms.Bucket) bucket).getKeyAsNumber().longValue();
                brandEntity.setId(brandId);
                Map<String, Aggregation> brandAggregationMap = ((Terms.Bucket) bucket).getAggregations().asMap();
                ParsedStringTerms brandNameAgg = (ParsedStringTerms) brandAggregationMap.get("brandNameAgg");
                brandEntity.setName(brandNameAgg.getBuckets().get(0).getKeyAsString());

                //解析品牌logo的子聚合，获取品牌的logo
                ParsedStringTerms logoAgg = (ParsedStringTerms) brandAggregationMap.get("logoAgg");
                List<? extends Terms.Bucket> logoAggBuckets = logoAgg.getBuckets();
                if (!CollectionUtils.isEmpty(logoAggBuckets)){
                    brandEntity.setLogo(logoAggBuckets.get(0).getKeyAsString());
                }
                return brandEntity;
            }).collect(Collectors.toList());
            responseVo.setBrands(brands);
        }

        //2.解析聚合结果，获取分类
        ParsedLongTerms categoryIdAgg = (ParsedLongTerms) aggregationMap.get("categoryIdAgg");
        List<? extends Terms.Bucket> categoryIdAggBuckets = categoryIdAgg.getBuckets();
        if (!CollectionUtils.isEmpty(categoryIdAggBuckets)){
              List<PmsCategoryEntity> categories  = categoryIdAggBuckets.stream().map(bucket ->{
                 PmsCategoryEntity categoryEntity =new PmsCategoryEntity();
                 //获取bucket的key.key是分类的Id
                 long categoryId = ((Terms.Bucket) bucket).getKeyAsNumber().longValue();
                 categoryEntity.setId(categoryId);
                 //解析分类名称的子聚合，获取分类名称
                 ParsedStringTerms categoryNameAgg = (ParsedStringTerms)((Terms.Bucket) bucket).getAggregations().get("categoryNameAgg");
                 categoryEntity.setName(categoryNameAgg.getBuckets().get(0).getKeyAsString());
                 return categoryEntity;

             }).collect(Collectors.toList());
             responseVo.setCategories(categories);
        }

        //3.解析聚合结果集，获取规格参数
        ParsedNested attrAge = (ParsedNested) aggregationMap.get("attrAgg");
        ParsedLongTerms attrIdAgg = (ParsedLongTerms)attrAge.getAggregations().get("attrIdAgg");
        List<? extends Terms.Bucket> attrIdAggBuckets = attrIdAgg.getBuckets();
        if (!CollectionUtils.isEmpty(attrIdAggBuckets)){
            List<SearchResponseAttrVo> filters = attrIdAggBuckets.stream().map(bucket ->{
                SearchResponseAttrVo responseAttrVo =new SearchResponseAttrVo();
                //规格参数id
                responseAttrVo.setAttrId(((Terms.Bucket) bucket).getKeyAsNumber().longValue());

                Aggregations aggs = ((Terms.Bucket) bucket).getAggregations();
                //规格参数的名称
                ParsedStringTerms attrNameAgg = aggs.get("attrNameAgg");
                responseAttrVo.setAttrName(attrNameAgg.getBuckets().get(0).getKeyAsString());
                //规格参数值
                ParsedStringTerms attrValueAgg = aggs.get("attrValueAgg");
                List<? extends Terms.Bucket> attrValueAggBuckets = attrValueAgg.getBuckets();
                if (!CollectionUtils.isEmpty(attrValueAggBuckets)){
                    List<String> attrValues =attrValueAggBuckets.stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
                    responseAttrVo.setAttrValues(attrValues);
                }
                return responseAttrVo;
            }).collect(Collectors.toList());
            responseVo.setFilters(filters);
        }
        return responseVo;

    }

    //创建dsl语句
    private SearchSourceBuilder buildDsl(SearchParamVo paramVo) {

        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();

        String keyword = paramVo.getKeyword();
        if (StringUtils.isBlank(keyword)){
            //TODO:广告
            return null;
        }

        //1.构建查询条件（bool查询）
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            //1.1 匹配查询
            boolQueryBuilder.must(QueryBuilders.matchQuery("title",keyword)
                .operator(Operator.AND));
            //1.2过滤

                //1.2.1 品牌过滤
                List<Long> brandId = paramVo.getBrandId();
                if (!CollectionUtils.isEmpty(brandId)){
                    //过滤
                    boolQueryBuilder.filter(QueryBuilders.termsQuery(
                            "brandId",brandId));
                }

                //1.2.2 分类过滤
                List<Long> cid = paramVo.getCid();
                if (cid != null){
                    boolQueryBuilder.filter(QueryBuilders.termsQuery(
                            "categoryId",cid));
                }

                //1.2.3 价格区间过滤
                Double priceFrom = paramVo.getPriceFrom();
                Double priceTo = paramVo.getPriceTo();
                if (priceFrom != null || priceTo != null){
                    RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price");
                    if (priceFrom !=null){
                        rangeQuery.gte(priceFrom);
                    }
                    if (priceTo != null){
                        rangeQuery.lte(priceTo);
                    }
                    boolQueryBuilder.filter(rangeQuery);
                }

                //1.2.4 是否有货
                Boolean store = paramVo.getStore();
                if (store != null){
                    boolQueryBuilder.filter(QueryBuilders.termQuery(
                            "store",store
                    ));
                }

                //1.2.5 规格参数过滤 props =5 , =6
                List<String> props = paramVo.getProps();
                if (!CollectionUtils.isEmpty(props)){
                    props.forEach(prop ->{
                        String[] attrs = StringUtils.split(prop, ":");
                        if (attrs !=null && attrs.length== 2){
                            String attrId =attrs[0];
                            String attrValueString = attrs[1];
                            String[] attrValues = StringUtils.split(attrValueString, "-");
                            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                            boolQuery.must(QueryBuilders.termQuery(
                                    "searchAttrs.attrId",attrId
                            ));
                            boolQuery.must(QueryBuilders.termQuery(
                                    "searchAttrs.attrValue",attrValues
                            ));
                            boolQuery.filter(QueryBuilders.nestedQuery(
                                    "searchAttrs",boolQuery, ScoreMode.None
                            ));
                        }
                    });
                }
                searchSourceBuilder.query(boolQueryBuilder);

        //2.构建排序
        Integer sort = paramVo.getSort();
        String field = "";
        org.elasticsearch.search.sort.SortOrder order =null;
        switch (sort){
            case 1: field = "price" ; order = SortOrder.ASC;
                break;
            case 2: field = "price" ; order = SortOrder.DESC;
                break;
            case 3: field = "createTime" ; order = SortOrder.DESC;
                break;
            case 4: field = "sales" ; order = SortOrder.DESC;
                break;
            default: field = "_score" ; order = SortOrder.DESC;
                break;
        }
        searchSourceBuilder.sort(field,order);

        //3.分页
        Integer pageNum = paramVo.getPageNum();
        Integer pageSize = paramVo.getPageSize();
        searchSourceBuilder.from((pageNum -1 ) * pageSize);
        searchSourceBuilder.size(pageSize);

        //4.高亮
        searchSourceBuilder.highlighter(new HighlightBuilder()
                    .field("title").preTags("<font style='color:red'>")
                    .postTags("</font>"));

        //5.聚合
            //5.1 品牌聚合
            searchSourceBuilder.aggregation(
                    AggregationBuilders.terms("brandIdAgg").field("brandId")
                    .subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName"))
                    .subAggregation(AggregationBuilders.terms("logoAgg").field("logo"))
            );

            //5.2 分类聚合
            searchSourceBuilder.aggregation(AggregationBuilders.terms("categoryIdAgg")
                        .field("categoryId")
            .subAggregation(AggregationBuilders.terms("categoryNameAgg")
                        .field("categoryName")));

            //5.3 规格参数聚合
            searchSourceBuilder.aggregation(AggregationBuilders
                    .nested("attrAgg","searchAttrs")
            .subAggregation(AggregationBuilders.terms("attrIdAgg")
                    .field("searchAttrs.attrId")
            .subAggregation(AggregationBuilders.terms("attrNameAgg")
                    .field("searchAttrs.attrName"))
            .subAggregation(AggregationBuilders.terms("attrValueAgg")
                    .field("searchAttrs.attrValue"))));

        //6. 结果集过滤(添加的为保存的字符！！！！！！！！！！！！！！！)
        searchSourceBuilder.fetchSource(new String[]{
                "skuId","title","price","defaultImage","subTitle"
        },null);

        System.out.println(searchSourceBuilder.toString());
        return searchSourceBuilder;

    }
}
