package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 商品三级分类
 *
 * @author sdl
 * @email sdl@atguigu.com
 * @date 2021-06-22 17:49:44
 */
public interface PmsCategoryService extends IService<PmsCategoryEntity> {

    PageResultVo queryPage(PageParamVo paramVo);

    List<PmsCategoryEntity> queryCategory(Long parentId);

    List<PmsCategoryEntity> queryLv2WithSubsByPid(Long pid);

    List<PmsCategoryEntity> queryLv1123CategoriesByCid(Long cid);

}

