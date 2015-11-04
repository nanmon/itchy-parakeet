/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package figurasgeometricas.guis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author nancio
 */
public class VentanaPrincipal extends JFrame{
    
    public VentanaPrincipal(){
        this.setTitle("Dibujo de Roberto Montaño");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((d.width-800)/2, (d.height-600)/2, 800, 600);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        //this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}
