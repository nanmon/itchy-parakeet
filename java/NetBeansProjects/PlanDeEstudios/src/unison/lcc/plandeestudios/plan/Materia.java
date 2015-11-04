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
public class Materia {
    
    private final String nombre;
    private final String clave;
    private final int eje;
    private final int creditos;
    private final int semestre;
    
    public Materia(String nombre, String clave, int eje, int creditos, int semestre){
        this.nombre = nombre;
        this. clave = clave;
        this.eje = eje;
        this.creditos = creditos;
        this.semestre = semestre;
    }

    @Override
    public String toString() {
        return nombre+"|"+clave+"|"+eje+"|"+creditos+"|"+semestre;
    }
    
    
    
}
