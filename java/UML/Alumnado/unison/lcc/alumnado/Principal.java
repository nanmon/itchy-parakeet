package unison.lcc.alumnado;
import unison.lcc.alumnado.utilidades.*;


/**
 * Class Principal
 * La clase Principal debe crear una lista de alumnos (en este caso vamos a suponer
 * que son 100 alumnos los que se tienen en la carrera de LCC) Una vez creada la
 * lista, se solicita a Estadistico que genere el diagrama correspondiente.
 */
public class Principal {
  /**
   * Este metodo inicia la ejecucion principal del programa. Desde este metodo se
   * construyen los objetos necesarios para el proposito del proyecto asi como las
   * llamadas a cada una de las operaciones requeridas.
   * @param        args Arreglo de cadenas proporcionadas durante la ejecucion del
   * programa.
   */
  public static void main(String [] args)
  {
      ListaDeAlumnos lista = new ListaDeAlumnos();
      Estadistico estats = new Estadistico();
      estats.generarDiagrama(lista);
  }
}
