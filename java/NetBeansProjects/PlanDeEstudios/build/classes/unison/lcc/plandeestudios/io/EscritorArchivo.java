/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unison.lcc.plandeestudios.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author nancio
 */
public class EscritorArchivo extends FileWriter{
    
    
    public EscritorArchivo(String directorio) throws IOException{
        super(directorio, true);
    }
    
    public void guardarDatos(String datos){
        PrintWriter impresor = new PrintWriter(this);
        impresor.printf("%s %n", datos);
        impresor.close();
    }
    
}
