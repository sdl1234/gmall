package com.atguigu.gmall.cart.service;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.cart.entity.Cart;
import com.atguigu.gmall.cart.entity.UserInfo;
import com.atguigu.gmall.cart.feign.GmallPmsClient;
import com.atguigu.gmall.cart.feign.GmallSmsClient;
import com.atguigu.gmall.cart.feign.GmallWmsClient;
import com.atguigu.gmall.cart.interceptor.LoginInterceptor;
import com.atguigu.gmall.cart.mapper.CartMapper;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.PmsSkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.PmsSkuEntity;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import com.atguigu.gmall.wms.entity.WmsWareSkuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.concurrent.ListenableFuture;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private GmallWmsClient wmsClient;

    @Autowired
    private GmallPmsClient pmsClient;

    @Autowired
    private GmallSmsClient smsClient;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:info:";
    private static final String PRICE_PREFIX = "cart:prefix:";

    @Autowired
    private CartAsyncService cartAsyncService;


    public void addCart(Cart cart) {

        //获取登录信息
        String userId = getUserInfo();
        String key = KEY_PREFIX + userId;

        //获取redis中购物车信息
        BoundHashOperations<String, Object, Object> boundHashOps = this.redisTemplate.boundHashOps(key);

        //查看购物车中是否包含了该商品
        String skuId = cart.getSkuId().toString();
        //商品数量
        BigDecimal count = cart.getCount();
        if (boundHashOps.hasKey(skuId)){
            //包含则更新数量
            String cartJson = boundHashOps.get(skuId).toString();
            cart = JSON.parseObject(cartJson, Cart.class);
            cart.setCount(cart.getCount().add(count));
            this.cartAsyncService.updateCartByUserIdAndSkuId(userId,cart);
        }else {
            //不包含 新增
            cart.setUserId(userId);
            //根据skuId 查询sku
            ResponseVo<PmsSkuEntity> pmsSkuEntityResponseVo = this.pmsClient.queryPmsSkuById(cart.getSkuId());
            PmsSkuEntity skuEntity = pmsSkuEntityResponseVo.getData();
            if (skuEntity != null) {
                 cart.setTitle(skuEntity.getTitle());
                 cart.setPrice(skuEntity.getPrice());
                 cart.setDefaultImage(skuEntity.getDefaultImage());
            }
            //根据skuId查询销售属性
            ResponseVo<List<PmsSkuAttrValueEntity>> skuAttrValuesBySkuId = this.pmsClient.querySkuAttrValuesBySkuId(cart.getSkuId());
            List<PmsSkuAttrValueEntity> attrValueEntities = skuAttrValuesBySkuId.getData();
            cart.setSaleAttrs(JSON.toJSONString(attrValueEntities));

            //营销信息
            ResponseVo<List<ItemSaleVo>> listResponseVo = this.smsClient.queryItemSalesBySkuId(cart.getSkuId());
            List<ItemSaleVo> itemSaleVoList = listResponseVo.getData();
            cart.setSales(JSON.toJSONString(itemSaleVoList));

            //库存信息
            ResponseVo<List<WmsWareSkuEntity>> responseVo = this.wmsClient.queryWmsWareSkuListBySkuId(cart.getSkuId());
            List<WmsWareSkuEntity> wareSkuEntities = responseVo.getData();
            if (!CollectionUtils.isEmpty(wareSkuEntities)){
                cart.setStore(wareSkuEntities.stream().anyMatch(
                        wmsWareSkuEntity ->wmsWareSkuEntity.getStock() - wmsWareSkuEntity.getStockLocked() > 0));
            }

            //商品加入购物车 默认为选中状态
            cart.setCheck(true);
            this.cartAsyncService.saveCart(userId,cart);
            this.redisTemplate.opsForValue().set(PRICE_PREFIX + skuId,skuEntity.getPrice().toString());
        }
        boundHashOps.put(skuId,JSON.toJSONString(cart));
    }

    public Cart queryCartBySkuId(Long skuId) {

        //获取登录信息
        String userId = getUserInfo();
        String key = KEY_PREFIX + userId;

        //获取redis中购物车信息
        BoundHashOperations<String, Object, Object> boundHashOps = this.redisTemplate.boundHashOps(key);
        //根据skuId查询redis是否有数据 (skuId 为Long 型 需要转换)
         if(boundHashOps.hasKey(skuId.toString())){
             //获取数据
             String cartJson = boundHashOps.get(skuId.toString()).toString();
             //转换 返回
             return JSON.parseObject(cartJson,Cart.class);
         }
        throw new RuntimeException("当前库存为空");
    }


    /**
     * 判断经过拦截器判断后 用户是否登录
     * @return
     */
    private String getUserInfo(){
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        if (userInfo.getUserId() != null){
            return userInfo.getUserId().toString();
        }
        return userInfo.getUserKey();
    }

    @Async
    public ListenableFuture<String> executor1() {
        try {
            System.out.println("executor1方法开始执行");
            TimeUnit.SECONDS.sleep(4);
            System.out.println("executor1执行结束--------");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return AsyncResult.forExecutionException(e);
        }
        return AsyncResult.forValue("ex1");
    }

    @Async
    public String executor2() {
        try {
            System.out.println("executor2方法开始执行");
            TimeUnit.SECONDS.sleep(5);
            System.out.println("executor2执行结束--------");
            int i = 1/0;
            return "ex2";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Cart> queryCarts() {

        //获取用户key
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String userKey = userInfo.getUserKey();

        //未登录 查询购物车
        String unloginKey = KEY_PREFIX + userKey;
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(unloginKey);
        List<Object> cartJsons = hashOps.values();
        List<Cart> unloginCarts = null;
        if (!CollectionUtils.isEmpty(cartJsons)){
            unloginCarts=  cartJsons.stream().map(cartJson ->{
                try {
                    Cart cart = JSON.parseObject(cartJson.toString(), Cart.class);
                    //实时价格
                    String currenPrice = this.redisTemplate.opsForValue().get(PRICE_PREFIX + cart.getSkuId());
                    cart.setCurrenPrice(new BigDecimal(currenPrice));
                    return cart;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
        }

        //判断是否登录
        Long userId = userInfo.getUserId();
        if (userId == null){
            return unloginCarts;
        }

        //合并购物车
        String loginKey = KEY_PREFIX + userId;
        //登录 获取购物车
        BoundHashOperations<String, Object, Object> loginHashOps = this.redisTemplate.boundHashOps(loginKey);
        //判断在为登录 状态下是否添加了购物车 有则合并
        if (!CollectionUtils.isEmpty(unloginCarts)){
            unloginCarts.forEach(cart -> {

                try {
                    //登陆后 查询有商品重复
                    if (loginHashOps.hasKey(cart.getSkuId().toString())){
                        //未登录时商品数量
                        BigDecimal count = cart.getCount();
                        //登录后商品数量
                        String cartJson = loginHashOps.get(cart.getSkuId().toString()).toString();
                        cart = JSON.parseObject(cartJson,Cart.class);
                        //更新购物车
                        cart.setCount(cart.getCount().add(count));

                        this.cartAsyncService.updateCartByUserIdAndSkuId(userId.toString(),cart);
                    }else {
                        //不包含当前产品 新增
                        cart.setUserId(userId.toString()); //用户Id 覆盖游客Id
                        this.cartAsyncService.saveCart(userId.toString(),cart);

                        loginHashOps.put(cart.getSkuId().toString(),JSON.toJSONString(cart));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            });
            //合并后删除游客 购物车
            this.cartAsyncService.deletCartByUserId(userKey);
            this.redisTemplate.delete(unloginKey);
        }

        //回显登录后的购物车
        List<Object> loginCartJsons = loginHashOps.values();
        if (!CollectionUtils.isEmpty(loginCartJsons)){
            return loginCartJsons.stream().map(loginCartJson ->{
                try {
                    Cart cart = JSON.parseObject(loginCartJson.toString(), Cart.class);
                    //实时价格
                    String currenPrice = this.redisTemplate.opsForValue().get(PRICE_PREFIX + cart.getSkuId());
                    cart.setCurrenPrice(new BigDecimal(currenPrice));
                    return cart;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
        }
        return null;

    }

    public void updateNum(Cart cart) {
        String userId = this.getUserInfo();
        String key = KEY_PREFIX + userId;

        //获取用户购物车
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        if (hashOps.hasKey(cart.getSkuId().toString())){
            try {
                BigDecimal count = cart.getCount();
                //页面传递的数量
                String cartJson = hashOps.get(cart.getSkuId().toString()).toString();
                cart = JSON.parseObject(cartJson,Cart.class);
                cart.setCount(count);

                //更换新sql+redis
                this.cartAsyncService.updateCartByUserIdAndSkuId(userId,cart);

                hashOps.put(cart.getSkuId().toString(),JSON.toJSONString(cart));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void deleteCart(Long skuId) {
        String userId = this.getUserInfo();
        String key = KEY_PREFIX + userId;

        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);

        //购物车中是否存在此商品
        if (hashOps.hasKey(skuId.toString())){
            this.cartAsyncService.deleteCartByUserIdAndSkuId(userId,skuId);
            hashOps.delete(skuId.toString());
        }
    }
}

























