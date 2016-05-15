/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.protocol.conn;

import com.albatross.protocol.Message;

/**
 *
 * @author iamrp
 */
public interface Connectable {

    public void bind(String connectionURL);
    public void send(Message m);
    public Message recv();
    public void close();
}
