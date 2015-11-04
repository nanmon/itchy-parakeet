package unison.lcc.alumnado.persona;


/**
 * Class Alumno
 */
public class Alumno {

  //
  // Fields
  //

  /**
   * Representa el nombre del alumno.
   */
  protected String nombre;
  /**
   * Representa la edad del alumno.
   */
  private double edad;
  /**
   * Representa el promedio general de calificaciones del ciclo escolar.
   */
  private double promedio;
  
  //
  // Constructors
  //
  public Alumno () { };
  
  //
  // Methods
  //
  
  /**
   * Nuevo promedio que debe asignar al alumno
   * @param        promedio Nuevo promedio que debe asignar al alumno.
   */
  public void actualizarPromedio(double promedio)
  {
      this.promedio=promedio;
  }


  /**
   * Este metodo se encarga de obtener todos los elementos de la clase en el
   * siguiente orden: nombre, edad, promedio Cada dato debe ir separado por coma.
   * return Los atributos ordenados del alumno
   * @return       String
   */
  public String obtenerDatos()
  {
      return nombre+","+edad+","+promedio;
  }


  /**
   * Este metodo se encarga de asignar los vaores a los atributos de la clase.
   * @param        nombre Representa el nombre del alumno
   * @param        edad Representa la edad del alumno
   * @param        promedio Representa el promedio del alumno
   */
  public void ponerDatos(String nombre, int edad, double promedio)
  {
      this.nombre=nombre;
      this.edad=edad;
      this.promedio=promedio;
  }
}
