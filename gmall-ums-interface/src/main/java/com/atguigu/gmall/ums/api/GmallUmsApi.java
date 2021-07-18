package com.atguigu.gmall.ums.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.ums.entity.UmsUserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface GmallUmsApi {

    /**
     * 用户查询
     * @param loginName
     * @param password
     * @return
     */
    @GetMapping("ums/user/query")
    public ResponseVo<UmsUserEntity> queryUser(
            @RequestParam("loginName")String loginName,
            @RequestParam("password")String password
    );
}
