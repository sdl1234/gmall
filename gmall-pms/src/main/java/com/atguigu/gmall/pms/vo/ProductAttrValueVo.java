package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.PmsSpuAttrValueEntity;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author sdl
 */
@Data
public class ProductAttrValueVo extends PmsSpuAttrValueEntity {

    /**
     * 为PmsSpuAttrValueEntity类添加前端需要的元素
     */
//    private List<String> valueSelected;


    /**
     * 此方法为元素valueSelected的setter方法的重写
     * @param valueSelected 添加的元素
     */
    public void setValueSelected(List<Object> valueSelected){
        //将valueSelected存入
            //判断valueSelected是否为空
            if (CollectionUtils.isEmpty(valueSelected)){
                return;
            }
            //用,将多个数据分开
            this.setAttrValue(StringUtils.join(valueSelected,","));
    }

}
