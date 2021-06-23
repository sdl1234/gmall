package com.atguigu.gmall.pms.controller;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gmall.pms.entity.PmsBrandEntity;
import com.atguigu.gmall.pms.service.PmsBrandService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.bean.PageParamVo;

/**
 * 品牌
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
@Api(tags = "品牌 管理")
@RestController
@RequestMapping("pms/brand")
public class PmsBrandController {

    @Autowired
    private PmsBrandService pmsBrandService;

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryPmsBrandByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = pmsBrandService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsBrandEntity> queryPmsBrandById(@PathVariable("id") Long id){
		PmsBrandEntity pmsBrand = pmsBrandService.getById(id);

        return ResponseVo.ok(pmsBrand);
    }

    /**
     * 保存
     * @return
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo save(@RequestBody PmsBrandEntity pmsBrand){
		pmsBrandService.save(pmsBrand);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody PmsBrandEntity pmsBrand){
		pmsBrandService.updateById(pmsBrand);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		pmsBrandService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
