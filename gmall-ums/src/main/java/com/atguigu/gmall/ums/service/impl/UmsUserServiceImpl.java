package com.atguigu.gmall.ums.service.impl;

import com.alibaba.nacos.common.util.UuidUtils;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.ums.entity.UmsUserEntity;
import com.atguigu.gmall.ums.mapper.UmsUserMapper;
import com.atguigu.gmall.ums.service.UmsUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;


@Service("umsUserService")
public class UmsUserServiceImpl extends ServiceImpl<UmsUserMapper, UmsUserEntity> implements UmsUserService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UmsUserEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UmsUserEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public Boolean checkByDataAndType(String data, Integer type) {
        QueryWrapper<UmsUserEntity> queryWrapper =new QueryWrapper();
        switch (type){
            case 1:queryWrapper.eq("username",data);break;
            case 2:queryWrapper.eq("phone",data);break;
            case 3:queryWrapper.eq("email",data);break;
            default:
                return null;
        }

        return this.count(queryWrapper) == 0;
    }

    @Override
    public ResponseVo register(UmsUserEntity userEntity, String code) {
        //发送短信验证码

        //生成盐
         userEntity.setSalt(StringUtils.substring(UuidUtils.generateUuid(),0,6));
        //使用MD5加密
        userEntity.setPassword(DigestUtils.md5Hex(userEntity.getSalt() + DigestUtils.md5Hex(userEntity.getPassword())));
        //设置默认值
        userEntity.setNickname(userEntity.getUsername());
        userEntity.setLevelId(1L);
        userEntity.setGender(1);
        userEntity.setBirthday(new Date());
        userEntity.setStatus(1);
        userEntity.setIntegration(100);
        userEntity.setGrowth(200);
        userEntity.setCreateTime(new Date());
        userEntity.setSourceType(1);

        //保存
        this.save(userEntity);
        //删除验证码


        return null;
    }

    @Override
    public UmsUserEntity queryUser(String loginName, String password) {
        //根据loginName查询用户数据
        List<UmsUserEntity> userEntities = this.list(new QueryWrapper<UmsUserEntity>().eq("username",loginName)
                .or().eq("phone",loginName)
                .or().eq("email",loginName));

        //判断是否为空
        if (CollectionUtils.isEmpty(userEntities)){
            return null;
        }
        //根据password加盐加密后对比
        for (UmsUserEntity userEntity : userEntities) {
            if(StringUtils.equals(userEntity.getPassword(),
                    DigestUtils.md5Hex(userEntity.getSalt()
                            + DigestUtils.md5Hex(password)))){
                return userEntity;
            }
        }
        //返回数据
        return null;
    }

}