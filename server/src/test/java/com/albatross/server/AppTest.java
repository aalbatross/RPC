package com.albatross.server;

import com.albatross.rpc.protocol.JsonUtils;
import com.albatross.rpc.protocol.Message;
import com.albatross.rpc.protocol.Schema;
import com.albatross.rpc.server.TCPRPCServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.zeromq.ZMQ;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigourous Test :-)
     */
    
    
    @Test(enabled = true)
    public void testGetAllServices() throws IOException {
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
    }

    @Test(enabled=true)
    public void testCallSimpleRemoteMethodWithoutDependency() throws IOException {
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
    }
    @Test
        public void testCallRemoteMethodWithDependency() throws IOException {
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
    }
    
}
