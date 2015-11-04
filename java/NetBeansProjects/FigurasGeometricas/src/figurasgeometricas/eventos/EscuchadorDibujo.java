/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package figurasgeometricas.eventos;

import figurasgeometricas.figuras.Figura;
import figurasgeometricas.guis.DibujoPanel;
import figurasgeometricas.guis.MenuPanel;
import figurasgeometricas.guis.VentanaPrincipal;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JRootPane;

/**
 *
 * @author nancio
 */
public class EscuchadorDibujo implements MouseListener, ActionListener{
    
    private final VentanaPrincipal vp;
    private Point a;
    
    public EscuchadorDibujo(VentanaPrincipal vp){
        this.vp = vp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        a = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        MenuPanel mp = (MenuPanel)vp.getContentPane().getComponent(0);
        DibujoPanel dp = (DibujoPanel)vp.getContentPane().getComponent(1);
        Figura fig = mp.crearFigura(a, e.getPoint());
        dp.agregarFigura(fig);
        dp.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    
}
