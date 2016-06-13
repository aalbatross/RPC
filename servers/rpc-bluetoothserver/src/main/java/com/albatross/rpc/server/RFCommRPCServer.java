/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.rpc.server;

import com.albatross.rpc.protocol.Message;
import com.albatross.rpc.protocol.Schema;
import com.albatross.rpc.protocol.commons.AbstractRPCServer;
import com.albatross.rpc.protocol.excption.RPCException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.TreeSet;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import org.apache.log4j.Logger;

/**
 *
 * @author iamrp
 */
public class RFCommRPCServer extends AbstractRPCServer{

    private final static Logger logger = Logger.getLogger(RFCommRPCServer.class);
    private UUID uuid; 
    private StreamConnectionNotifier service;
    private String connectionURL;
    private StreamConnection conn;
    private BufferedReader insreader;
    private PrintWriter oswriter;
    private ObjectMapper mapper;
    private RemoteDevice dev;
    public RFCommRPCServer(String connectionURL) {
        super(connectionURL);
        mapper = new ObjectMapper();
        try {
            LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
        } catch (BluetoothStateException ex) {
            ex.printStackTrace();
        }
    }

    public RFCommRPCServer(LinkedHashMap<String, Object> objectLookup, LinkedHashMap<String, Schema> lookupSchemaMap, TreeSet<String> lookups,String connectionURL) {
        super(objectLookup, lookupSchemaMap, lookups, connectionURL);
        mapper = new ObjectMapper();
        try {
            LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
        } catch (BluetoothStateException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 
     * @param connectionURL 
     */
    @Override
    protected final void bind(String connectionURL) {
        try {
            uuid = new UUID("8848",true); 
            this.connectionURL = "btspp://localhost:" + uuid.toString() +
                    ";name=RFCommPCServer";
            service = 
                    (StreamConnectionNotifier) Connector.open( this.connectionURL );
        } catch (IOException ex) {
            throw new RPCException("Error while binding to local bluetooth stack "+ex.getMessage());
        }
    }
    /**
     * responding to request
     * @param m 
     */
    @Override
    protected void send(Message m) {
        if(dev!=null){
            try {
                String jsonResp= mapper.writeValueAsString(m);
                oswriter.write(jsonResp);
                if(logger.isDebugEnabled())
                    logger.debug("Response sent: "+jsonResp);
                insreader.close();
                oswriter.close();
                conn.close();
                
            } catch (Exception ex) {
                throw new RPCException("Error while sending "+ ex.getMessage());
            }
        }   
        else{
            throw new RPCException("Cannot send response OutpustStream not initialized");
        }
        
    }

    /**
     * receiving request
     * @return 
     */
    @Override
    protected Message recv() {
        try {
            conn = (StreamConnection) service.acceptAndOpen();
            oswriter = new PrintWriter(conn.openOutputStream());
            insreader = new BufferedReader(new InputStreamReader(conn.openInputStream()));
            dev = RemoteDevice.getRemoteDevice(conn);
            
            String jsonReq=insreader.readLine();
            if(logger.isDebugEnabled())
                    logger.debug("Request received: "+jsonReq);
            return mapper.readValue(jsonReq, Message.class);
            
        } catch (IOException ex) {
            throw new RPCException("Error while receiving data: "+ex.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            service.close();
        } catch (IOException ex) {
            throw new RPCException("Error while closing service "+ex.getMessage());
        }
    }

    
    
}
