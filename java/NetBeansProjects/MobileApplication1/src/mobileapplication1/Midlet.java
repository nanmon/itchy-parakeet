/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication1;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author nancio
 */
public class Midlet extends MIDlet implements CommandListener {

    private Display display;
    private Command exitCommand;
    private Command storeCommand;
    private TextField input;
    private List list;
    private Cookie gc;
    private Graphics g;
    
    public void commandAction(Command command, Displayable displayable){
        if(displayable == gc){
            if(command == exitCommand) exitMIDlet();
        }
            
    }
    
    public void startApp() {
        //stringItem = new StringItem("Hello", "HoliSymbian!");
        //input = new TextField("Input", null, 30, TextField.ANY);
        /*
        list = new List("Choices", List.EXCLUSIVE);
        form = new Form(null, new Item[] {input});
        exitCommand = new Command("Exit", Command.EXIT, 1);
        getInput = new Command("Print", Command.ITEM, 2);
        list.append("One", null);
        list.append("Two", null);
        form.addCommand(exitCommand);
        form.setCommandListener(this);
        
        display = Display.getDisplay(this);
        display.setCurrent(form);
        */
        gc = new Cookie(false);
        exitCommand = new Command("Exit", Command.EXIT, 1);
        storeCommand = new Command("Store", Command.ITEM, 2);
        
        gc.addCommand(exitCommand);
        //gc.addCommand(storeCommand);
        gc.setCommandListener(this);
        display = Display.getDisplay(this);
        display.setCurrent(gc);
        gc.startGame();
        //this.exitMIDlet();
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }

    public void exitMIDlet(){
        display.setCurrent(null);
        notifyDestroyed();
    }
}

