package com.albatross.server;

import com.albatross.rpc.protocol.JsonUtils;
import com.albatross.rpc.protocol.Message;
import com.albatross.rpc.protocol.Schema;
import com.albatross.rpc.server.TCPRPCServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.zeromq.ZMQ;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigourous Test :-)
     */
    
    static TCPRPCServer server = new TCPRPCServer("tcp://*:5555");
    AtomicInteger count = new AtomicInteger();
    
    @Test(expectedExceptions = org.zeromq.ZMQException.class)
    public void init() throws IOException,org.zeromq.ZMQException, Exception{
        System.out.println(HelloService.class.isInterface());
        Schema sc= JsonUtils.generateProtocolFromInterface(HelloService.class);
        Message mreq = new Message(UUID.randomUUID().toString(), "hello", sc.getServicename(), Message.MSG_REQ, "printHello", new Object[]{"Hello World!!"}, null, new String[]{});
        ObjectMapper mapper = new ObjectMapper();
        server.bind("tcp://*:5555");
        server.bindObject("hello", new HelloServer(),sc );
        //Message processRequest = server.processRequest(mreq);
        //System.out.println(mapper.writeValueAsString(processRequest));
        new testGetAllServices().start();
        new testCallRemoteMethodWithDependency().start();
        new testCallSimpleRemoteMethodWithoutDependency().start();
        new Thread(new Runnable(){
           boolean start_enable=true;
            public void run() {
                while(start_enable){
                    try {
                        Thread.sleep(200);
                        if(count.get()==3){
                            start_enable=false;
                            server.close();
                            
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                         
                }
            }
            
        }).start();
        server.startServer();
           
    }
    

    
    class testGetAllServices extends Thread{
        
        @Override
        public void run(){
            try {
                Thread.sleep(2000);
                //System.out.println(HelloService.class.isInterface());
                //Schema sc= JsonUtils.generateProtocolFromInterface(HelloService.class);
                Message mreq = new Message(UUID.randomUUID().toString(), "", "", Message.MSG_REQ, "", new Object[]{}, null, new String[]{});
                ObjectMapper mapper = new ObjectMapper();
                ZMQ.Context context = ZMQ.context(1);
                
                //  Socket to talk to clients
                ZMQ.Socket requester = context.socket(ZMQ.REQ);
                requester.connect("tcp://*:5555");
                
                requester.send(mapper.writeValueAsString(mreq));
                String res = requester.recvStr();
                System.out.println(res);
                Assert.assertNotNull(res);
                requester.close();
                System.out.println("testGetAllServices done");
                count.incrementAndGet();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    
    class testCallSimpleRemoteMethodWithoutDependency extends Thread {
        public void run(){
            try {
                Thread.sleep(2000);
                //System.out.println(HelloService.class.isInterface());
                Schema sc = JsonUtils.generateProtocolFromInterface(HelloService.class);
                
                Message mreq = new Message(UUID.randomUUID().toString(), "hello", sc.getServicename(), Message.MSG_REQ, "printHello", new Object[]{"Hello World!!"}, null, new String[]{});
                ObjectMapper mapper = new ObjectMapper();
                ZMQ.Context context = ZMQ.context(1);
                
                //  Socket to talk to clients
                ZMQ.Socket requester = context.socket(ZMQ.REQ);
                requester.connect("tcp://*:5555");
                
                requester.send(mapper.writeValueAsString(mreq));
                String res = requester.recvStr();
                System.out.println(res);
                Assert.assertNotNull(res);
                
                requester.close();
                System.out.println("TestGetAllServiceWithoutDependency done");
                count.incrementAndGet();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
 
    class testCallRemoteMethodWithDependency extends Thread  {
        
        @Override
        public void run(){
            try {
                Thread.sleep(2000);
                //System.out.println(HelloService.class.isInterface());
                Schema sc = JsonUtils.generateProtocolFromInterface(HelloService.class);
                
                Message mreq = new Message(UUID.randomUUID().toString(), "hello", sc.getServicename(), Message.MSG_REQ, "saySample", new Object[]{new HelloService.Sample(23, "samdas")}, null, new String[]{});
                ObjectMapper mapper = new ObjectMapper();
                ZMQ.Context context = ZMQ.context(1);
                
                //  Socket to talk to clients
                ZMQ.Socket requester = context.socket(ZMQ.REQ);
                requester.connect("tcp://*:5555");
                
                requester.send(mapper.writeValueAsString(mreq));
                String res = requester.recvStr();
                System.out.println(res);
                Assert.assertNotNull(res);
                requester.close();
                System.out.println("TestCallRemoteWithDependency done");
                count.incrementAndGet();
            } catch (Exception ex) {
               ex.printStackTrace();
            }
        }
    }
    
}
