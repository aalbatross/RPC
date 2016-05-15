/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.example;

import com.albatross.rpc.protocol.excption.RPCException;

/**
 *
 * @author iamrp
 */
public class HomeServer implements HomeService{

    boolean[] bulbs;
    boolean[] tvs;
    
    public HomeServer(int b,int t){
        bulbs = new boolean[b];
        tvs= new boolean[t];
    }
    
    public boolean toggleSwitchBulb(Integer id) {
        if(id<0 || id>bulbs.length)
            throw new RPCException("Out of bounds Exception");
        bulbs[id]=!bulbs[id];
        return bulbs[id];
    }

    public boolean switchOnTV(Integer id) {
        if(id<0 || id>tvs.length)
            throw new RPCException("Out of bounds Exception");
        if(tvs[id]==true)
            throw new RPCException("Already Switched on");
        else
            tvs[id]=true;
        return true;
    }

    public boolean switchOffTV(Integer id) {
        if(id<0 || id>tvs.length)
            throw new RPCException("Out of bounds Exception");
        if(tvs[id]==false)
            throw new RPCException("Already Switched off");
        else
            tvs[id]=false;
        return false;
    }

    public void autoManageMode(Integer mode) {
        throw new RPCException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
