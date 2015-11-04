/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package golfito;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 *
 * @author Alumno
 */
public class GolfitoComponent extends JComponent{
    
    private Graphics brocha;
    private Color colorPasto;
    private Color colorBandera;
    private boolean relleno;
    private int figura;
    
    public GolfitoComponent(){
        super();
        colorPasto = Color.green;
        colorBandera = Color.red;
        relleno = true;
        figura=0;
    }
    
    @Override
    public void paint(Graphics g){
        brocha = g;
        g.setColor(colorPasto);
        g.fillOval(10, 140, 300, 150);
        g.setColor(Color.black);
        g.fillOval(65, 210, 30, 20);
        g.setColor(Color.gray);
        g.fillRect(80, 50, 10, 170);
        g.setColor(colorBandera);
        if(relleno){
            switch(figura){
                case 0: g.fillRect(90, 50, 106, 56);
                    break;
                case 1: g.fillPolygon(new int[] {90, 90, 196}, new int[] {50, 106, 78}, 3);
                    break;
                case 2: g.fillPolygon(new int[] {90, 90, 196, 143, 196}, new int[] {50, 106, 106, 78, 50}, 5);
            }
        }else{
            switch(figura){
                case 0: g.drawRect(90, 50, 106, 56);
                    break;
                case 1: g.drawPolygon(new int[] {90, 90, 196}, new int[] {50, 106, 78}, 3);
                    break;
                case 2: g.drawPolygon(new int[] {90, 90, 196, 143, 196}, new int[] {50, 106, 106, 78, 50}, 5);
            }
        }
        g.setColor(Color.white);
        g.fillOval(240, 220, 20, 20);
    }
    
    public void cambiarPasto(int i){
        colorPasto = i==0? Color.green : (i==1 ? Color.red : Color.blue);
        this.repaint();
    }
    
    public void cambiarBandera(int i){
        colorBandera =  i==0? Color.green : (i==1 ? Color.red : Color.blue);
        this.repaint();
    }
    
    public void rellenoBandera(boolean b){
        relleno = b;
        this.repaint();
    }
    
    public void figuraBandera(int f){
        this.figura = f;
        repaint();
    }
    
}
