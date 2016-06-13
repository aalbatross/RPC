/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.client;

import com.albatross.rpc.protocol.Message;
import com.albatross.rpc.protocol.commons.AbstractRPCClient;
import com.albatross.rpc.protocol.excption.RPCException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.zeromq.ZMQ;

/**
 *
 * @author iamrp
 * TCP RPC client
 */
public class TCPRPCClient extends AbstractRPCClient {
    
    private ZMQ.Socket socket;
    private ZMQ.Context ctx;
    private final static Logger logger = Logger.getLogger(TCPRPCClient.class);
    
    public TCPRPCClient(String connectionURL) {
        super(connectionURL);
        
    }
    
    protected final void bind(String connectionURL) {
        this.ctx = ZMQ.context(1);
        this.socket = ctx.socket(ZMQ.REQ);
        this.socket.connect(connectionURL);
        logger.info("Client started.. ready to connect");
    }

    protected final void send(Message m) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(m);
            if(logger.isDebugEnabled())
                logger.debug("Request sent: " + response);
            this.socket.send(response);
        } catch (JsonProcessingException ex) {
            throw new RPCException("Error while sending: "+ex.getCause().getMessage());
        }
    }

    protected final Message recv() {
        try {
            String request = this.socket.recvStr();
            if(logger.isDebugEnabled())
            logger.debug("Response received: " + request);
            ObjectMapper mapper = new ObjectMapper();
            Message req = mapper.readValue(request, Message.class);
            return req;
        } catch (IOException ex) {
            
            throw new RPCException("Error while receiving:" + ex.getCause().getMessage());
        }

    }

    public final void close() {
        this.socket.close();
        this.ctx.close();
    }

}
