package com.atguigu.gmall.search.pojo;


import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.util.List;


/**
 * 接受页面传递过来的检索参数
 * search?keyword=小米&brandId=1,3&cid=225&props=5:高通-麒麟&props=6:骁龙865-硅谷1000&sort=1&priceFrom=1000&priceTo=6000&pageNum=1&store=true
 *  搜索-》小米-》品牌 1，3-》分类 225 -》机身存储 高通-麒麟-》
 *  cpu品牌 晓龙865-硅谷1000-》排序 1 -》价格区间 1000-6000 -》
 *  分页 1 -》是否有库存 true
 *
 *  search?keyword=手机&brandId=1,3&cid=225&props=6:高通-麒麟&sort=1&priceFrom=1000&priceTo=6000&pageNum=1&store=true
 */


@Data
public class SearchParamVo {

    //搜索条件
    private String keyword;

    //品牌 集合
    private List<Long> brandId;

    //分类
    private List<Long> cid;

    //查询参数
    private List<String> props;

    /**
     * 排序方式
     * 0为默认 按照得分降序排序
     * 1 按照价格升序
     * 2 按照价格降序
     * 3 按照创建时间按降序
     * 4 按照销售量降序排序
     */
    private Integer sort = 0 ;

    /**
     * priceFrom 起始值
     * priceTo 结束值
     */
    private Double priceFrom;
    private Double priceTo;

    /**
     * pageNum 页码 给初始值1
     * pageSize 每页条数 给默认值20（不可更改 final）
     */
    private Integer pageNum =1 ;
    private final Integer pageSize = 20;

    //是否有库存
    private Boolean store;

}
