package com.chelsea.java_common;

public class HelloService implements IHelloService{

    @Override
    public void sayHello() {
        System.out.println("hello world");
    }

}
