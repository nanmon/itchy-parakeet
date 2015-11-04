/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package figurasgeometricas.figuras;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author nancio
 */
public abstract class Figura {
    
    protected Point inicio;
    protected Point fin;
    protected int grosor;
    protected Color color;
    
    public Figura(Point a, Point b, int grosor, Color color){
        this.inicio = new Point(Math.min(a.x,b.x), Math.min(a.y, b.y));
        this.fin = new Point(Math.max(a.x,b.x), Math.max(a.y, b.y));;
        this.grosor = grosor;
        this.color = color;
    }
    
    abstract public void pintar(Graphics g);
}
