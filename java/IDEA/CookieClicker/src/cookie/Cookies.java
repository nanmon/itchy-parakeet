package cookie;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Created by nancio on 5/10/14.
 */
public class Cookies extends MIDlet implements CommandListener{

    private Display display;
    private Game gc;

    public Cookies(){
        gc = new Game();
        display = Display.getDisplay(this);
        gc.startGame();
    }

    protected void startApp() throws MIDletStateChangeException {
        display.setCurrent(gc);
    }

    protected void pauseApp() {
        display.setCurrent(null);
    }

    protected void destroyApp(boolean b) throws MIDletStateChangeException {

    }

    public void commandAction(Command command, Displayable displayable) {

    }
}
