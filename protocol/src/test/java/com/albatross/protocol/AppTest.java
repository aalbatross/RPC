package com.albatross.protocol;

import com.albatross.rpc.protocol.JsonUtils;
import com.albatross.rpc.protocol.Schema;
import com.albatross.rpc.protocol.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import org.testng.annotations.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
 

 
    /**
     * Rigourous Test :-)
     */
    @org.testng.annotations.Test
    public void testApp() throws IOException
    {
        
       Schema result= JsonUtils.generateProtocolFromInterface(HelloService.class);
       System.out.println(result);
        //JsonUtils.generateJavaClassesFromProtocol(result,new File("/home/iamrp/test"));
        //ObjectMapper mapper = new ObjectMapper();
        //Schema readValue = mapper.readValue(result, Schema.class);
       // System.out.println(readValue);
    
    }
    
    @Test
    public void testMessage() throws IOException{
        //String result= JsonUtils.generateProtocolFromInterface(HelloService.class);
       //System.out.println(result);
        //JsonUtils.generateJavaClassesFromProtocol(result,new File("/home/iamrp/test"));
        ObjectMapper mapper = new ObjectMapper();
        Schema readValue = JsonUtils.generateProtocolFromInterface(HelloService.class);
        
        Message m = new Message(UUID.randomUUID().toString(),"Lookup",readValue.getServicename(),Message.MSG_REQ,readValue.getMethods()[2].getName(),new Object[]{},null,new String[]{});
        System.out.println(mapper.writeValueAsString(m));
    }
}
