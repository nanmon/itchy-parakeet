/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbolic;

import javax.microedition.midlet.*;

/**
 * @author Roberto Montaño
 */
public class Midlet extends MIDlet {

    public void startApp() {
        Cookiefloat a = new Cookiefloat("15");
        Cookiefloat b = new Cookiefloat("1.15");
        for(int i=0;i<200; ++i){
            a = Cookiefloat.m(a, b);
            System.out.println(a.get());
        }
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
