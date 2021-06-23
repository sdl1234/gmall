package com.atguigu.gmall.ums.controller;

import java.util.List;

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

import com.atguigu.gmall.ums.entity.UmsUserCollectSkuEntity;
import com.atguigu.gmall.ums.service.UmsUserCollectSkuService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.bean.PageParamVo;

/**
 * 关注商品表
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:58:20
 */
@Api(tags = "关注商品表 管理")
@RestController
@RequestMapping("ums/umsusercollectsku")
public class UmsUserCollectSkuController {

    @Autowired
    private UmsUserCollectSkuService umsUserCollectSkuService;

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryUmsUserCollectSkuByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = umsUserCollectSkuService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<UmsUserCollectSkuEntity> queryUmsUserCollectSkuById(@PathVariable("id") Long id){
		UmsUserCollectSkuEntity umsUserCollectSku = umsUserCollectSkuService.getById(id);

        return ResponseVo.ok(umsUserCollectSku);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody UmsUserCollectSkuEntity umsUserCollectSku){
		umsUserCollectSkuService.save(umsUserCollectSku);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody UmsUserCollectSkuEntity umsUserCollectSku){
		umsUserCollectSkuService.updateById(umsUserCollectSku);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		umsUserCollectSkuService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
