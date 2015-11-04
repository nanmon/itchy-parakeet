/**
	Clases y Objetos
	Nan Monta~o
	26 Ago 2014
*/

/*public char String.prototype.last(){
	return this.charAt(this.length()-1);
}*/

public class Alumno{
//Propiedades
	String nombre="Panchito";
	int edad=19, semestre=1;
	float peso=50;
	float [] calificacion={0,0,100};
//Metodos
	public void capturarCalificaciones(int nCalif){
		java.util.Scanner escan = new java.util.Scanner(System.in);
		for(int i=0; i<nCalif; ++i){
			System.out.print("Calificacion "+(i+1)+": ");
			calificacion[i]=escan.nextFloat();
		}
	}
}

public class Clases{
	public static void main(String args[]){
		
		//Nombre de la clase: Sustantivo, singular, mayusculas, camello
		//Atributos: Sustantivo, singular, minusculas, camello
		//Metodos: Verbos, minusculas, camello
	}
}