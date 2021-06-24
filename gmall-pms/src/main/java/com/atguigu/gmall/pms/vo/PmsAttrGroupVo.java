package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.PmsAttrEntity;
import com.atguigu.gmall.pms.entity.PmsAttrGroupEntity;
import lombok.Data;

import java.util.List;

@Data
public class PmsAttrGroupVo extends PmsAttrGroupEntity {

    private List<PmsAttrEntity> attrEntities;
}
