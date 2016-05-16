/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.server;

import com.albatross.rpc.server.TCPRPCServer;
import com.albatross.rpc.protocol.JsonUtils;
import com.albatross.rpc.protocol.Message;
import com.albatross.rpc.protocol.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author iamrp
 */
public class AppServer {
    
    public static void main(String[] args) throws IOException{
        System.out.println(HelloService.class.isInterface());
        Schema sc= JsonUtils.generateProtocolFromInterface(HelloService.class);
        Message mreq = new Message(UUID.randomUUID().toString(), "hello", sc.getServicename(), Message.MSG_REQ, "printHello", new Object[]{"Hello World!!"}, null, new String[]{});
        ObjectMapper mapper = new ObjectMapper();
        TCPRPCServer server = new TCPRPCServer();
        server.bind("tcp://*:5555");
        server.bindObject("hello", new HelloServer(),sc );
        //Message processRequest = server.processRequest(mreq);
        //System.out.println(mapper.writeValueAsString(processRequest));
        server.startServer();
    }
    
}
