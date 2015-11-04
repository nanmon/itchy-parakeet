/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fracciones;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Roberto Montaño Valdez (nancio)
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Fraccion a, b;
        int an, ad, bn, bd, err=0;
        boolean posible;
        do{
            posible = true;
            try {
                ad = (int)(Math.random()*9);
                an = (int)(Math.random()*9);
                bd = (int)(Math.random()*9);
                bn = (int)(Math.random()*9);
                a = new Fraccion(an, ad);
                b = new Fraccion(bn, bd);
                Fraccion.sumar(a, b);
            } catch (FraccionNoValidaException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                posible = false;
                a = null;
                b=null;
                ++err;
            } catch (ArithmeticException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                posible = false;
                b=null;
                a=null;
                ++err;
            }
        }while(!posible);
        Ventana jf = new Ventana();
        jf.add(new DibujoFraccion(a, b));
        Ventana.crearVentana(jf);
    }
    
}
