package com.atguigu.gmall.pms.vo;

import lombok.Data;

import java.util.Set;

@Data
public class SaleAttrValueVo {

    private Long attrId;
    private String attrName;
    /**
     * 使用set set中无序 且无相同的数据
     */
    private Set<String> attrValues;
}
