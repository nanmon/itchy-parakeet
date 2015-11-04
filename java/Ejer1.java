/**
*	Trabajos en clase
*	Nan Monta~o
*	25/Ago/2014
*/

public class Ejer1{
	public static void main(String args[]){
		//manipular las cadenas recibidas del usuario (ejecucion)
		// saber el tamanio del arreglo
		System.out.println("Numero de cadenas: "+args.length);
		for(int i=0; i<args.length; ++i){
			System.out.print( (i+1) +") "+ args[i]);
			System.out.println( " ("+args[i].length()+" caracteres)");
		}
		System.out.println("\tHecho por Nan");
	}
} 
