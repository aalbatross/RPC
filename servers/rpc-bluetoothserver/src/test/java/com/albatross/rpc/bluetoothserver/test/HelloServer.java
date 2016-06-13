/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.bluetoothserver.test;

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
        return new Sample(25,"sasma");
    }

    public TSample saySample(Sample s) {
        return new TSample(12,s);
    }
    
}
