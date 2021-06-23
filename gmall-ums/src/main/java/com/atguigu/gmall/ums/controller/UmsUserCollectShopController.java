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

import com.atguigu.gmall.ums.entity.UmsUserCollectShopEntity;
import com.atguigu.gmall.ums.service.UmsUserCollectShopService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.bean.PageParamVo;

/**
 * 关注店铺表
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:58:20
 */
@Api(tags = "关注店铺表 管理")
@RestController
@RequestMapping("ums/umsusercollectshop")
public class UmsUserCollectShopController {

    @Autowired
    private UmsUserCollectShopService umsUserCollectShopService;

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryUmsUserCollectShopByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = umsUserCollectShopService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<UmsUserCollectShopEntity> queryUmsUserCollectShopById(@PathVariable("id") Long id){
		UmsUserCollectShopEntity umsUserCollectShop = umsUserCollectShopService.getById(id);

        return ResponseVo.ok(umsUserCollectShop);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody UmsUserCollectShopEntity umsUserCollectShop){
		umsUserCollectShopService.save(umsUserCollectShop);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody UmsUserCollectShopEntity umsUserCollectShop){
		umsUserCollectShopService.updateById(umsUserCollectShop);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		umsUserCollectShopService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
