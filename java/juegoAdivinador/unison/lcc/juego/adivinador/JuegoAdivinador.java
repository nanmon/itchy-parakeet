package unison.lcc.juego.adivinador;


/**
 * Generador y verificador del numero al azar.
 * 
 * @author (Roberto Montaño)
 * @version (27.08.2014)
 */
public class JuegoAdivinador
{
    // instance variables - replace the example below with your own
    private int numeroMagico;

    /**
     * Crea el numero al azar y el rango.
     * 
     * @param  a   Limite inferior del rango.
     * @param  b   Limite superior del rango.
     */
    public void inicializar(int a, int b)
    {
        // put your code here
        numeroMagico=a+(int)(Math.random()*(b-a+1));
    }
    
    /**
     * Revisa si se ha adivinado el numero magico o si hay que cambiar el rango.
     *
     * @param  y   numero a comparar con numero magico
     * @return     0 si se adivina, 1 si necesita subir el valor, -1 si necesita un valor más pequeño.
     */
    public int verificar(int y)
    {
        // put your code here
        return numeroMagico>y ? 1 : (numeroMagico<y ? -1 : 0);
    }

}
