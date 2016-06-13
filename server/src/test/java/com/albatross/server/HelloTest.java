/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.server;

import java.lang.reflect.Method;
import junit.framework.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author iamrp
 * 
 * Reflection test 
 */
public class HelloTest {
    @Test
    public void test() throws Exception{
        HelloServer server= new HelloServer();
        Object obj=server;
        
        System.out.println("Class: "+obj.getClass());
        Method m=obj.getClass().getMethod("printHello",String.class );
        Object invoke = m.invoke(obj, "Hello World!!");
        Assert.assertNull(invoke);
        System.out.println(m.getReturnType().equals(void.class));
        Assert.assertEquals(void.class, m.getReturnType());
    }
}
