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
public class Consumidor implements Runnable{
    
    private Object objetillo;
    private final Almacen almacen;

    public Consumidor(Almacen almacen) {
        this.almacen = almacen;
    }
    
    public void comprar(){
        this.objetillo = almacen.sacar();
    }
    
    @Override
    public void run() {
        for(int i=0; i<100; ++i){ 
            comprar();
            System.out.println("Consumido: "+this.objetillo);
        }
    }
    
    
    
}
