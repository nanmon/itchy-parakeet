/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roberto Montaño
 */
public class Cliente {

    public void enter(){
        try(Socket enchufe = new Socket("148.225.71.111", 1995)) {
                System.out.println(enchufe.getLocalAddress());
                ObjectInputStream input = new ObjectInputStream(enchufe.getInputStream());
                System.out.println((String)input.readObject());
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        new Cliente().enter();
    }
    
}
