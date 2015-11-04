/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package golfito;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

/**
 *
 * @author Alumno
 */
public class EscuchadorMagico implements MouseListener, ActionListener{
    
    private Point posicion;
    private int clicks;
    private MyLittleJFrame ventana;
    
    public EscuchadorMagico(MyLittleJFrame jf){
        clicks = 0;
        ventana = jf;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if(clicks==0){ 
            ++clicks;
            posicion = e.getPoint();
        }else if(posicion.y==e.getPoint().y && posicion.x==e.getPoint().x){
            clicks=2;
        }else{
            clicks=1;
            posicion = e.getPoint();
        }if(clicks==2){
            if(posicion.y>140){
                String opciones[]= {"Verde", "Rojo", "Azul"};
                int colorPasto = JOptionPane.showOptionDialog(
                        ventana, 
                        "Seleccione color para el pasto.", 
                        "Cambiar color de pasto", 
                        JOptionPane.OK_CANCEL_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE, 
                        null, 
                        opciones, 
                        -1);
                if(colorPasto>=0){
                    ventana.getGolfito().cambiarPasto(colorPasto);
                    ventana.repaint();
                }
            }else if(posicion.y>77&&posicion.y<131 && posicion.x>93&&posicion.x<197){
                String opciones[]= {"Verde", "Rojo", "Azul"};
                int colorBandera = JOptionPane.showOptionDialog(
                        ventana, 
                        "Seleccione color para la bandera.", 
                        "Cambiar color de bandera", 
                        JOptionPane.OK_CANCEL_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE, 
                        null, 
                        opciones, 
                        -1);
                if(colorBandera>=0){
                    ventana.getGolfito().cambiarBandera(colorBandera);
                    ventana.repaint();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if( o instanceof JCheckBox){
            JCheckBox jcb = (JCheckBox)o;
            ventana.getGolfito().rellenoBandera(jcb.isSelected());
        }if(o instanceof JRadioButton){
            if(((JRadioButton)(ventana.getContentPane().getComponent(2))).isSelected())
                ventana.getGolfito().figuraBandera(0);
            else if(((JRadioButton)(ventana.getContentPane().getComponent(3))).isSelected())
                ventana.getGolfito().figuraBandera(1);
            else ventana.getGolfito().figuraBandera(2);
        }
    }
    
}
