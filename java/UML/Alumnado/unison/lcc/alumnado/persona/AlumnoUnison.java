package unison.lcc.alumnado.persona;


/**
 * Class AlumnoUnison
 */
public class AlumnoUnison extends Alumno {

  //
  // Fields
  //

  /**
   * Este valor representa el identificador del estudiante universitario.
   */
  protected String expediente;
  /**
   * Este valor representa el numero de creditos que el alumno ha cursado en su respectiva carrera.
   */
  private int creditos;
  /**
   * Indica el semestre de la carrera en la que se encuentra el alumno
   */
  private int semestre;
  
  //
  // Constructors
  //
  public AlumnoUnison() {}
  
  public AlumnoUnison (String expediente, int creditos, int semestre) 
  { 
    this.expediente = expediente;
    this.creditos = creditos;
    this.semestre = semestre;
  };
  
  //
  // Methods
  //

  /**
   * Este metodo retorna el promedio general que tiene actualmente el alumno.
   * 
   * @return       Promedio general del alumno.
   */
  public double obtenerPromedio()
  {
      
      String [] datos = this.obtenerDatos().split(",");
      return Double.parseDouble(datos[2]);
  }


  /**
   * Este metodo incrementa el numero de creditos de acuerdo al valor proporcionado.
   * Retorna la cantidad de creditos actuales despues de hacer el incremento
   * correspondiente.
   * @return       int
   * @param        incremento Cantidad que debe aumentar el numero de creditos del
   * alumno. return Cantidad de creditos del alumno
   */
  public int incrementarCreditos(int incremento)
  {
      this.creditos+=incremento;
      return this.creditos;
  }


}
