/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peleadebandas;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Roberto Montaño
 */
class Cholo extends Thread{
    public int vida, arma, bando;//public
    private Celda celda;

    public Cholo(Celda celda) {
        super();
        this.vida = 10;
        this.arma = (int)(Math.random()*4);
        this.bando = (int)(Math.random()*2);
        this.celda = celda;
        celda.setCholo(this);//param Cholo, text vida
    }
    
    public Cholo(Celda celda, int bando){
        this(celda);
        this.bando=bando;
    }

    @Override
    public void run() {
        try{
            while(vida>2){
                Thread.sleep(500);

                int i = celda.getIndice()+((int)(Math.random()*3)-1)+((int)(Math.random()*3)-1)*8;
                if(i<0) i=56+celda.getIndice();
                if(i>=64) i%=8;
                Celda nueva = ((Celda)celda.getParent().getComponent(i));
                if(nueva.getCholo()==null){
                    celda.setBlank();
                    celda = nueva;
                    celda.setCholo(this);
                }else{
                    Cholo otroCholo = nueva.getCholo();
                    if(otroCholo.bando!=this.bando){
                        if(otroCholo.arma>this.arma) this.quitarVida();
                        else if(otroCholo.arma<this.arma ) otroCholo.quitarVida();
                        else{ 
                            this.quitarVida();
                            otroCholo.quitarVida();
                        }
                    }
                }
            }
            celda.setBlank();
        }catch(ArrayIndexOutOfBoundsException ex){
            Logger.getLogger(Cholo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cholo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void quitarVida() {
        --vida;
        if(vida<2) celda.setBlank();
        else{
             celda.repintar();
            this.setPriority(vida);
        }
    }
    
    
}
