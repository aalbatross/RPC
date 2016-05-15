/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.client;

import com.albatross.rpc.protocol.Message;
import com.albatross.rpc.protocol.Method;
import com.albatross.rpc.protocol.ProtocolDataTypeMap;
import com.albatross.rpc.protocol.Schema;
import com.albatross.rpc.protocol.conn.Connectable;
import com.albatross.rpc.protocol.excption.RPCException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author iamrp
 */
public abstract class AbstractRPCClient implements Connectable {

    private LinkedHashMap<String, Schema> lookupSchemaMap;
    private ObjectMapper mapper;

    public AbstractRPCClient() {
        lookupSchemaMap = new LinkedHashMap<String, Schema>();
        mapper = new ObjectMapper();

    }

    public LinkedHashMap<String, Schema> getLookupSchemaMap() {
        return lookupSchemaMap;
    }

    public void getAllServices() throws JsonProcessingException, IOException {
        Message mreq = new Message(UUID.randomUUID().toString(), "", "", Message.MSG_REQ, "", new Object[]{}, null, new String[]{});
        Message mres = executeRequest(mreq);
        if (mres.getExceptions().length == 0) {
            LinkedHashMap map = (LinkedHashMap) mres.getResponse();
            Set keys=map.keySet();
            for(Object key:keys){
                String k1 = (String)key;
                String json = mapper.writeValueAsString(map.get(key));
                Schema v1 = mapper.readValue(json, Schema.class);
                System.out.println(v1);
                this.lookupSchemaMap.put(k1, v1);
            }
        }   
        else{
            throw new RuntimeException(mapper.writeValueAsString(mres.getExceptions()));
        }
        
    }

    public Object call(String lookup, String methodName, Object[] args) throws JsonProcessingException, IOException, ClassNotFoundException {
        if (!this.lookupSchemaMap.containsKey(lookup)) {
            throw new RuntimeException("Lookup not available");
        }
        
        Schema sc = this.lookupSchemaMap.get(lookup);
        Message mreq = new Message(UUID.randomUUID().toString(), lookup, sc.getServicename(), Message.MSG_REQ, "printHello", new Object[]{"Hello World!!"}, null, new String[]{});

        Method[] methods = sc.getMethods();
        Method call = null;
        for (Method meth : methods) {
            if (meth.getName().equals(methodName)) {
                call = meth;
            }
        }
        if (call != null) {
            mreq.setMethodname(methodName);
        } else {
            throw new RuntimeException(RPCException.METHOD_NOT_FOUND);
        }
        String[] strs = call.getArgs();
        /*if (strs.length != args.length) {
            throw new RuntimeException(RPCException.ARG_NOT_MATCH_WITH_METHOD);
        }*/
        /*for (int i = 0; i < strs.length; i++) {
            System.out.println(strs[i]+"-"+args[i].getClass().getName());
            if ((strs[i]).equals(ProtocolDataTypeMap.javaToProtoDataType(args[i].getClass()))) {
                continue;
            } else {
                
                throw new RuntimeException(RPCException.ARG_NOT_MATCH_WITH_METHOD);
            }
        }*/
        mreq.setArgs(args);
        Message response = this.executeRequest(mreq);
        if (response.getExceptions().length > 0) {
            throw new RuntimeException(mapper.writeValueAsString(response.getExceptions()));
        }
        return response.getResponse();

    }

    public AbstractRPCClient(LinkedHashMap<String, Schema> lookupSchemaMap) {
        this.lookupSchemaMap = lookupSchemaMap;
        mapper = new ObjectMapper();

    }

    protected Message executeRequest(Message msg) {
        this.send(msg);
        return this.recv();
    }

}
