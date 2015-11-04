/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package figurasgeometricas.guis;

import figurasgeometricas.figuras.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author nancio
 */
public class MenuPanel extends JPanel{
    
    private final JRadioButton linea;
    private final JRadioButton rectangulo;
    private final JRadioButton ovalo;
    private ButtonGroup bg;
    private final JComboBox colores;
    private final JSpinner grosor;
    
    public MenuPanel(){
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        Dimension d = new Dimension(120, 320);
        this.setBounds(0, 0, 120, 320);
        this.setPreferredSize(d);
        this.setMaximumSize(d);
        this.setMinimumSize(d);
        linea = new JRadioButton("Linea", true);
        rectangulo = new JRadioButton("Rectangulo");
        ovalo = new JRadioButton("Ovalo");
        bg = new ButtonGroup();
        String items[] = {"Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Morado"};
        colores = new JComboBox(items);
        grosor = new JSpinner(new SpinnerNumberModel(1, 0, 50, 1));
        bg.add(linea);
        bg.add(rectangulo);
        bg.add(ovalo);
        this.add(linea);
        this.add(rectangulo);
        this.add(ovalo);
        this.add(colores);
        this.add(grosor);
        this.setBorder(BorderFactory.createTitledBorder("Menu"));
    }
    
    public Figura crearFigura(Point a, Point b){
        Color clr = Color.red;
        switch(colores.getSelectedIndex()){
            case 1: clr = Color.orange; break;
            case 2: clr = Color.yellow; break;
            case 3: clr = Color.green; break;
            case 4: clr = Color.blue; break;
            case 5: clr = Color.magenta; break;
        }
        int grs = (int)grosor.getValue();
        
        return linea.isSelected() ? new Linea(a, b, grs, clr) : (ovalo.isSelected() ? new Ovalo(a, b, grs, clr) : new Rectangulo(a, b, grs, clr));
    }
}
