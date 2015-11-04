/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playground;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Roberto Montaño
 */
public class Ball implements Runnable{
    private final int radio;
    private double speed[];
    private final Color color;
    private final double position[];
    private final JPanel panel;
    private Thread hilo;
    
    public Ball(JPanel panel, Point start, Point end){
        this(panel);
        position[0]= end.x;
        position[1]= end.y;
        speed = new double[] {
            Math.sqrt(Math.pow(end.x-start.x, 2)+Math.pow(end.y-start.y, 2))/10,
            Math.atan2((-end.y+start.y),(-end.x+start.x))
        };
    }
    
    public Ball(JPanel panel){
        this.panel = panel;
        this.radio = (int)(Math.random()*5+5);
        this.position = new double[] {
                radio+(Math.random()*(panel.getWidth()-radio)), 
                radio+(Math.random()*(panel.getHeight()-radio/2))
        };
        this.speed = new double[] {3+Math.random()*5, Math.random()*Math.PI*2};
        this.color = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
    }

    public void setSpeed(double[] speed) {
        this.speed = speed;
    }
    
    public void start(){
        if(hilo == null){
            hilo = new Thread(this, "pelota");
            hilo.start();
        }
    }
    
    @Override
    public void run(){
        try {
            while(true){
                paint();
                Thread.sleep(17);
                depaint();
                move();
            }
        }catch (InterruptedException ex) {
                   Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void paint(){
        Graphics g = panel.getGraphics();
        g.setColor(color);
        g.fillOval((int)position[0]-radio, (int)position[1]-radio, 2*radio, 2*radio);
    }
    
    public void depaint(){
        Graphics g = panel.getGraphics();
        g.setColor(panel.getBackground());
        g.fillOval((int)position[0]-radio, (int)position[1]-radio, 2*radio, 2*radio);
    }
    
    public void move(){
        if(position[0]+radio>panel.getWidth() || position[0]-radio<0){
            speed[1] = Math.PI-speed[1];
            this.position[0] = position[0]<radio+1 ? radio+1 : panel.getWidth()-radio-1;
        }
        this.position[0] +=speed[0]*Math.cos(speed[1]);
        
        if(position[1] +radio>panel.getHeight() || position[1] -radio<0){
            speed[1] = -speed[1];
            this.position[1] = position[1] <radio+1 ? radio+1 : panel.getHeight()-radio-1;
        }
        this.position[1] +=speed[0]*Math.sin(speed[1]);
    }
    
}
