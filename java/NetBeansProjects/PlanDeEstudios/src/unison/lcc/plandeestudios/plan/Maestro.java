/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unison.lcc.plandeestudios.plan;

/**
 *
 * @author nancio
 */
public class Maestro {
    
    private final String nombre;
    private final String expediente;
    private final String curp;
    private final String correo;
    
    public Maestro(String nombre, String expediente, String curp, String correo){
        this.nombre = nombre;
        this.expediente = expediente;
        this.curp = curp;
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombre+"|"+expediente+"|"+curp+"|"+correo;
    }
    
}
