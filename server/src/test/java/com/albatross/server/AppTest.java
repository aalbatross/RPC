package com.albatross.server;

import com.albatross.protocol.JsonUtils;
import com.albatross.protocol.Message;
import com.albatross.protocol.Schema;
import com.albatross.server.HelloService.Sample;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import org.testng.annotations.Test;
import org.zeromq.ZMQ;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigourous Test :-)
     */
    @Test(enabled = false)
    public void testApp() throws IOException {
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

    }

    @Test(enabled=false)
    public void testApp2() throws IOException {
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

    }
    @Test
        public void testApp3() throws IOException {
        //System.out.println(HelloService.class.isInterface());
        Schema sc = JsonUtils.generateProtocolFromInterface(HelloService.class);

        Message mreq = new Message(UUID.randomUUID().toString(), "hello", sc.getServicename(), Message.MSG_REQ, "saySample", new Object[]{}, null, new String[]{});
        ObjectMapper mapper = new ObjectMapper();
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to clients
        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://*:5555");

        requester.send(mapper.writeValueAsString(mreq));
        String res = requester.recvStr();
        System.out.println(res);

    }
}
