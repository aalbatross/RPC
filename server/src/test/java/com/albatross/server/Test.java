/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.server;

/**
 *
 * @author iamrp
 */
public class Test {
    
    public static void main(String[] args){
        String str="com.albatross.server.HelloService.Sample";
        String[] splits = str.split("\\.");
        for(String split:splits)
            System.out.println(split);
        System.out.println(int.class.getCanonicalName());
    }
    
}
