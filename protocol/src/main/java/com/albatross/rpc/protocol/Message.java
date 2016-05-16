/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.protocol;

/**
 *
 * @author iamrp 
 * Request and Response Message Envelope
 */
public class Message {

    public static final String MSG_REQ = "REQ";
    public static final String MSG_RES = "RES";

    private String msgId;
    private String lookupName;
    private String serviceName;
    private String type;
    private String methodname;
    private Object[] args;
    private Object response;
    private String[] exceptions;

    public Message() {

    }

    public Message(String msgId, String lookup, String serviceName, String type, String methodname, Object[] args, Object response, String[] exceptions) {
        this.msgId = msgId;
        this.type = type;
        this.lookupName = lookup;
        this.serviceName = serviceName;
        this.methodname = methodname;
        this.args = args;
        this.response = response;
        this.exceptions = exceptions;
    }

    /**
     * get associated lookup name for the Object bound in server
     *
     * @return
     */
    public String getLookupName() {
        return lookupName;
    }

    public void setLookupName(String lookupName) {
        this.lookupName = lookupName;
    }

    /**
     * return Service name for message request/response
     *
     * @return
     */
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * unique identifier for message
     *
     * @return
     */
    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    /**
     * get type of message Request/Response
     *
     * @return
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * get MethodName to be called from the Service
     *
     * @return
     */
    public String getMethodname() {
        return methodname;
    }

    public void setMethodname(String methodname) {
        this.methodname = methodname;
    }

    /**
     * get arguments to call the assigned method name
     *
     * @return
     */
    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    /**
     * gets response from targeted RPCServer
     *
     * @return
     */
    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    /**
     * get Exceptions in execution of the remote method
     *
     * @return
     */
    public String[] getExceptions() {
        return exceptions;
    }

    public void setExceptions(String[] exceptions) {
        this.exceptions = exceptions;
    }

}
