package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.PmsAttrGroupVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.pms.entity.PmsAttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
public interface PmsAttrGroupService extends IService<PmsAttrGroupEntity> {

    PageResultVo queryPage(PageParamVo paramVo);


    List<PmsAttrGroupVo> queryPmsAttrGroupByCatId(Long catId);
}

