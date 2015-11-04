/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speaker;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author Roberto Montaño
 */
public class Midlet extends MIDlet implements CommandListener{
    
    private Display display;
    private List list;
    
    public Midlet(){
        display = Display.getDisplay(this);
        list = new List("Start", List.EXCLUSIVE);
        Command cmd_ok = new Command("Ok", Command.OK, 1);
        list.addCommand(cmd_ok);
        list.setCommandListener(this);
    }

    public void startApp() {
        display.setCurrent(list);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if(c.getCommandType()==Command.OK){
            new BluetootClient(this);
        }
    }
    
    protected void setAlert(String info){
        Alert a = new Alert("INFO");
        a.setString(info);
        a.setTimeout(Alert.FOREVER);
        display.setCurrent(a);
    }
}
