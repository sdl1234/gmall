package jdkAgencyTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKDynamicProxy  implements InvocationHandler {

    private Object target;

    public  JDKDynamicProxy(Object  target){
        this.target = target;
    }

    public <T> T getProxy(){
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("方法前");
        Object result = method.invoke(target, args);
        System.out.println("方法后");
        return result;
    }
}
