/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.protocol;

/**
 *
 * @author iamrp 
 * Dependent class for Service
 */
public class Dependency {
    /**
     * namespace or package of Service 
     */
    private String namespace;
    /**
     * name of Service
     */
    private String name;
    /**
     * encapsulated fields
     */
    private String[] fields;
    
    public Dependency() {

    }
    /**
     *  instantiates Dependency of a Service
     * @param namespace
     * @param name
     * @param fields 
     */
    public Dependency(String namespace, String name, String[] fields) {
        this.namespace = namespace;
        this.name = name;
        this.fields = fields;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

}
