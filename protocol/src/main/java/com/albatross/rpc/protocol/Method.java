/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.protocol;

/**
 *
 * @author iamrp 
 * Defines a Method from Schema
 */
public class Method {

    private String name;
    private String returntype;
    private String[] args;

    public Method(){
        
    }
    
    public Method(String name, String returntype, String[] args) {
        this.name = name;
        this.returntype = returntype;
        this.args = args;
    }
    /**
     * gets name of method
     * @return 
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /**
     * stringified type of the return object
     * @return 
     */
    public String getReturntype() {
        return returntype;
    }

    public void setReturntype(String returntype) {
        this.returntype = returntype;
    }
    /**
     * types of the object send as argument
     * @return 
     */
    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

}
