package com.albatross.rpc.example;

import com.albatross.rpc.client.TCPRPCClient;

/**
 * Hello world!
 *
 */
public class AppTCPClient 
{
    public static void main( String[] args ) 
    {
        TCPRPCClient client = new TCPRPCClient();
        client.bind("tcp://localhost:5555");
        client.getAllServices();
        //System.out.println(client.getLookupSchemaMap());
        Object obj1= client.call("home", "switchOnTV", new Object[]{8});
        System.out.println("Server says: TV switched on: "+obj1);
        Object obj2=client.call("hello", "saySample", new Object[]{});
        System.out.println("Server says: "+obj2);
        Object obj3= client.call("hello", "saySample", new Object[]{new HelloService.Sample(33, "I am Service with dependent class demo")});
        System.out.println("Server says: "+obj3);
        client.close();
    }
}
