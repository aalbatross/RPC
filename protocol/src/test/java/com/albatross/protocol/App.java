/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.protocol;

/**
 *
 * @author iamrp
 */
public class App {
    
    public static void main(String[] args) throws ClassNotFoundException{
        Class<?> forName = Class.forName("com.albatross.protocol.HelloService$Sample");
        java.lang.reflect.Method[] methods=forName.getDeclaredMethods();
        for(java.lang.reflect.Method method:methods)
            System.out.println(method.getName());
        System.out.println(forName.getCanonicalName());
    }
    
}
