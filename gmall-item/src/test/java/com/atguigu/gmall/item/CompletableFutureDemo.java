package com.atguigu.gmall.item;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {
//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        //创建线程池方法
//        CompletableFuture future = CompletableFuture.supplyAsync(
//                //重写方法
//                new Supplier<Object>() {
//                    @Override
//                    public Object get() {
//                        //主线程出现异常则直接会运行异常方法。
//                        System.out.println(Thread.currentThread().getName() + "\tcompletableFuture");
////                        int i = 1/0;
//                        return 1024;
//                    }
//                }
//        ).thenApply(new Function<Object, Object>() {  //可以在return之前获取到上个线程中某个已经执行完的并返回的结果
//            @Override
//            public Object apply(Object o) {
//                System.out.println("thenApply方法， 上次返回结果是：" + o);
//                return o ;
//            }
//        }).whenComplete(new BiConsumer<Object, Throwable>() { //当重写的方法中的业务执行完成后运行下面代码（return 之前）
//            @Override
//            public void accept(Object o, Throwable throwable) {
//                System.out.println("-------o=" + o.toString());
//                System.out.println("-------throwable=" + throwable);
//            }
//            //主线程出现异常后执行
//        }).exceptionally(new Function<Throwable, Object>() {
//            @Override
//            public Object apply(Throwable throwable) {
//                System.out.println("throwable=" + throwable);
//                return 6666;
//            }
//        });
//        System.out.println(future.get());
//
//    }


    public static void main(String[] args) {
        List<CompletableFuture> futures = Arrays.asList(
                CompletableFuture.completedFuture("hello"),
                CompletableFuture.completedFuture("world!"),
                CompletableFuture.completedFuture("hello"),
                CompletableFuture.completedFuture("Java!")
        );
        //将所有线程全部完成后一并输出
        final CompletableFuture<Void> allCompleted = CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}));
        allCompleted.thenRun(() -> {
                futures.stream().forEach(future ->{

                    try {
                        System.out.println("get future at : " + System.currentTimeMillis() + " result : " + future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
        });
    }



















}





























