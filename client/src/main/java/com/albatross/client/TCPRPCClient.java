/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.client;

import com.albatross.protocol.Message;
import com.albatross.protocol.Method;
import com.albatross.protocol.Schema;
import com.albatross.protocol.excption.RPCException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.zeromq.ZMQ;

/**
 *
 * @author iamrp
 */
public class TCPRPCClient extends AbstractRPCClient {
    
    private ZMQ.Socket socket;
    private ZMQ.Context ctx;
    
    public TCPRPCClient() {
        super();
        this.ctx = ZMQ.context(1);
        this.socket = ctx.socket(ZMQ.REQ);
    }
    
    
    public final void bind(String connectionURL) {
        this.socket.connect(connectionURL);
    }

    public final void send(Message m) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(m);
            System.out.println("Request sent: " + response);
            this.socket.send(response);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    public final Message recv() {
        try {
            String request = this.socket.recvStr();
            System.out.println("Response received: " + request);
            ObjectMapper mapper = new ObjectMapper();
            Message req = mapper.readValue(request, Message.class);
            return req;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error while sending:" + ex);
        }

    }

    public final void close() {
        this.socket.close();
        this.ctx.close();
    }

}
