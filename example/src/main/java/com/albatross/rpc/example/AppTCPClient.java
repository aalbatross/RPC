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
        System.out.println(obj1+"  "+obj1.getClass());
        Object obj2=client.call("hello", "saySample", new Object[]{});
        System.out.println(obj2+"  "+obj2.getClass());
        Object obj3= client.call("hello", "saySample", new Object[]{new HelloService.Sample(33, "asasdsda")});
        System.out.println(obj3+"  "+obj3.getClass());
        client.close();
    }
}
