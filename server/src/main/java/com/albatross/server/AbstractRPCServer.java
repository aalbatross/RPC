/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.server;

import com.albatross.protocol.conn.Connectable;
import com.albatross.protocol.Message;
import com.albatross.protocol.Method;
import com.albatross.protocol.Schema;
import com.albatross.protocol.excption.RPCException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iamrp
 * abstract RPC Server implementation
 */
public abstract class AbstractRPCServer extends Thread implements Connectable {

    private LinkedHashMap<String, Object> objectLookup;
    private LinkedHashMap<String, Schema> lookupSchemaMap;
    private TreeSet<String> lookups;

    public AbstractRPCServer() {
        objectLookup = new LinkedHashMap<String, Object>();
        lookupSchemaMap = new LinkedHashMap<String,Schema>();
        lookups = new TreeSet<String>();
    }

    public AbstractRPCServer(LinkedHashMap<String, Object> objectLookup, LinkedHashMap<String, Schema> lookupSchemaMap, TreeSet<String> lookups) {
        this.objectLookup = objectLookup;
        this.lookupSchemaMap = lookupSchemaMap;
        this.lookups = lookups;
    }

    private void bindObjectImpl(String lookup, Object obj, Schema schema) {
        objectLookup.put(lookup, obj);
        lookupSchemaMap.put(lookup, schema);
        lookups.add(lookup);
    }

    public void bindObject(String lookup, Object obj, Schema schema) {
        if (objectLookup.containsKey(lookup)) {
            throw new RuntimeException("Lookup " + lookup + " already assigned");
        } else {
            bindObjectImpl(lookup, obj, schema);
        }
    }

    public void unbindObject(String lookup, Object obj, Schema schema) {
        if (!objectLookup.containsKey(lookup)) {
            throw new RuntimeException("Lookup " + lookup + " not exists");
        } else {
            objectLookup.remove(lookup);
            lookupSchemaMap.remove(lookup);
            lookups.remove(lookup);
        }
    }

    private Message getAllSchemaResponseImpl(Message req) {
        req.setResponse(lookupSchemaMap);
        req.setType(Message.MSG_RES);
        return req;
    }

    private Message getExecuteResponseImpl(Message req) throws IOException, ClassNotFoundException {
        String lookup = req.getLookupName();
        String serviceName = req.getServiceName();
        req.setType(Message.MSG_RES);
        ArrayList<String> exceptions = new ArrayList<String>();

        Schema schema = lookupSchemaMap.get(lookup);
        if (!schema.getServicename().equals(serviceName)) {
            exceptions.add(RPCException.SERVICE_NAME_DONOT_MATCH);
            req.setExceptions(exceptions.toArray(new String[exceptions.size()]));
            return req;
        }

        Method[] methods = schema.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(req.getMethodname())) {
                Object[] objs = req.getArgs();
                String[] strs = method.getArgs();
                for(int i=0;i<objs.length;i++){
                    if(objs[i].getClass().getName().equals(LinkedHashMap.class.getName()))
                    {
                        if(!strs[i].equals(LinkedHashMap.class.getName())){
                            ObjectMapper mapper = new ObjectMapper();
                            String[] split=strs[i].split("\\.");
                            System.out.println(strs[i]+" split length: "+split.length);
                            objs[i]=mapper.readValue(mapper.writeValueAsString(objs[i]), Class.forName(strs[i].replace("."+split[split.length-1], "$"+split[split.length-1])));
                        }
                    }
                }
                ArrayList<Class> classes = new ArrayList<Class>();
                for (Object obj : objs) {
                    classes.add(obj.getClass());
                }
                Class[] clss = classes.toArray(new Class[classes.size()]);
                java.lang.reflect.Method meth;
                try {
                    meth = objectLookup.get(lookup).getClass().getMethod(req.getMethodname(), clss);
                    if(meth.getReturnType().equals(void.class)){
                         meth.invoke(objectLookup.get(lookup), objs);
                         req.setResponse("");
                         break;
                    }
                    else{
                        Object invoke=meth.invoke(objectLookup.get(lookup), objs);
                        req.setResponse(invoke);
                        break;
                    }
                } catch (Exception ex) {
                    
                    exceptions.add(ex.getCause().getMessage());
                    req.setExceptions(exceptions.toArray(new String[exceptions.size()]));
                    
                    return req;
                }

            }

        }
        if (req.getResponse() == null) {
            exceptions.add(RPCException.METHOD_NOT_FOUND);
            req.setExceptions(exceptions.toArray(new String[exceptions.size()]));
            return req;
        }
        return req;

    }

    public Message processRequest(Message req) throws IOException, ClassNotFoundException {
        if (!req.getType().equals(Message.MSG_REQ)) {
            req.setType(Message.MSG_RES);
            req.setExceptions(new String[]{RPCException.MESSAGE_MUST_BE_REQ});
            return req;
        }
        if (req.getLookupName().isEmpty() && req.getServiceName().isEmpty()) {
            return getAllSchemaResponseImpl(req);
        }
        if (!req.getMethodname().isEmpty() && !req.getLookupName().isEmpty() && !req.getServiceName().isEmpty()) {
            return getExecuteResponseImpl(req);
        }
        req.setType(Message.MSG_RES);
        req.setExceptions(new String[]{RPCException.REQ_NOT_EXECUTED});
        return req;
    }

    public void rebindObject(String lookup, Object obj, Schema schema) {
        bindObjectImpl(lookup, obj, schema);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted())
            try {
                process();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    private void process() throws IOException, ClassNotFoundException {
        Message req = this.recv();
        Message res = this.processRequest(req);
        this.send(res);
    }
}
