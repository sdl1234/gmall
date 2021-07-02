package com.atguigu.gmall.search.pojo;


import com.atguigu.gmall.pms.entity.PmsBrandEntity;
import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponseVo {
    // 过滤
    private List<PmsBrandEntity> brands;
    private List<PmsCategoryEntity> categories;
    // 规格：[{attrId: 8, attrName: "内存", attrValues: ["8G", "12G"]}, {attrId: 9, attrName: "机身存储", attrValues: ["128G", "256G"]}]
    private List<SearchResponseAttrVo> filters;

    // 分页
    private Integer pageNum;
    private Integer pageSize;
    private Long total;

    // 当前页数据
    private List<Goods> goodsList;
}
