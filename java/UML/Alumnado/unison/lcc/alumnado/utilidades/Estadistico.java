package unison.lcc.alumnado.utilidades;
import unison.lcc.alumnado.utilidades.ListaDeAlumnos;


/**
 * Class Estadistico
 * La clase Estadistico se encarga de generar un arreglo de frecuencias para la
 * cantidad de alumnos en cada semestre, y graficar estas frecuencias. Se tiene un
 * arreglo de 10 (indicando cada semestre), con la cantidad de alumnos que cursan
 * cada uno de los semestres.
 */
public class Estadistico {

  //
  // Fields
  //

  /**
   * Arreglo que representa la cantidad de alumnos existentes en cada semestre.
   */
  private int[] frecuencias;
  
  //
  // Constructors
  //
  public Estadistico () { };
  
  //
  // Methods
  //

  /**
   * Esta clase se encarga de desplegar un listado de la cantidad de alumnos
   * inscritos en cada semestre. La salida debe ser algo similar a lo siguiente:
   * Semestre I: xxxxxxxxxxxxxxxxxxxx (20) Semestre II: xxxxxxxxxx (10) Semestre III:
   * xxxxxxxxxxxxxxxx (16) Semestre IV: xxxxxxxxxxxxxxxxxx (18) Semestre V:
   * xxxxxxxxxxxxxx (14) Semestre VI: xxxxxxxxxx (10) Semestre VII: xxxxxxxxxxxx (12)
   * Semestre VIII: (0) Semestre IX: (0) Semestre X: (0) 
   * @param        lista Lista de alumnos.
   */
  public void generarDiagrama(ListaDeAlumnos lista)
  {
      this.generarFrecuencia(lista);
      System.out.print("Semestre I: ");
      for(int i=0; i<frecuencias[0]; ++i) System.out.print("x");
      System.out.print("\nSemestre II: ");
      for(int i=0; i<frecuencias[1]; ++i) System.out.print("x");
      System.out.print("\nSemestre III: ");
      for(int i=0; i<frecuencias[2]; ++i) System.out.print("x");
      System.out.print("\nSemestre IV: ");
      for(int i=0; i<frecuencias[3]; ++i) System.out.print("x");
      System.out.print("\nSemestre V: ");
      for(int i=0; i<frecuencias[4]; ++i) System.out.print("x");
      System.out.print("\nSemestre VI: ");
      for(int i=0; i<frecuencias[5]; ++i) System.out.print("x");
      System.out.print("\nSemestre VII: ");
      for(int i=0; i<frecuencias[6]; ++i) System.out.print("x");
      System.out.print("\nSemestre VIII: ");
      for(int i=0; i<frecuencias[7]; ++i) System.out.print("x");
      System.out.print("\nSemestre IX: ");
      for(int i=0; i<frecuencias[8]; ++i) System.out.print("x");
      System.out.print("\nSemestre X: ");
      for(int i=0; i<frecuencias[9]; ++i) System.out.print("x");
  }


  /**
   * Este metodo se encarga de obtener la cantidad de alumnos que existen en cada
   * semestre.
   * @param        lista Lista de alumnos.
   */
  public void generarFrecuencia(ListaDeAlumnos lista)
  {
      this.frecuencias = new int[10];
      for(int i=0; i<lista.obtenerLista().length; ++i){
          ++frecuencias[lista.obtenerLista()[i].obtenerSemestre()-1];
      }
  }


}
