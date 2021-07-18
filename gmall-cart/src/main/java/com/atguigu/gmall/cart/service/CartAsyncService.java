package com.atguigu.gmall.cart.service;


import com.atguigu.gmall.cart.entity.Cart;
import com.atguigu.gmall.cart.mapper.CartMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CartAsyncService {

    @Autowired
    private CartMapper cartMapper;

    @Async
    public void updateCartByUserIdAndSkuId(
            String userId, Cart cart
    ){
        cartMapper.update(cart,new QueryWrapper<Cart>()
                .eq("user_id",userId)
                .eq("sku_id",cart.getSkuId()));
    }


    @Async
    public void saveCart(String skuId, Cart cart){
        this.cartMapper.insert(cart);
    }

    @Async
    public void deletCartByUserId(String userId) {
        this.cartMapper.delete(new QueryWrapper<Cart>().eq("user_id",userId));
    }

    @Async
    public void deleteCartByUserIdAndSkuId(String userId, Long skuId) {
        this.cartMapper.delete(new QueryWrapper<Cart>()
                .eq("user_id",userId)
                .eq("sku_id",skuId));
    }


}
