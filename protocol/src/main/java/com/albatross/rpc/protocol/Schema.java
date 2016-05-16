/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.protocol;

/**
 *
 * @author iamrp
 * Schema of protocol
 */
public class Schema {
  
    private String namespace;
    private String servicename;
    private Dependency[] dependencies;
    private Method[] methods;

    public Schema(){
        
    }
    /**
     * Name space of schema 
     * @return 
     */
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    /**
     * returns name of Service
     * @return 
     */
    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }
    /**
     * return dependent classes
     * @return 
     */
    public Dependency[] getDependencies() {
        return dependencies;
    }

    public void setDependencies(Dependency[] dependencies) {
        this.dependencies = dependencies;
    }
    /**
     * return various methods defined in the Service
     * @return 
     */
    public Method[] getMethods() {
        return methods;
    }

    public void setMethods(Method[] methods) {
        this.methods = methods;
    }
}
