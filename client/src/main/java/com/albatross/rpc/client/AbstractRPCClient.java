/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.client;

import com.albatross.rpc.protocol.Message;
import com.albatross.rpc.protocol.Method;
import com.albatross.rpc.protocol.Schema;
import com.albatross.rpc.protocol.conn.Connectable;
import com.albatross.rpc.protocol.excption.RPCException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;
import org.apache.log4j.Logger;

/**
 *
 * @author iamrp
 */
public abstract class AbstractRPCClient extends Connectable {
    
    private LinkedHashMap<String, Schema> lookupSchemaMap;
    private ObjectMapper mapper;
    private final static Logger logger = Logger.getLogger(AbstractRPCClient.class);
    public AbstractRPCClient(String connectionURL) {
        lookupSchemaMap = new LinkedHashMap<String, Schema>();
        mapper = new ObjectMapper();
        this.bind(connectionURL);
    }

    public LinkedHashMap<String, Schema> getLookupSchemaMap() {
        return lookupSchemaMap;
    }

    public void getAllServices()  {
        try{
        if(logger.isDebugEnabled())
            logger.debug("getAllServices called");
        Message mreq = new Message(UUID.randomUUID().toString(), "", "", Message.MSG_REQ, "", new Object[]{}, null, new String[]{});
        Message mres = executeRequest(mreq);
        if (mres.getExceptions().length == 0) {
            LinkedHashMap map = (LinkedHashMap) mres.getResponse();
            Set keys=map.keySet();
            for(Object key:keys){
                String k1 = (String)key;
                String json = mapper.writeValueAsString(map.get(key));
                Schema v1 = mapper.readValue(json, Schema.class);                
                this.lookupSchemaMap.put(k1, v1);
            }
        }   
        else{
            throw new RPCException(mapper.writeValueAsString(mres.getExceptions()));
        }
        }
        catch(Exception ex){
            throw new RPCException(ex.getMessage());
        }
    }

    public Object call(String lookup, String methodName, Object[] args){
        try{
        
        if (!this.lookupSchemaMap.containsKey(lookup)) {
            throw new RPCException( RPCException.LOOK_UP_NOT_AVAILABLE);
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
            throw new RPCException(RPCException.METHOD_NOT_FOUND);
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
            throw new RPCException(mapper.writeValueAsString(response.getExceptions()));
        }
        return response.getResponse();
        }
        catch(Exception ex){
            throw new RPCException("Error on calling remote method "+ex.getMessage());
        }
    }

    public AbstractRPCClient(LinkedHashMap<String, Schema> lookupSchemaMap,String connectionURL) {
        this.lookupSchemaMap = lookupSchemaMap;
        mapper = new ObjectMapper();
        this.bind(connectionURL);
    }

    protected Message executeRequest(Message msg) {
        this.send(msg);
        return this.recv();
    }

}
