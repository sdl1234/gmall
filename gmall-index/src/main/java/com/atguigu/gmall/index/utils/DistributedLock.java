package com.atguigu.gmall.index.utils;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


//注入spring容器
@Component
public class DistributedLock {

    @Autowired
    private StringRedisTemplate redisTemplate;


    private Timer timer;

    //加锁
    public Boolean lock(String lockName, String uuid, Integer expire){
        //定义lua脚本
        String script = "if(redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1) " +
                "then " +
                "   redis.call('hincrby', KEYS[1], ARGV[1], 1) " +
                "   redis.call('expire', KEYS[1], ARGV[2]) " +
                "   return 1 " +
                "else " +
                "   return 0 " +
                "end";
        //设置锁
        Boolean flag  =this.redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class),
                Arrays.asList(lockName),uuid,expire.toString());
        //判断返回值 回调lock方法
        if (!flag){
            try {
                Thread.sleep(100);
                lock(lockName,uuid,expire);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.renewExpire(lockName,uuid,expire);
        return true;

    }

    //解锁
    public void unlock(String lockName,String uuid){
        String script = "if(redis.call('hexists', KEYS[1], ARGV[1]) == 0) " +
                "then " +
                "   return nil " +
                "elseif(redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0) " +
                "then " +
                "   return redis.call('del', KEYS[1]) " +
                "else " +
                "   return 0 " +
                "end";
        Long flag =this.redisTemplate.execute(new DefaultRedisScript<>(script,Long.class),Arrays.asList(lockName),uuid);
        if (flag ==null){
            throw  new IllegalMonitorStateException("要释放的锁不属于此线程！");
        }else if (flag ==1){
            this.timer.cancel();
        }


    }

    public void renewExpire(String lockName,String uuid ,Integer expire){
        //创建lua语句
        String script = "if(redis.call('hexists', KEYS[1], ARGV[1]) == 1) then redis.call('expire', KEYS[1], ARGV[2]) end";
        //创建看门狗
        this.timer =new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class),Arrays.asList(lockName), uuid,expire.toString());
            }
            //每次重置过期时间的 * 1000/3   每过过期时间 * 1000 /3 执行一次
        },expire * 1000 /3,expire * 1000 /3);
    }





    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("定时器" + System.currentTimeMillis());
            }
        },5000,10000);
    }

}
