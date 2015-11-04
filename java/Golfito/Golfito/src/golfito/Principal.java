/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package golfito;

import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

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
        MyLittleJFrame jf = new MyLittleJFrame();
        EscuchadorMagico em = new EscuchadorMagico(jf);
        jf.addMouseListener(em);
        ((JCheckBox)(jf.getContentPane().getComponent(1))).addActionListener(em);
        ((JRadioButton)(jf.getContentPane().getComponent(2))).addActionListener(em);
        ((JRadioButton)(jf.getContentPane().getComponent(3))).addActionListener(em);
        ((JRadioButton)(jf.getContentPane().getComponent(4))).addActionListener(em);
        jf.setVisible(true);
        
        
    }
}
