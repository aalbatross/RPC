/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albatross.bluetooth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

/**
 *
 * @author iamrp
 */
public class BluetoothRemoteDeviceDiscovery {

    static ArrayList<RemoteDevice> remoteDevices = new ArrayList<RemoteDevice>();
    static ArrayList<ServiceRecord> servicesAvailable = new ArrayList<ServiceRecord>();

    public static void getMyLocalBluetoothDetails() throws BluetoothStateException {
        System.out.println("************************************************************");
        System.out.println("Local Bluetooth device details");
        LocalDevice device = LocalDevice.getLocalDevice();
        System.out.println("Bluetooth Device address :" + device.getBluetoothAddress());
        System.out.println("Discovery Status: " + (device.getDiscoverable() == DiscoveryAgent.NOT_DISCOVERABLE ? "Not Discoverable" : "Discoverable"));
        System.out.println("Friendly Name: " + device.getFriendlyName());
        System.out.println("Device Class :" + device.getDeviceClass());
        System.out.println("************************************************************");
    }

    public static void searchRemoteDevicesAround() throws Exception {
        System.out.println("************************************************************");
        final Object syncObject = new Object();
        synchronized (syncObject) {
            boolean st = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {

                public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                    try {
                        remoteDevices.add(btDevice);
                        System.out.println("Device Friendly Name:" + btDevice.getFriendlyName(true) + " ,Bluetooth Address: " + btDevice.getBluetoothAddress() + " Device class:" + cod);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                public void serviceSearchCompleted(int transID, int respCode) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                public void inquiryCompleted(int discType) {
                    System.out.println("Discovered all remote devices " + discType);
                    synchronized (syncObject) {
                        syncObject.notifyAll();
                    }
                    // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

            });
            syncObject.wait();
        }
        System.out.println("************************************************************");
    }

    public static void searchServices() throws Exception {
        System.out.println("************************************************************");
        final Object syncObject = new Object();

        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {

            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                System.out.println("TransID: " + transID);
                for (int i = 0; i < servRecord.length; i++) {
                    System.out.println("URL:" + servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false));
                    int[] attrs = servRecord[i].getAttributeIDs();
                    for (int attr : attrs) {
                        System.out.println("attr:" + attr + " value: " + servRecord[i].getAttributeValue(attr).getValue().toString());
                    }
                    servicesAvailable.add(servRecord[i]);
                    System.out.println("<<<<");
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
                System.out.println("Service search completed TransID:" + transID + " respCode:" + respCode);
                synchronized (syncObject) {
                    syncObject.notifyAll();
                }
            }

            public void inquiryCompleted(int discType) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };

        for (int i = 0; i < remoteDevices.size(); i++) {
            synchronized (syncObject) {
                System.out.println("Discovering all services on " + remoteDevices.get(i));
                LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(new int[]{0x0100}, new UUID[]{new UUID(0x1106)}, remoteDevices.get(i), listener);
                syncObject.wait();
            }
        }

        System.out.println("************************************************************");
    }

    public static void main(String[] args) throws Exception {

        if (LocalDevice.isPowerOn()) {
            getMyLocalBluetoothDetails();
            searchRemoteDevicesAround();
            searchServices();
        } else {
            System.out.println("Power on blutooth");
        }

    }
}
