/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peleadebandas;

import javax.swing.JLabel;

/**
 *
 * @author Roberto Montaño
 */
public class Celda extends JLabel{
    private static String[] BANDAS = {"blank.jpeg", "spidercholo.jpeg", "baticholo.png"};
    private Cholo cholo;
    private int indice;

    public Celda(int indice) {
        super();
        this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rsc/"+BANDAS[0])));
        this.indice = indice;
        this.setVerticalTextPosition(JLabel.TOP);
        this.setHorizontalTextPosition(JLabel.LEFT);
        this.setIconTextGap(-15);
    }

    public int getIndice() {
        return indice;
    }

    void setBlank() {
        this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rsc/"+BANDAS[0])));
        this.repaint();
        this.cholo=null;
        this.setText("  ");
    }

    void setCholo(Cholo cholo) {
        this.cholo = cholo;
        this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rsc/"+BANDAS[cholo.bando+1])));
        this.setText(cholo.vida+"");
        this.repaint();
    }

    public Cholo getCholo() {
        return cholo;
    }
    
    public void repintar(){
        this.setText(cholo.vida+"");
        this.repaint();
    }
}
