/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.bluetoothserver.test;

import com.albatross.rpc.protocol.JsonUtils;
import com.albatross.rpc.protocol.Message;
import com.albatross.rpc.protocol.Schema;
import com.albatross.rpc.server.RFCommRPCServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

/**
 *
 * @author iamrp
 */
public class AppRFCommServer {
    public static void main(String[] args) throws Exception{
        
    
        System.out.println(HelloService.class.isInterface());
        Schema sc= JsonUtils.generateProtocolFromInterface(HelloService.class);
        Message mreq = new Message(UUID.randomUUID().toString(), "hello", sc.getServicename(), Message.MSG_REQ, "printHello", new Object[]{"Hello World!!"}, null, new String[]{});
        ObjectMapper mapper = new ObjectMapper();
        //no need to provide it binds automatically
        RFCommRPCServer server = new RFCommRPCServer("");
        server.bindObject("hello", new HelloServer(),sc );
        Message processRequest = server.processRequest(mreq);
        System.out.println(mapper.writeValueAsString(processRequest));
        server.startServer();
    
    
    }
}
