/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fabrica;

/**
 *
 * @author Roberto Montaño
 */
class Productor extends Thread{
    private Object objeto;
    private Almacen caja;

    public Productor(Almacen caja) {
        this.caja = caja;
    }
    
    public void run(){
        for(int i=0; i<100; ++i){
            caja.addObject(new Object());
            System.out.println("Producido.");
        }
    }
    
}
