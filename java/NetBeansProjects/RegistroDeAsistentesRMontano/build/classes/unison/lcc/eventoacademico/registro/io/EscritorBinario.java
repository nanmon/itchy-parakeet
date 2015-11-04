/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unison.lcc.eventoacademico.registro.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 * @author Roberto Montaño
 */
public class EscritorBinario extends ObjectOutputStream{
    

    public EscritorBinario(String archivo) throws IOException {
        super(new FileOutputStream(new File(archivo)));
    }

    
    
    
}
