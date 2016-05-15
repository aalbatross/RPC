/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.example;

import com.albatross.protocol.JsonUtils;
import com.albatross.protocol.Schema;
import com.albatross.server.TCPRPCServer;
import java.io.IOException;

/**
 *
 * @author iamrp
 */
public class AppTCPServer {
    public static void main(String[] args) throws IOException{
        Schema sc=JsonUtils.generateProtocolFromInterface(HomeService.class);
        TCPRPCServer server = new TCPRPCServer();
        server.bind("tcp://localhost:5555");
        server.bindObject("hello", new HomeServer(50,10), sc);
        server.start();
    }
}