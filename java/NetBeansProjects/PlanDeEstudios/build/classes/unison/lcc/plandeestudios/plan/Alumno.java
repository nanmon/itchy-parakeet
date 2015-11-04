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
public class Alumno {
    
    private final String nombre;
    private final String expediente;
    private final String curso;
    private final String curp;
    private final String correo;
    
    public Alumno(String nombre, String expediente, int curso, String curp, String correo){
        this.nombre = nombre;
        this.expediente = expediente;
        this.curso = curso==0 ? "2014-2" : "2015-1";
        this.curp = curp;
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombre+"|"+expediente+"|"+curso+"|"+curp+"|"+correo;
    }
    
}
