/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package figurasgeometricas.guis;

import figurasgeometricas.figuras.Figura;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author nancio
 */
public class DibujoPanel extends JPanel{
    
    private final ArrayList listaDeFiguras;
    
    public DibujoPanel(){
        this.setLayout(new FlowLayout());
        listaDeFiguras = new ArrayList();
        //No funciona porque se sobreescribe el metodo paint
        this.setBorder(BorderFactory.createTitledBorder("Dibujo"));        
    }
    
    public void agregarFigura(Figura fig){
        listaDeFiguras.add(fig);
    }
    
    @Override
    public void paint(Graphics g){
        ///Intento de TitledBorder (:
        g.setColor(Color.blue);
        g.drawRect(5,5,this.getWidth()-10, this.getHeight()-10);
        g.setColor(Color.black);
        g.drawString("Dibujo", 10, 10);
        /////////////
        Figura fig;
        for(int i=0; i<listaDeFiguras.size(); ++i){
            fig = (Figura)listaDeFiguras.get(i);
            fig.pintar(g);
        }
    }
    
}
