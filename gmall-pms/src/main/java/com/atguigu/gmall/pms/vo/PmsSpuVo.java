package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.PmsSpuEntity;
import lombok.Data;

import java.util.List;
@Data
public class PmsSpuVo extends PmsSpuEntity {

    private List<ProductAttrValueVo> baseAttrs;
    private List<PmsSkuVo> skus;
    private List<String> spuImages;


}
