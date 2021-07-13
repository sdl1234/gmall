package com.atguigu.gmall.pms.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.PmsAttrGroupEntity;
import com.atguigu.gmall.pms.service.PmsAttrGroupService;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.pms.vo.PmsAttrGroupVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 属性分组
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
@Api(tags = "属性分组 管理")
@RestController
@RequestMapping("pms/attrgroup")
public class PmsAttrGroupController {

    @Autowired
    private PmsAttrGroupService pmsAttrGroupService;


    /**
     * 根据 cid spuId skuId 查询商品详情标题，以及商品详情数据
     * @param cid
     * @param spuId
     * @param skuId
     * @return
     */
    @GetMapping("with/attr/value/{cid}")
    public ResponseVo<List<ItemGroupVo>> queryGroupsWithAttrValuesByCidAndSpuIdAndSkuId(
            @PathVariable("cid")Long cid,
            @RequestParam("spuId")Long spuId,
            @RequestParam("skuId")Long skuId
    ){
        List<ItemGroupVo> groupVos = this.pmsAttrGroupService.queryGroupsWithAttrValuesByCidAndSpuIdAndSkuId(cid,spuId,skuId);
        return ResponseVo.ok(groupVos);
    }



    /**
     * 查询分类下的组及规格参数
     * @param catId 三级分类ID
     * @return 商品基本数据
     */
    @GetMapping("withattrs/{catId}")
    @ApiOperation("查询分类下的组及规格参数")
    public ResponseVo<List<PmsAttrGroupVo>> queryPmsAttrGroupByCatId(
            @PathVariable("catId")Long catId
    ){
         List<PmsAttrGroupVo> paramVoList =pmsAttrGroupService.queryPmsAttrGroupByCatId(catId);
         return ResponseVo.ok(paramVoList);
    }





    /**
     * 三级分类详情
     * @param cid   三级分类Id
     * @return  三级分类详情集合
     */
    @GetMapping("category/{cid}")
    @ApiOperation("查询三级分类的分组")
    public ResponseVo<List<PmsAttrGroupEntity>> queryPmsAttrGroupByCid(
            @PathVariable("cid") Long cid
    ){
        List<PmsAttrGroupEntity> pmsAttrGroupEntityList =this.pmsAttrGroupService.list(
          new QueryWrapper<PmsAttrGroupEntity>().eq("category_id",cid)
        );
        return ResponseVo.ok(pmsAttrGroupEntityList);
    }


    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryPmsAttrGroupByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = pmsAttrGroupService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsAttrGroupEntity> queryPmsAttrGroupById(@PathVariable("id") Long id){
		PmsAttrGroupEntity pmsAttrGroup = pmsAttrGroupService.getById(id);

        return ResponseVo.ok(pmsAttrGroup);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody PmsAttrGroupEntity pmsAttrGroup){
		pmsAttrGroupService.save(pmsAttrGroup);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody PmsAttrGroupEntity pmsAttrGroup){
		pmsAttrGroupService.updateById(pmsAttrGroup);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		pmsAttrGroupService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
