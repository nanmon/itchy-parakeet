/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fabrica;

import java.util.ArrayList;

/**
 *
 * @author Roberto Montaño
 */
public class Almacen {
    
    ArrayList caja = new ArrayList();

    public Object sacar() {
        return caja.remove(0);
    }

    void addObject(Object object) {
        caja.add(object);
    }
    
}
