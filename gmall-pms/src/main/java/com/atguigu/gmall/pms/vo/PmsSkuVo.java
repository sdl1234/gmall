package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.PmsSkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.PmsSkuEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PmsSkuVo extends PmsSkuEntity {

    /**
     * sku图片
     */
    private List<String> images;

    /**
     * growBounds 成长积分
     * buyBounds 购物积分
     * work 优惠生效情况[1111（四个状态位，从右到左）;
     *  0 - 无优惠，成长积分是否赠送;
     *  1 - 无优惠，购物积分是否赠送;
     *  2 - 有优惠，成长积分是否赠送;
     *  3 - 有优惠，购物积分是否赠送【状态位0：不赠送，1：赠送】]
     */
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    private List<Integer> work;


    /**
     * fullPrice 满多少
     * reducePrice 减多少
     * fullAddOther Integer
     */
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;

    /**
     * fullCount 满几件
     * discount 打几折
     * ladderAddOther 是否叠加其他优惠[0-不可叠加，1-可叠加]
     */
    private Integer fullCount;
    private BigDecimal discount;
    private Integer ladderAddOther;

    private List<PmsSkuAttrValueEntity> saleAttrs;
}
