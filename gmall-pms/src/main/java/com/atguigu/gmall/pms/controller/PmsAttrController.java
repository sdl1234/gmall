package com.atguigu.gmall.pms.controller;

import java.util.List;

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

import com.atguigu.gmall.pms.entity.PmsAttrEntity;
import com.atguigu.gmall.pms.service.PmsAttrService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.bean.PageParamVo;

/**
 * 商品属性
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
@Api(tags = "商品属性 管理")
@RestController
@RequestMapping("pms/attr")
public class PmsAttrController {

    @Autowired
    private PmsAttrService pmsAttrService;


    /**
     * 查询分类下的规格参数
     * @param cid 分类Id
     * @param type 是否基本属性
     * @param searchType 是否搜索属性
     * @return 销售属性集合
     */
    @GetMapping("category/{cid}")
    @ApiOperation(" 查询分类下的规格参数")
    public ResponseVo<List<PmsAttrEntity>> queryPmsAttrEntityByCid(
            @PathVariable("cid")Long cid,
            @RequestParam(value = "type",required = false)Integer type,
            @RequestParam(value = "searchType",required = false)Integer searchType
    ){
        List<PmsAttrEntity> pmsAttrEntityList =this.pmsAttrService
                .queryPmsAttrEntityByCid(cid,type,searchType);
        return ResponseVo.ok(pmsAttrEntityList);
    }



    /**
     * 查询规格参数
     * @param gid   组Id
     * @return  组参数集合
     */
    @GetMapping("group/{gid}")
    @ApiOperation("查询组下的规格参数")
    public ResponseVo<List<PmsAttrEntity>> queryPmsAttrEntityByGid(
            @PathVariable("gid")Long gid
    ){
        List<PmsAttrEntity> pmsAttrEntityList = this.pmsAttrService.list(
                new QueryWrapper<PmsAttrEntity>().eq("group_id",gid)
        );
        return ResponseVo.ok(pmsAttrEntityList);
    }




    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryPmsAttrByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = pmsAttrService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsAttrEntity> queryPmsAttrById(@PathVariable("id") Long id){
		PmsAttrEntity pmsAttr = pmsAttrService.getById(id);

        return ResponseVo.ok(pmsAttr);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody PmsAttrEntity pmsAttr){
		pmsAttrService.save(pmsAttr);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody PmsAttrEntity pmsAttr){
		pmsAttrService.updateById(pmsAttr);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		pmsAttrService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
