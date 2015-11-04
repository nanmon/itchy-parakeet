package unison.lcc.juego.adivinador;
import unison.lcc.juego.adivinador.ui.*;


/**
 * Juego adivinador: Genera un numero al azar que el usuario tiene que adivinar dado un rango. El
 * rango puede ser dado como parametro para la funcion main, si no sera usado el rango por default
 * [0, 50].
 * 
 * @author (Roberto Montaño)
 * @version (28.08.2014)
 */
public class Principal
{
    public static void main(String args[])
    {
        // put your code here
        try{
            if(args.length>0 && args.length!=2) throw new RuntimeException(); 
            int[] rango = {args.length==0 ? 0 :Integer.parseInt(args[0]),
                           args.length==0? 50 : Integer.parseInt(args[1])};
            if(rango[0]>rango[1]){
                int aux = rango[0];
                rango[0] = rango[1];
                rango[1] = aux;
            }
            DatoUsuario entrada = new DatoUsuario();
            JuegoAdivinador numeroMagico = new JuegoAdivinador();
            int numero, cambiarRango;
            numeroMagico.inicializar(rango[0], rango[1]);
            entrada.inicializar();
            do{
                System.out.println("El rango es ["+rango[0]+","+rango[1]+"].");
                numero = entrada.pedirValor();
                cambiarRango=numeroMagico.verificar(numero);
                if(cambiarRango<0 && rango[1]>=numero){ 
                    rango[1] = numero-1;
                    System.out.println("El numero magico es menor");
                }
                if(cambiarRango>0 && rango[0]<=numero){ 
                    rango[0] = numero+1;
                    System.out.println("El numero magico es mayor");
                }
            }while(cambiarRango!=0);
            
            entrada.despedir();
        }catch(Exception err){
            System.out.println("Error: argumentos invalidos.");
        }
    }
}
