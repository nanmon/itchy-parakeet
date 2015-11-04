package unison.lcc.alumnado.utilidades;
import unison.lcc.alumnado.persona.AlumnoLcc;


/**
 * Class ListaDeAlumnos
 * La clase ListaDeAlumnos se encarga de mantener una lista de alumnos. En este
 * problema se elaborara una lista de 100 alumnos.
 */
public class ListaDeAlumnos {

  //
  // Fields
  //

  private AlumnoLcc[] lista;
  
  //
  // Constructors
  //
  /**
   * El metodo inicializar se encarga de generar la lista de alumnos; 
   * cada atributo de cada alumno debe generarse en forma aleatoria. 
   * Los datos deben apegarse a a realidad, por ejemplo, 
   * - La longitud de los expedientesdebe ser entre 8 y 10 ambos inclusive. 
   * - El rango de semestres debe estar entre [1,10] 
   * - No existe cantidad maxima de creditos. 
   * - El promedio debe estar entre [0.0, 100.0]. 
   * - El nombre debe ser generado al azar, generando una letra mayuscula al inicio 
   * y minusculas despues (se deben generar al azar, nombres de longitud entre 5 y 10 caracteres, 
   * puras letras)
   */
  public ListaDeAlumnos () 
  {
    this.lista = new AlumnoLcc[100];
    String nombre, expediente;
    int edad, creditos, semestre;
    double promedio;
    for(int i=0; i<100; ++i){
        nombre=Character.toString((char)(65+(int)(Math.random()*26)));
        for(int j=5+(int)(Math.random()*6); j>0; --j){
            nombre+=Character.toString((char)(97+(int)(Math.random()*26)));
        }
        nombre+=" "+Character.toString((char)(65+(int)(Math.random()*26)));
        for(int j=5+(int)(Math.random()*6); j>0; --j){
            nombre+=Character.toString((char)(97+(int)(Math.random()*26)));
        }
        edad = 18+(int)(Math.random()*13);
        promedio = ((int)(Math.random()*1001))/10;
        expediente = "";
        for(int j=8+(int)(Math.random()*3); j>0; --j){
            expediente+=Character.toString((char)(48+(int)(Math.random()*10)));
        }
        creditos = (int)(Math.random()*500);
        semestre = 1+ (int)(Math.random()*10);
        lista[i] = new AlumnoLcc(expediente, creditos, semestre);
        lista[i].ponerDatos(nombre, edad, promedio);
    }
  };
  
  //
  // Methods
  //

  /**
   * El método obtenerLista debe retornar el arreglo de alumnos de la clase. 
   * @return Arreglo de los alumnos generados.
   */
  public AlumnoLcc[] obtenerLista () {
    return lista;
  }

  //
  // Other methods
  //

}
