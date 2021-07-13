package com.atguigu.gmall.pms.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.PmsSpuDescEntity;
import com.atguigu.gmall.pms.service.PmsSpuDescService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * spu信息介绍
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
@Api(tags = "spu信息介绍 管理")
@RestController
@RequestMapping("pms/spudesc")
public class PmsSpuDescController {

    @Autowired
    private PmsSpuDescService pmsSpuDescService;

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryPmsSpuDescByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = pmsSpuDescService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{spuId}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsSpuDescEntity> queryPmsSpuDescById(@PathVariable("spuId") Long spuId){
		PmsSpuDescEntity pmsSpuDesc = pmsSpuDescService.getById(spuId);

        return ResponseVo.ok(pmsSpuDesc);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody PmsSpuDescEntity pmsSpuDesc){
		pmsSpuDescService.save(pmsSpuDesc);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody PmsSpuDescEntity pmsSpuDesc){
		pmsSpuDescService.updateById(pmsSpuDesc);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> spuIds){
		pmsSpuDescService.removeByIds(spuIds);

        return ResponseVo.ok();
    }

}
