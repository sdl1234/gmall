package com.atguigu.gmall.pms.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import com.atguigu.gmall.pms.service.PmsCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 根据三级分类Id查询123级分类
     * @param cid
     * @return
     */
    @GetMapping("sub/{cid3}")
    public ResponseVo<List<PmsCategoryEntity>> queryLv1123CategoriesByCid(@PathVariable("cid3") Long cid){
        List<PmsCategoryEntity> categoryEntities = this.pmsCategoryService.queryLv1123CategoriesByCid(cid);
        return ResponseVo.ok(categoryEntities);
    }



    @GetMapping("subs/{pid}")
    public ResponseVo<List<PmsCategoryEntity>> queryLv2WithSubsByPid(@PathVariable("pid")Long pid){
        List<PmsCategoryEntity> categoryEntities = this.pmsCategoryService.queryLv2WithSubsByPid(pid);
        return ResponseVo.ok(categoryEntities);
    }



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
