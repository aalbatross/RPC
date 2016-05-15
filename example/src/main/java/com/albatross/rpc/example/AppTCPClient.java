package com.albatross.rpc.example;

import com.albatross.rpc.client.TCPRPCClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class AppTCPClient 
{
    public static void main( String[] args ) throws JsonProcessingException, IOException, ClassNotFoundException
    {
        TCPRPCClient client = new TCPRPCClient();
        client.bind("tcp://localhost:5555");
        client.getAllServices();
        //System.out.println(client.getLookupSchemaMap());
        client.call("home", "switchOnTV", new Object[]{8});
        client.call("hello", "saySample", new Object[]{});
        client.call("hello", "saySample", new Object[]{new HelloService.Sample(33, "asasdsda")});
    }
}
