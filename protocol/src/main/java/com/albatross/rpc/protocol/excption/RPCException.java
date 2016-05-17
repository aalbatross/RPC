/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.protocol.excption;

import com.albatross.rpc.protocol.Message;
import java.io.IOException;

/**
 *
 * @author iamrp
 * 
 */
public class RPCException extends RuntimeException{
    public static final String REQ_NOT_PROPER = "Request was not proper";
    public static final String SERVICE_NAME_DONOT_MATCH = "Servicename donot match with lookup";
    public static final String METHOD_NOT_FOUND = "Method not found";
    public static final String MESSAGE_MUST_BE_REQ="Mesage type must be request " + Message.MSG_REQ;
    public static final String REQ_NOT_EXECUTED="Request cannot be executed";
    public static final String ARG_NOT_MATCH_WITH_METHOD="args not matched with request";
    public static final String LOOK_UP_NOT_AVAILABLE="lookup not available";
    
       
    public RPCException(String message) {
        super(message);
    }

    public RPCException(String message, Throwable cause) {
        super(message, cause);
    }
     
    
}
