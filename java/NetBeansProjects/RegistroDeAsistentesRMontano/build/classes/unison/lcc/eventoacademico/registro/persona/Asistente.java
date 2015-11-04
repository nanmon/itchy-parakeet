/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unison.lcc.eventoacademico.registro.persona;

import java.io.Serializable;

/**
 *
 * @author Roberto Montaño
 */
public class Asistente implements Serializable{
    private final String nombre, apellido, curp;
    //getter, setter, nuevo, actualizar, jtextfield

    public Asistente(String nombre, String apellido, String curp) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.curp = curp;
    }
    
    public String obtenerNombreCompleto(){
        return nombre+" "+apellido;
    }

    public String getCurp() {
        return curp;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
    
    
}
