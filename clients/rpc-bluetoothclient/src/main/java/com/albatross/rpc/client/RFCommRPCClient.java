/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.client;

import com.albatross.rpc.protocol.Message;
import com.albatross.rpc.protocol.commons.AbstractRPCClient;

/**
 *
 * @author root
 */
public class RFCommRPCClient extends AbstractRPCClient {

    public RFCommRPCClient(String connectionURL) {
        super(connectionURL);
    }

    @Override
    protected void bind(String connectionURL) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void send(Message m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Message recv() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
