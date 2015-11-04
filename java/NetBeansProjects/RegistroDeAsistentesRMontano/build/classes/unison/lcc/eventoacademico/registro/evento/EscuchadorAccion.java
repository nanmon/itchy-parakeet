/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unison.lcc.eventoacademico.registro.evento;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import unison.lcc.eventoacademico.registro.gui.RegistroAsistenteGUI;

/**
 *
 * @author Roberto Montaño
 */
public class EscuchadorAccion implements ActionListener{
    
    private final RegistroAsistenteGUI ragui;

    public EscuchadorAccion(RegistroAsistenteGUI ragui) {
        this.ragui = ragui;
    }
    
    

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() instanceof JButton){
            if(((JButton)ae.getSource()).getText().equals("Nuevo")){
                ragui.nuevoAsistente();
            }else{
                ragui.actualizarAsistente();
            }
        }
    }
    
}
