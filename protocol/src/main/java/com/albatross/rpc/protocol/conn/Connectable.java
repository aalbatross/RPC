/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.protocol.conn;

import com.albatross.rpc.protocol.Message;

/**
 *
 * @author iamrp
 * Interface to implement for Servers and Clients
 */
public abstract class Connectable {
    /**
     * binds 
     * @param connectionURL 
     */
    protected abstract void bind(String connectionURL);
    /**
     * sends message 
     * @param m 
     */
    protected abstract void send(Message m);
    /**
     * receive message
     * @return 
     */
    protected abstract Message recv();
    /**
     * close connection
     */
    protected abstract void close();
}
