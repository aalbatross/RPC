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
public interface HelloService {
    
    class Sample{
        int value;
        String name;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        
        
    }
    
    class TSample{
        int id;
        Sample smp;
    }
    
    public void printHello(String name);
    public String sayHello();
    public Sample saySample();
    public TSample saySample(Sample s);
        
    
}
