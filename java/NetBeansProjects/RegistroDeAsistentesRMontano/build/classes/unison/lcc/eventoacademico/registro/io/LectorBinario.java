/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unison.lcc.eventoacademico.registro.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author Roberto Montaño
 */
public class LectorBinario extends ObjectInputStream{

    public LectorBinario(String archivo) throws IOException {
        super(new FileInputStream(new File(archivo)));
    }
    
    
    
}
