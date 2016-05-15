/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.example;

import com.albatross.protocol.excption.RPCException;

/**
 *
 * @author iamrp
 */
public interface HomeService {
    
    public boolean toggleSwitchBulb(Integer id) throws RPCException;
    public boolean switchOnTV(Integer id) throws RPCException;
    public boolean switchOffTV(Integer id) throws RPCException;
    public void autoManageMode(Integer mode) throws RPCException;
    
}
