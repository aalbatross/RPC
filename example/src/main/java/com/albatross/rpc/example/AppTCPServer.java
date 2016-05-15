/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.example;

import com.albatross.rpc.protocol.JsonUtils;
import com.albatross.rpc.protocol.Schema;
import com.albatross.rpc.server.TCPRPCServer;
import java.io.IOException;

/**
 *
 * @author iamrp
 */
public class AppTCPServer {
    public static void main(String[] args) throws IOException{
        Schema sc1=JsonUtils.generateProtocolFromInterface(HomeService.class);
        Schema sc2=JsonUtils.generateProtocolFromInterface(HelloService.class);
        TCPRPCServer server = new TCPRPCServer();
        server.bind("tcp://localhost:5555");
        server.bindObject("home", new HomeServer(50,10), sc1);
        server.bindObject("hello", new HelloServer(), sc2);
        server.start();
    }
}
