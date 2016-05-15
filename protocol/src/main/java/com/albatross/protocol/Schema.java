/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.protocol;

/**
 *
 * @author iamrp
 * Schema of protocol
 */
public class Schema {
  
    private String namespace;
    private String servicename;
    private Dependencies[] dependencies;
    private Method[] methods;

    public Schema(){
        
    }
    
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public Dependencies[] getDependencies() {
        return dependencies;
    }

    public void setDependencies(Dependencies[] dependencies) {
        this.dependencies = dependencies;
    }

    public Method[] getMethods() {
        return methods;
    }

    public void setMethods(Method[] methods) {
        this.methods = methods;
    }


    
}
