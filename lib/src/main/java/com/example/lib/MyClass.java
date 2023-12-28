package com.example.lib;

import java.io.FileOutputStream;

import sun.misc.ProxyGenerator;

public class MyClass {

    public static void main(String[] args) {
        try {
            proxy();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void proxy() throws Exception {
        String name = Api.class.getName() + "$Proxy0";
        //生成代理指定接口的Class数据
        byte[] bytes = ProxyGenerator.generateProxyClass(name, new Class[]{ Api.class });
        FileOutputStream fos = new FileOutputStream("lib/" + name + ".class");
        fos.write(bytes);
        fos.close();
    }
}