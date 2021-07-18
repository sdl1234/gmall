package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.entity.Cart;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.bean.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;



    @PostMapping("deleteCart")
    @ResponseBody
    public ResponseVo<Object> deleteCart(
            @RequestParam("skuId")Long skuId
    ){
        this.cartService.deleteCart(skuId);
        return ResponseVo.ok();
    }

    /**
     * 修改数量
     * @param cart
     * @return
     */
    @PostMapping("updateNum")
    @ResponseBody
    public ResponseVo<Object> updateNum(
            @RequestBody Cart cart
    ){
        this.cartService.updateNum(cart);
        return ResponseVo.ok();
    }




    /**
     * 查询购物车
     * @param model
     * @return
     */
    @GetMapping("cart.html")
    public String queryCarts(Model model){

         List<Cart> carts= this.cartService.queryCarts();
         model.addAttribute("carts",carts);
         return "cart";


    }





    /**
     * 添加购物车
     * @param cart
     * @return
     */
    @GetMapping
    public String addCart(Cart cart){
        if (cart == null || cart.getSkuId() == null){
            throw new RuntimeException("尚未选择商品");
        }
        this.cartService.addCart(cart);

        return "redirect:http://cart.gmall.com/addCart.html?skuId=" + cart.getSkuId();
    }


    /**
     * 方法重载 添加购物车给成功跳转添加成功页面
     */
    @GetMapping("addCart.html")
    public String addCart(@RequestParam("skuId")Long skuId, Model model){
         Cart cart =this.cartService.queryCartBySkuId(skuId);

         model.addAttribute("cart",cart);

         return "addCart";

    }



    @GetMapping("test")
    @ResponseBody
    private String test() throws ExecutionException, InterruptedException {


        long now = System.currentTimeMillis();
        System.out.println("test方法开始执行");
/*        this.cartService.executor1().addCallback(new SuccessCallback<String>(){
            @Override
            public void onSuccess(String result) {
                System.out.println("1正常执行" + result);
            }
        }, new FailureCallback(){

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("1异常执行" + ex.getMessage());
            }
        });*/

        this.cartService.executor2();

        System.out.println("test方法执行结束  " + (System.currentTimeMillis() -now));
        return "hello cart";
    }
}





















