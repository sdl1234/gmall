package com.atguigu.gmall.elasticsearch.demo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "goods", type = "info", shards = 3,replicas = 2)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private Long id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Keyword,index = false)
    private String images;
    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Boolean)
    private Boolean saleable;
    @Field(type = FieldType.Long)
    private Long stock;
    @Field(type = FieldType.Keyword)
    private String brand;
    @Field(type = FieldType.Keyword)
    private String category;
}
