/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication2;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author nancio
 */
public class Midlet extends MIDlet implements CommandListener{

    private CookieFloat a;
    private CookieFloat b;
    private CookieFloat c;
    private CookieFloat d;
    private Display display;
    private Form form;
    private Command exitCommand;
    private StringItem stringItem;
    
    public void commandAction(Command command, Displayable displayable){
        if(displayable == form)
            if(command == exitCommand)
                exitMIDlet();
    }
    
    public void startApp() {
        a = new CookieFloat("123456789123","0");
        b = new CookieFloat("115", "0");
        c = new CookieFloat("11999", "");
        d = new CookieFloat(1);
        
        stringItem = new StringItem("a<=a", a.gt(a)+"");
        form = new Form(null, new Item[] {stringItem});
        exitCommand = new Command("Exit", Command.EXIT, 1);
        form.addCommand(exitCommand);
        form.setCommandListener(this);
        
        display = Display.getDisplay(this);
        display.setCurrent(form);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    public void exitMIDlet() {
        display.setCurrent(null);
        notifyDestroyed();
    }
}
