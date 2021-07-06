package com.atguigu.gmall.search.pojo;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Data
@Document(indexName = "goods", type = "info", shards = 3,replicas = 2)
public class Goods {

    //搜索字段  商品销售信息Id 一级标题 二级标题 默认图片 价格
    @Id
    private Long skuId;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;
    @Field(type = FieldType.Keyword, index = false)
    private String defaultImage;
    @Field(type = FieldType.Double)
    private Double price;

    //排序筛选 销量 新品 是否有货 （综合 评论数 todo）
    /**
     * 销量 给个默认值 0 查修数据库后可以进行修改
     */
    @Field(type = FieldType.Long)
    private Long sales = 0L;

    /**
     * 新品 根据创建的时间进行排序
     */
    @Field(type = FieldType.Date)
    private Date createTime;

    /**
     * 是否有货 给默认值为 false 查询数据库后可以进行修改
     */
    @Field(type = FieldType.Boolean)
    private boolean store =false;

    //聚合字段 品牌id 品牌name 品牌logo 分类id 分类name 查询条件（未知长度的集合）
    @Field(type = FieldType.Long)
    private Long brandId;
    @Field(type = FieldType.Keyword)
    private String brandName;
    @Field(type = FieldType.Keyword)
    private String logo;

    @Field(type = FieldType.Long)
    private Long categoryId;
    @Field(type = FieldType.Keyword)
    private String categoryName;

    /**
     *查询条件 此处使用nested和type结合 表示嵌套结构
     */
    @Field(type = FieldType.Nested)
    private List<SearchAttrValue> searchAttrs;


}
