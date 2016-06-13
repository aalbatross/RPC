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
public interface HelloService {
    
    class Sample{
        int value;
        String name;

        public Sample(){
            
        }
        
        public Sample(int v,String n){
            this.value =v;
            this.name = n;
        }
        
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

        public TSample(int id, Sample smp) {
            this.id = id;
            this.smp = smp;
        }
        
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Sample getSmp() {
            return smp;
        }

        public void setSmp(Sample smp) {
            this.smp = smp;
        }
        
        
        
    }
    
    public void printHello(String name);
    public String sayHello();
    public Sample saySample();
    public TSample saySample(Sample s);
        
    
}
