/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.server;

import com.albatross.rpc.protocol.Message;
import com.albatross.rpc.protocol.Schema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.TreeSet;
import org.zeromq.ZMQ;

/**
 *
 * @author iamrp TCPRPCServer implementation
 */
public class TCPRPCServer extends AbstractRPCServer {

    ZMQ.Socket socket;
    ZMQ.Context ctx;

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AbstractRPCServer.class);

    public TCPRPCServer() {
        super();
        this.ctx = ZMQ.context(1);
        this.socket = ctx.socket(ZMQ.REP);
        if (logger.isInfoEnabled()) {
            logger.info("TCPRPC Server started");
        }
    }

    public TCPRPCServer(LinkedHashMap<String, Object> objectLookup, LinkedHashMap<String, Schema> lookupSchemaMap, TreeSet<String> lookups) {
        super(objectLookup, lookupSchemaMap, lookups);
        this.ctx = ZMQ.context(1);
        this.socket = ctx.socket(ZMQ.REP);
        if (logger.isInfoEnabled()) {
            logger.info("TCPRPC Server started");
        }
    }

    public final void bind(String connectionURL) {
        this.socket.bind(connectionURL);
    }

    protected final void send(Message m) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(m);
            if (logger.isInfoEnabled()) {
                logger.info("Response sent: " + response);
            }
            this.socket.send(response);
        } catch (JsonProcessingException ex) {
            logger.error(ex.getCause());
            throw new RuntimeException("Error while sending:" + ex);
        }
    }

    protected final Message recv() {
        try {
            String request = this.socket.recvStr();
            if (logger.isInfoEnabled()) {
                logger.info("Request received: " + request);
            }

            ObjectMapper mapper = new ObjectMapper();
            Message req = mapper.readValue(request, Message.class);
            return req;
        } catch (IOException ex) {
            logger.error(ex.getCause().getMessage());
            throw new RuntimeException("Error while receiving:" + ex);
        }

    }

    protected final void close() {
        this.socket.close();
        this.ctx.term();
    }

}
