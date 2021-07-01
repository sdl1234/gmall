package com.atguigu.gmall.pms.controller;

import java.util.List;

import com.atguigu.gmall.pms.entity.PmsSkuAttrValueEntity;
import com.atguigu.gmall.pms.service.PmsSkuAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gmall.pms.entity.PmsSkuEntity;
import com.atguigu.gmall.pms.service.PmsSkuService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.bean.PageParamVo;

/**
 * sku信息
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
@Api(tags = "sku信息 管理")
@RestController
@RequestMapping("pms/sku")
public class PmsSkuController {

    @Autowired
    private PmsSkuService pmsSkuService;




    /**
     * 查询spu的所有sku信息
     * @param spuId 商品基本信息Id
     * @return 商品销售数据集合
     */
    @GetMapping("spu/{spuId}")
    @ApiOperation("查询spu的所有sku信息")
    public ResponseVo<List<PmsSkuEntity>> queryInfo(
            @PathVariable("spuId")Long  spuId
    ){
        List<PmsSkuEntity> pmsSkuEntityList = pmsSkuService.list(
                new QueryWrapper<PmsSkuEntity>()
                        .eq("spu_id",spuId)
        );
        return ResponseVo.ok(pmsSkuEntityList);
    }


    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryPmsSkuByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = pmsSkuService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsSkuEntity> queryPmsSkuById(@PathVariable("id") Long id){
		PmsSkuEntity pmsSku = pmsSkuService.getById(id);

        return ResponseVo.ok(pmsSku);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody PmsSkuEntity pmsSku){
		pmsSkuService.save(pmsSku);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody PmsSkuEntity pmsSku){
		pmsSkuService.updateById(pmsSku);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		pmsSkuService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
