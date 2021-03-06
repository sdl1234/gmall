package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.pms.entity.PmsAttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
public interface PmsAttrService extends IService<PmsAttrEntity> {

    PageResultVo queryPage(PageParamVo paramVo);

    List<PmsAttrEntity> queryPmsAttrEntityByCid(Long cid, Integer type, Integer searchType);
}

