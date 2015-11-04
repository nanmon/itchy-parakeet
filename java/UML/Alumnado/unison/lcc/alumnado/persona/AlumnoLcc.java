package unison.lcc.alumnado.persona;


/**
 * Class AlumnoLcc
 */
public class AlumnoLcc extends AlumnoUnison {

  //
  // Fields
  //

  
  //
  // Constructors
  //
  public AlumnoLcc (String expediente, int creditos, int semestre) {
      super(expediente, creditos, semestre);
  };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  //
  // Other methods
  //

  /**
   * Este metodo se encarga de entregar el semestre actual del alumno. Para lograr
   * esto, es necesario calcular el semestre, para poder actualizar el semestre antes
   * de entregarlo. En otras palabras, este metodo debe mandar llamar al metodo de
   * calcularSemestre(). return Valor actual del semestre del alumno.
   * @return       int
   */
  public int obtenerSemestre()
  {
      return this.calcularSemestre();
  }


  /**
   * Este metodo se encarga de calcular y asignar el semestre en el cual debe
   * encontrarse el alumno. Esto se logra de acuerdo a las siguientes reglas: Primer
   * semestre: si la cantidad de creditos es menor que 39 Segundo semestre: si los
   * creditos estan en [39, 91] Tercer semestre: si los creditos estan en (91, 142]
   * Cuarto semestre: si los creditos estan en (142, 195] Quinto semestre: si los
   * creditos estan en (195, 243] Sexto semestre: si los creditos estan en (243, 291]
   * Septimo semestre: si los creditos estan en (291, 339] Octavo semestre: si la
   * cantidad de creditos es mayor que 339.
   * @return       Semestre obtenido de acuerdo a las reglas establecidas.
   */
  private int calcularSemestre()
  {
      int creditos = this.incrementarCreditos(0);
      if(creditos<39) return 1;
      if(creditos<=91) return 2;
      if(creditos<=142) return 3;
      if(creditos<=195) return 4;
      if(creditos<=243) return 5;
      if(creditos<=291) return 6;
      if(creditos<=339) return 7;
      return 8;
  }


}
