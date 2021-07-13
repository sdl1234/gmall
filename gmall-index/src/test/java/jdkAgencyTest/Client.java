package jdkAgencyTest;

public class Client {
    public static void main(String[] args) {

        Subject subject =new JDKDynamicProxy(new RealSubject()).getProxy();
        subject.doSomeThing();
    }

}
