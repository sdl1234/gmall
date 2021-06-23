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

import com.atguigu.gmall.ums.entity.UmsUserLoginLogEntity;
import com.atguigu.gmall.ums.service.UmsUserLoginLogService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.bean.PageParamVo;

/**
 * 用户登陆记录表
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:58:20
 */
@Api(tags = "用户登陆记录表 管理")
@RestController
@RequestMapping("ums/umsuserloginlog")
public class UmsUserLoginLogController {

    @Autowired
    private UmsUserLoginLogService umsUserLoginLogService;

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryUmsUserLoginLogByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = umsUserLoginLogService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<UmsUserLoginLogEntity> queryUmsUserLoginLogById(@PathVariable("id") Long id){
		UmsUserLoginLogEntity umsUserLoginLog = umsUserLoginLogService.getById(id);

        return ResponseVo.ok(umsUserLoginLog);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody UmsUserLoginLogEntity umsUserLoginLog){
		umsUserLoginLogService.save(umsUserLoginLog);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody UmsUserLoginLogEntity umsUserLoginLog){
		umsUserLoginLogService.updateById(umsUserLoginLog);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		umsUserLoginLogService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
