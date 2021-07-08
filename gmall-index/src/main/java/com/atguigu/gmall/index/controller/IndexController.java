package com.atguigu.gmall.index.controller;


import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.index.sercive.IndexService;
import com.atguigu.gmall.pms.entity.PmsCategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping
    public String toIndex(Model model){

         List<PmsCategoryEntity> categoryEntities =this.indexService.queryLv1Categories();
         model.addAttribute("categories",categoryEntities);

         //TODO: 加载其他数据

        return "index";
    }

    @ResponseBody
    @GetMapping("index/cates/{pid}")
    public ResponseVo<List<PmsCategoryEntity>> queryLv2WithSubsById(@PathVariable("pid")Long pid){
         List<PmsCategoryEntity> categoryEntities = this.indexService.queryLv2WithSubsById(pid);
         return ResponseVo.ok(categoryEntities);

    }


    @GetMapping("index/test/lock")
    @ResponseBody
    public ResponseVo testLock(){
        this.indexService.testLock();
        return ResponseVo.ok();
    }

}
