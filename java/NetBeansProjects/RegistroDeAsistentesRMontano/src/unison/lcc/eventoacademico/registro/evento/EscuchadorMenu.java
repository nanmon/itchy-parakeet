/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unison.lcc.eventoacademico.registro.evento;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import unison.lcc.eventoacademico.registro.gui.RegistroAsistenteGUI;

/**
 *
 * @author Roberto Montaño
 */
public class EscuchadorMenu implements ActionListener{
    
    private final RegistroAsistenteGUI ragui;
    
    public EscuchadorMenu(RegistroAsistenteGUI ragui){
        this.ragui = ragui;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() instanceof JMenuItem){
            if(((JMenuItem)ae.getSource()).getText().equals("Leer Archivo")){
                ragui.cambiarArchivo();
            }else{
                System.exit(0);
            }
        }
    }
    
}
