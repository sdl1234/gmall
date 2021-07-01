package com.atguigu.gmall.wms.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.wms.entity.WmsWareSkuEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface GmallWmsApi {
    /**
     * 获取某个sku的库存信息
     * @param skuId 商品销售信息Id
     * @return 商品库存
     */
    @GetMapping("wms/waresku/sku/{skuId}")
    @ApiOperation(" 获取某个sku的库存信息")
    public ResponseVo<List<WmsWareSkuEntity>> queryWmsWareSkuListBySkuId(
            @PathVariable("skuId")Long skuId
    );

}
