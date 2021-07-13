package com.atguigu.gmall.index.aspect;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.index.config.GmallCache;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class GmallAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RBloomFilter bloomFilter;

/*

    @Pointcut("execution(* com.atguigu.gmall.index.sercive.*.*(..))")
    public void pointcut(){};

    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    }
*/



    @Around("@annotation(com.atguigu.gmall.index.config.GmallCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
    //获取目标对象的注解属性
        //使用ProceedingJoinPoint获取目标对象 （强转为MethodSignature）
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //根据目标对象获取对象方法
        Method method = signature.getMethod();
        //获取方法的返回值类型
        Class returnType = signature.getReturnType();
        //根据方法获取注解类
        GmallCache annotation = method.getAnnotation(GmallCache.class);
        //获取方法中的参数列表（使用集合表示）
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        //获取注解类中的元素
        String prefix = annotation.prefix();

    //拼接参数
        //前缀
        String key = prefix + args;
    //查询缓存中是否存在数据
        //使用布隆过滤
        boolean contains = this.bloomFilter.contains(key);
        if (!contains){
            return null;
        }

        String json = this.redisTemplate.opsForValue().get(key);
        //判断--》序列化参数
        if (StringUtils.isNotBlank(json)) {
            return JSON.parseObject(json,returnType);
        }

    //加锁
        RLock fairLock = this.redissonClient.getFairLock(annotation.lock() + args);
        fairLock.lock();

        try {
            //再次判断缓存中是否有数据
            String json2 = this.redisTemplate.opsForValue().get(key);
            //判断--》序列化参数
            if (StringUtils.isNotBlank(json2)) {
                return JSON.parseObject(json2,returnType);
            }
            //调用目标方法
            Object result = joinPoint.proceed(joinPoint.getArgs());




            if (result != null) {
            //添加缓存
                int timeOut = annotation.timeout() + new Random().nextInt(annotation.random());
                this.redisTemplate.opsForValue().set(key,JSON.toJSONString(result),timeOut, TimeUnit.MINUTES);
            }
            return result;
        } finally {
            //关锁
            fairLock.unlock();
        }
    }


}
