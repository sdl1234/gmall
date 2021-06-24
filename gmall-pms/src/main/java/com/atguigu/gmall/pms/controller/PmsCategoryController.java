package com.atguigu.gmall.pms.controller;

import java.util.List;
import java.util.Locale;

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

import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import com.atguigu.gmall.pms.service.PmsCategoryService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.bean.PageParamVo;

/**
 * 商品三级分类
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
@Api(tags = "商品三级分类 管理")
@RestController
@RequestMapping("pms/category")
public class PmsCategoryController {

    @Autowired
    private PmsCategoryService pmsCategoryService;


    /**
     * 分类列表
     * @param parentId 父节点Id -1:查询所有，0：查询一级节点
     * @return  节点集合
     */
    @GetMapping("parent/{parentId}")
    @ApiOperation("根据父节点Id查询分类")
    public ResponseVo<List<PmsCategoryEntity>> queryCategory(
            @PathVariable("parentId") Long parentId
    ){
        List<PmsCategoryEntity> pmsCategoryEntityList = this.pmsCategoryService.queryCategory(parentId);
        return ResponseVo.ok(pmsCategoryEntityList);
    }



    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryPmsCategoryByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = pmsCategoryService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsCategoryEntity> queryPmsCategoryById(@PathVariable("id") Long id){
		PmsCategoryEntity pmsCategory = pmsCategoryService.getById(id);

        return ResponseVo.ok(pmsCategory);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody PmsCategoryEntity pmsCategory){
		pmsCategoryService.save(pmsCategory);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody PmsCategoryEntity pmsCategory){
		pmsCategoryService.updateById(pmsCategory);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		pmsCategoryService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
