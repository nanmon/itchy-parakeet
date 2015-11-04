/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fracciones;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nancio
 */
public class DibujoFraccion extends javax.swing.JComponent{
    
    private Fraccion a;
    private Fraccion b;
    
    public DibujoFraccion(Fraccion a, Fraccion b){
        super();
        this.setSize(400, 300);
        this.a = a;
        this.b = b;
    }
    
    public DibujoFraccion(){
        super();
        try {
            this.a = new Fraccion(3, 7);
            this.b = new Fraccion(2, 6);
        } catch (Exception ex) {
            Logger.getLogger(DibujoFraccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void paintComponent(java.awt.Graphics g){
        Fraccion r;
        try{
            r = Fraccion.sumar(a, b);
        }catch(Exception err){
            r=null;
        }
        int mcd=1;
        for(int i=2; i<=Math.sqrt(r.obtenerDenominador());++i)
            if(r.obtenerDenominador()%i==0) mcd = i;
        
        int borde = 10;
        int extra = 50;
        int k;
        if(r.obtenerNumerador()==0) k=1;
        else k = (a.obtenerNumerador()*b.obtenerDenominador()+b.obtenerNumerador()*a.obtenerDenominador())/r.obtenerNumerador();
        
        g.setColor(Color.blue);
        
        int a_k = a.obtenerNumerador()*b.obtenerDenominador()/k;
        
        for(int i=0; mcd*i<a_k; ++i){
            for(int j=0; j<mcd && mcd*i+j<a_k; ++j){
                g.fillRect(
                    borde+j*(getWidth()/3-2*borde)/mcd,
                    borde+i*(getHeight()-2*borde-extra)/(r.obtenerDenominador()/mcd),
                    (getWidth()/3-2*borde)/mcd,
                    (getHeight()-2*borde-extra)/(r.obtenerDenominador()/mcd)
                );
            }
        }
        
        g.setColor(Color.red);
        
        int b_k = b.obtenerNumerador()*a.obtenerDenominador()/k;
        
        for(int i=0; mcd*i<b_k; ++i){
            for(int j=0; j<mcd && mcd*i+j<b_k; ++j){
                g.fillRect(
                    borde+getWidth()/3+j*(getWidth()/3-2*borde)/mcd,
                    borde+i*(getHeight()-2*borde-extra)/(r.obtenerDenominador()/mcd),
                    (getWidth()/3-2*borde)/mcd,
                    (getHeight()-2*borde-extra)/(r.obtenerDenominador()/mcd)
                );
            }
        }
        
        for(int i=0; mcd*i<r.obtenerNumerador(); ++i){
            for(int j=0; j<mcd && mcd*i+j<r.obtenerNumerador(); ++j){
                g.setColor(mcd*i+j<a_k ? Color.blue : Color.red);
                g.fillRect(
                    borde+2*getWidth()/3+j*(getWidth()/3-2*borde)/(mcd),
                    borde+i*(getHeight()-2*borde-extra)/(r.obtenerDenominador()/mcd),
                    (getWidth()/3-2*borde)/mcd,
                    (getHeight()-2*borde-extra)/(r.obtenerDenominador()/mcd)
                );
            }
        }
        
        g.setColor(Color.black);
        
        for(int i=0; i<=mcd;++i){
            int pos = borde+i*(getWidth()/3-2*borde)/mcd;
            g.drawLine(pos, borde, pos, getHeight()-extra-borde);
            pos = getWidth()/3+borde+i*(getWidth()/3-2*borde)/mcd;
            g.drawLine(pos, borde, pos, getHeight()-extra-borde);
            pos = 2*getWidth()/3+borde+i*(getWidth()/3-2*borde)/mcd;
            g.drawLine(pos, borde, pos, getHeight()-extra-borde);
        }
        for(int i=0; i<=r.obtenerDenominador()/mcd;++i){
            int pos = borde+i*(getHeight()-2*borde-extra)/(r.obtenerDenominador()/mcd);
            g.drawLine(borde, pos, getWidth()/3-borde, pos);
            g.drawLine(getWidth()/3+borde, pos, 2*getWidth()/3-borde, pos);
            g.drawLine(2*getWidth()/3+borde, pos, getWidth()-borde, pos);
        }
        
        g.drawString("+", getWidth()/3-borde/2, borde+(getHeight()-2*borde-extra)/2);
        g.drawString("=", 2*getWidth()/3-borde/2, borde+(getHeight()-2*borde-extra)/2);
        
        g.drawString("+", getWidth()/3-borde/2, getHeight()-extra+borde+5);
        g.drawString("=", 2*getWidth()/3-borde/2, getHeight()-extra+borde+5);
        
        //fraccion a
        g.drawString(a.obtenerNumerador()+"", getWidth()/6, getHeight()-extra+borde);
        g.drawString("__", getWidth()/6, getHeight()-extra+borde+5);
        g.drawString(a.obtenerDenominador()+"", getWidth()/6, getHeight()-extra/2+borde);
        
        //fraccion b
        g.drawString(b.obtenerNumerador()+"", getWidth()/2, getHeight()-extra+borde);
        g.drawString("__", getWidth()/2, getHeight()-extra+borde+5);
        g.drawString(b.obtenerDenominador()+"", getWidth()/2, getHeight()-extra/2+borde);
        
        //fraccion r
        g.drawString(r.obtenerNumerador()+"", 5*getWidth()/6, getHeight()-extra+borde);
        g.drawString("__", 5*getWidth()/6, getHeight()-extra+borde+5);
        g.drawString(r.obtenerDenominador()+"", 5*getWidth()/6, getHeight()-extra/2+borde);
        
    }
}
