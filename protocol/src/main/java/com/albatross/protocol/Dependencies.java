/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.protocol;

/**
 *
 * @author iamrp
 * Dependent class for Service
 */
public class Dependencies {
  private String namespace;
  private String name;
  private String[] fields;

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
