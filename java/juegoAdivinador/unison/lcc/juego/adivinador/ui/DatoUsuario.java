package unison.lcc.juego.adivinador.ui;


/**
 * Se encarga de recibir datos del usuario, tales como su nombre o un numero.
 * 
 * @author (Roberto Montaño)
 * @version (27.08.2014)
 */
public class DatoUsuario
{
    // instance variables - replace the example below with your own
    String nombre;

    /**
     * Da la bienvenida al usuario y pide su nombre.
     */
    public void inicializar()
    {
        // put your code here
        java.util.Scanner escan = new java.util.Scanner(System.in);
        System.out.println("Bienvenido a Juego Adivinador."+ 
                           "Intenta adivinar el numero dentro de un rango");
        System.out.print("Nombre del Jugador: ");
        nombre = escan.next();
        
    }
    
    /**
     * Valida la captura del numero dado por el usuario.
     *
     * @return     Numero dado por el usuario.
     */
    public int pedirValor()
    {
        // put your code here
        java.util.Scanner escan = new java.util.Scanner(System.in);
        int numero=0;
        String entrada;
        do{
            System.out.print("Cual es el numero magico? ");
            entrada = escan.next();
            if(soloDigitos(entrada))
                numero = Integer.parseInt(entrada);
            else System.out.println("Valor invalido");
        }while(!soloDigitos(entrada));
        return numero;
    }
    
    /**
     * Revisa una cadena si solo contiene digitos.
     *
     * @param  s   Cadena a verificar si son solo digitos.
     * @return     si es o no lo es.
     */
    private boolean soloDigitos(String s)
    {
        // put your code here
        for(int i=0; i<s.length(); ++i)
            if((s.charAt(i)<'0' || s.charAt(i)>'9') && s.charAt(i)!='-') return false;
        return true;
    }
    
    /**
     * Se despide del usuario
     *
     */
    public void despedir()
    {
        // put your code here
        System.out.println("Felicidades "+nombre+"! Adivinaste el numero magico! yey");
        System.out.println("Hecho por Nan Monta~o");
    }

}
