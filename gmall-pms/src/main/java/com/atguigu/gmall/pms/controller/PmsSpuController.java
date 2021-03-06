package com.atguigu.gmall.pms.controller;

import java.util.List;

import com.atguigu.gmall.pms.vo.PmsSpuVo;
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

import com.atguigu.gmall.pms.entity.PmsSpuEntity;
import com.atguigu.gmall.pms.service.PmsSpuService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import javax.ws.rs.Path;

/**
 * spu信息
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
@Api(tags = "spu信息 管理")
@RestController
@RequestMapping("pms/spu")
public class PmsSpuController {

    @Autowired
    private PmsSpuService pmsSpuService;


    /**
     * 分页查询已上架的spu
     * @param pageParamVo 分页条件
     * @return
     */
    @PostMapping("page")
    public ResponseVo<List<PmsSpuEntity>> querySpusByPage(
            @RequestBody PageParamVo pageParamVo)
    {
        PageResultVo page = pmsSpuService.queryPage(pageParamVo);
        List<PmsSpuEntity> list = (List<PmsSpuEntity>) page.getList();
        return ResponseVo.ok(list);
    }


    /**
     * 查询spu列表
     * @param categoryId 类型ID
     * @param pageParamVo 分页数据
     * @return  分页数据，查询数据
     */
    @GetMapping("category/{categoryId}")
    @ApiOperation("查询spu列表")
    public ResponseVo<PageResultVo> querySpuInfo(
            @PathVariable("categoryId") Long categoryId,
            PageParamVo pageParamVo
    ){
        PageResultVo pageResultVo =this.pmsSpuService.querySpuInfo(categoryId,pageParamVo);
        return ResponseVo.ok(pageResultVo);
    }



    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryPmsSpuByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = pmsSpuService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<PmsSpuEntity> queryPmsSpuById(@PathVariable("id") Long id){
		PmsSpuEntity pmsSpu = pmsSpuService.getById(id);

        return ResponseVo.ok(pmsSpu);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody PmsSpuVo pmsSpuVo){
		pmsSpuService.bigSave(pmsSpuVo);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody PmsSpuEntity pmsSpu){
		pmsSpuService.updateById(pmsSpu);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		pmsSpuService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
