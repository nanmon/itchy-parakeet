/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speaker;

import java.util.Enumeration;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 *
 * @author Roberto Montaño
 */
class BluetootClient implements Runnable{
    
    private InquiryListener inqListen;
    private ServiceListener servListen;
    private boolean listening = true;
    private Midlet midlet;
    private String deviceName;
    private L2CAPConnection con;

    public BluetootClient(Midlet aThis){
        this.midlet = aThis;
        new Thread(this).start();
    }

    public void run() {
        System.out.println("Starting...");
        try{
            LocalDevice device = LocalDevice.getLocalDevice();
            DiscoveryAgent agent = device.getDiscoveryAgent();
            device.setDiscoverable(DiscoveryAgent.LIAC);
            inqListen = new InquiryListener();
            synchronized(inqListen){
                agent.startInquiry(DiscoveryAgent.LIAC, inqListen);
                try{
                    inqListen.wait();
                }catch(InterruptedException e){}
            }
            
            Enumeration devices = inqListen.cached_devices.elements();
            
            UUID[] u = new UUID[] { ""};
        }catch(Exception e){}
    }

    private static class InquiryListener implements DiscoveryListener {

        public InquiryListener() {
        }

        public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public void serviceSearchCompleted(int transID, int respCode) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public void inquiryCompleted(int discType) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private static class ServiceListener {

        public ServiceListener() {
        }
    }
    
}
