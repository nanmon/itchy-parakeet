
import java.awt.*;
import javax.swing.*;

/**
 * Class AppletBJ - write a description of the class here
 * 
 * @author (your name) 
 * @version (a version number)
 */
public class AppletBJ extends JApplet
{
    // instance variables - replace the example below with your own
    private int num;
    private int opcion;
    private int dim;
    private int x[];
    private int y[];

    /**
     * Called by the browser or applet viewer to inform this JApplet that it
     * has been loaded into the system. It is always called before the first 
     * time that the start method is called.
     */
    public void init()
    {
        // this is a workaround for a security conflict with some browsers
        // including some versions of Netscape & Internet Explorer which do 
        // not allow access to the AWT system event queue which JApplets do 
        // on startup to check access. May not be necessary with your browser. 
        JRootPane rootPane = this.getRootPane();    
        rootPane.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
            
        // provide any initialisation necessary for your JApplet
        opcion = JOptionPane.showOptionDialog(
            this, 
            "Seleccione opcion a imprimir", 
            null, 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            new String[] {"Figura con lineas", "Figura personal (Diamante)"}, 
            -1
        );
        
        dim = (int)(1+Math.random()*6)*100;
    }

    /**
     * Called by the browser or applet viewer to inform this JApplet that it 
     * should start its execution. It is called after the init method and 
     * each time the JApplet is revisited in a Web page. 
     */
    public void start()
    {
        if(opcion==0){
            String cantidad = JOptionPane.showInputDialog(this,"Dimension del cuadro: "+dim+"x"+dim+"\nNumero de rectas:");
            num = Integer.parseInt(cantidad);
        }else if(opcion==1){
            String grados = JOptionPane.showInputDialog(this, "Dimension del cuadro: "+dim+"x"+dim+"\nGrados a girar:");
            num = Integer.parseInt(grados);
            x = new int[] {1,2,3,0,1,2,3,4,2};
            y = new int[] {0,0,0,1,1,1,1,1,3};
        }else{
            //
        }
        // provide any code requred to run each time 
        // web page is visited
    }

    /** 
     * Called by the browser or applet viewer to inform this JApplet that
     * it should stop its execution. It is called when the Web page that
     * contains this JApplet has been replaced by another page, and also
     * just before the JApplet is to be destroyed. 
     */
    public void stop()
    {
        // provide any code that needs to be run when page
        // is replaced by another page or before JApplet is destroyed 
    }

    /**
     * Paint method for applet.
     * 
     * @param  g   the Graphics object for this applet
     */
    public void paint(Graphics g)
    {
        if(opcion==0) dibujarConLineas(g);
        if(opcion==1) dibujarFiguraPersonal(g);
    }

    /**
     * Called by the browser or applet viewer to inform this JApplet that it
     * is being reclaimed and that it should destroy any resources that itint)(Math.random()*501)
     * has allocated. The stop method will always be called before destroy. 
     */
    public void destroy()
    {
        // provide code to be run when JApplet is about to be destroyed.
    }


    /**
     * Returns information about this applet. 
     * An applet should override this method to return a String containing 
     * information about the author, version, and copyright of the JApplet.
     *
     * @return a String representation of information about this JApplet
     */
    public String getAppletInfo()
    {
        // provide information about the applet
        return "Title:   \nAuthor:   Nancio\nManejo de applets";
    }


    /**
     * Returns parameter information about this JApplet. 
     * Returns information about the parameters than are understood by this JApplet.
     * An applet should override this method to return an array of Strings 
     * describing these parameters. 
     * Each element of the array should be a set of three Strings containing 
     * the name, the type, and a description.
     *
     * @return a String[] representation of parameter information about this JApplet
     */
    public String[][] getParameterInfo()
    {
        // provide parameter information about the applet
        String paramInfo[][] = {
                 {"firstParameter",    "1-10",    "description of first parameter"},
                 {"status", "boolean", "description of second parameter"},
                 {"images",   "url",     "description of third parameter"}
        };
        return paramInfo;
    }
    
    private void dibujarConLineas(Graphics brocha){
        brocha.setColor(new Color(0,0,0));
        brocha.drawString("Roberto Montaño", 0, 19);
        brocha.drawRect(20, 20, dim, dim);
        for(int i=0; i<num; ++i){
            brocha.setColor(new Color(i*255/num, i*255/num, i*255/num));
            brocha.drawLine(20, 20+i*dim/num, 20+(i*dim+1)/num, 20+dim);
            brocha.setColor(new Color(255-i*255/num, i*255/num, i*255/num));
            brocha.drawLine(20+i*dim/num, 20+dim, 20+dim, 20+dim-(i*dim+1)/num);
            brocha.setColor(new Color(i*255/num, 255-i*255/num, i*255/num));
            brocha.drawLine(20+dim, 20+dim-i*dim/num, 20+dim-(i*dim+1)/num, 20);
            brocha.setColor(new Color(i*255/num, i*255/num, 255-i*255/num));
            brocha.drawLine(20+(i*dim+1)/num, 20, 20, 20+dim-i*dim/num);
        }
    }
    
    private void dibujarFiguraPersonal(Graphics brocha){
        brocha.setColor(new Color(0));
        brocha.drawString("Figura Original", 0, 19);
        brocha.drawRect(20, 20, dim, dim);
        brocha.drawLine(20+dim/2, 20, 20+dim/2, dim+20);
        brocha.setColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
        brocha.drawLine(posx(8),posy(8),posx(3),posy(3));
        brocha.drawLine(posx(8),posy(8),posx(4),posy(4));
        brocha.drawLine(posx(8),posy(8),posx(5),posy(5));
        brocha.drawLine(posx(8),posy(8),posx(6),posy(6));
        brocha.drawLine(posx(8),posy(8),posx(7),posy(7));
        brocha.drawLine(posx(3),posy(3),posx(7),posy(7));
        brocha.drawLine(posx(3),posy(3),posx(0),posy(0));
        brocha.drawLine(posx(4),posy(4),posx(0),posy(0));
        brocha.drawLine(posx(5),posy(5),posx(0),posy(0));
        brocha.drawLine(posx(5),posy(5),posx(1),posy(1));
        brocha.drawLine(posx(5),posy(5),posx(2),posy(2));
        brocha.drawLine(posx(6),posy(6),posx(2),posy(2));
        brocha.drawLine(posx(7),posy(7),posx(2),posy(2));
        brocha.drawLine(posx(0),posy(0),posx(2),posy(2));
        
    }
    
    private int posx(int i){
        return 20+dim/20+x[i]*dim/10;
    }
    
    private int posy(int i){
        return 45+dim/20+y[i]*dim/10;
    }
}
