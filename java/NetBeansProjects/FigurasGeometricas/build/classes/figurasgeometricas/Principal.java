/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package figurasgeometricas;

import figurasgeometricas.eventos.EscuchadorDibujo;
import figurasgeometricas.eventos.EscuchadorVentana;
import figurasgeometricas.guis.*;
import java.awt.BorderLayout;
import javax.swing.JRootPane;

/**
 *
 * @author Roberto Montaño
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        VentanaPrincipal vp = new VentanaPrincipal();
        MenuPanel mp = new MenuPanel();
        DibujoPanel dp = new DibujoPanel();
        vp.add(mp, BorderLayout.WEST);
        vp.add(dp, BorderLayout.CENTER);
        EscuchadorDibujo ed = new EscuchadorDibujo(vp);
        EscuchadorVentana ev = new EscuchadorVentana();
        dp.addMouseListener(ed);
        vp.addWindowListener(ev);
        
        vp.setVisible(true);
    }
    
}
