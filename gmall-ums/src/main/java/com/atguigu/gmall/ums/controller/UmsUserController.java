package com.atguigu.gmall.ums.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.ums.entity.UmsUserEntity;
import com.atguigu.gmall.ums.service.UmsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户表
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:58:20
 */
@Api(tags = "用户表 管理")
@RestController
@RequestMapping("ums/user")
public class UmsUserController {


    @Autowired
    private UmsUserService umsUserService;

    @GetMapping("query")
    public ResponseVo<UmsUserEntity> queryUser(
            @RequestParam("loginName")String loginName,
            @RequestParam("password")String password
    ){
          UmsUserEntity userEntity =this.umsUserService.queryUser(loginName,password);

          return ResponseVo.ok(userEntity);
    }




    /**
     * 用户注册
     * @return
     */
    @PostMapping("register")
    public ResponseVo<Boolean> register(
            @RequestParam("code")String code,
            UmsUserEntity userEntity
    ){
        ResponseVo rel = this.umsUserService.register(userEntity,code);
        return rel;
    }



    /**
     * 数据校验
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public Boolean checkByDataAndType(
            @PathVariable("data")String data,
            @PathVariable("type")Integer type
    ){
        return this.umsUserService.checkByDataAndType(data,type);
    }

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryUmsUserByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = umsUserService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<UmsUserEntity> queryUmsUserById(@PathVariable("id") Long id){
		UmsUserEntity umsUser = umsUserService.getById(id);

        return ResponseVo.ok(umsUser);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody UmsUserEntity umsUser){
		umsUserService.save(umsUser);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody UmsUserEntity umsUser){
		umsUserService.updateById(umsUser);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		umsUserService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
