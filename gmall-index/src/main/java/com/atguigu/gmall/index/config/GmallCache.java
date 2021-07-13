package com.atguigu.gmall.index.config;


import java.lang.annotation.*;


/**
 * Target({ElementType.METHOD}) 事务的注解可以作用在类上和方法上
 * ElementType.TYPE 作用在类上
 * ElementType.METHOD 作用在方法上
 *
 * Retention(RetentionPolicy.RUNTIME)  运行时注解
 *
 * Inherited 是否可以继承
 *
 * Documented 生成文档时是否要添加到文档里
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GmallCache {

    /**
     * 自定义缓存前缀
     * @return
     */
    String prefix() default "gmall:";

    /**
     * 缓存过期时间 单位是min
     * @return
     */
    int timeout() default 30;

    /**
     * 为了防止缓存，给缓存时间添加 随机值
     * 默认为30 ，单位min
     * @return
     */
    int random() default 30;

    /**
     * 为防止缓存击穿，为锁加前缀
     * @return
     */
    String lock() default "lock:";

}























































