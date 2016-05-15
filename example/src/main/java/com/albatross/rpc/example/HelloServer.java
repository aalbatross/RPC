/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.example;

/**
 *
 * @author iamrp
 */
public class HelloServer implements HelloService {

    public void printHello(String name) {
        System.out.println(name);
    }

    public String sayHello() {
        return "Hello";
    }

    public Sample saySample() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public TSample saySample(Sample s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
