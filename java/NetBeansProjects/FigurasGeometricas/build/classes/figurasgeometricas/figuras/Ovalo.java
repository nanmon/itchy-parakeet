/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package figurasgeometricas.figuras;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author nancio
 */
public class Ovalo extends Figura{
    
    public Ovalo(Point a,Point b, int grosor, Color color){
        super(a, b, grosor, color);
    }

    @Override
    public void pintar(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(grosor));
        g2d.drawOval(inicio.x, inicio.y, fin.x-inicio.x, fin.y-inicio.y);
    }
    
}
