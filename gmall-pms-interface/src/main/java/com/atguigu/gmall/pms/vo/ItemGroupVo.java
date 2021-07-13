package com.atguigu.gmall.pms.vo;

import lombok.Data;

import java.util.List;

@Data
public class ItemGroupVo {

    /**
     *基本类型名称
     */
    private String groupName;


    /**
     *每个基本类型的数据
     */
    private List<AttrValueVo> attrValues;
}
