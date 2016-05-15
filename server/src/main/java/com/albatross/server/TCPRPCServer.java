/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.server;

import com.albatross.protocol.Message;
import com.albatross.protocol.Schema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.TreeSet;
import org.zeromq.ZMQ;

/**
 *
 * @author iamrp
 * TCPRPCServer implementation
 */
public class TCPRPCServer extends AbstractRPCServer {

    ZMQ.Socket socket;
    ZMQ.Context ctx;
    
    public TCPRPCServer(){
        super();
        this.ctx= ZMQ.context(1);
        this.socket = ctx.socket(ZMQ.REP);
    }
    
    public TCPRPCServer(LinkedHashMap<String, Object> objectLookup, LinkedHashMap<String, Schema> lookupSchemaMap, TreeSet<String> lookups) {
        super(objectLookup, lookupSchemaMap, lookups);
        this.ctx= ZMQ.context(1);
        this.socket = ctx.socket(ZMQ.REP);
    }

    public void bind(String connectionURL) {
        this.socket.bind(connectionURL);
    }

    public void send(Message m) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String response= mapper.writeValueAsString(m);
                        System.out.println("Response sent: "+response);
            this.socket.send(response);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    public Message recv() {
        try {
            String request= this.socket.recvStr();
            System.out.println("Requst received: "+request);
            ObjectMapper mapper = new ObjectMapper();
            Message req= mapper.readValue(request,Message.class);
            return req;
        } catch (IOException ex) {
           throw new RuntimeException("Error while receiving:"+ex);
        }
        
    }

    public void close() {
        this.socket.close();
        this.ctx.term();
    }
    
}
