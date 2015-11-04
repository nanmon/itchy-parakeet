package unison.lcc.numeros;


/**
 * Class NumeroEntero
 * Autor: Luis Roberto Montaño Valdez
 */
public class NumeroEntero extends NumeroNatural{

  //
  // Fields
  //

  /** Valor minimo*/
  public final static int MINIMO = Integer.MIN_VALUE;
  
  //
  // Constructors
  //
  public NumeroEntero () { 
      this.valor = (int)(Math.random()*((long)MAXIMO-MINIMO))+MINIMO;
  };
  
  public NumeroEntero(int a) {
      this.valor = a;
  }
  
  public NumeroEntero(String a) {
      this(Integer.parseInt(a));
  }
  
  public NumeroEntero(NumeroEntero a){
    this(a.obtenerValor());
  }
  
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
   * Suma el valor de los objetos recibidos y regresa un objeto de tipo NumeroEntero con el
   * resultado obtenido, o null si no es una operación cerrada.
   * @return       NumeroEntero
   * @param        a Sumando.
   * @param        b Sumando.
   */
  public static NumeroEntero sumar(NumeroEntero a, NumeroEntero b)
  {
      long val = (long)a.obtenerValor() + b.obtenerValor();
      if(val<MINIMO || val>MAXIMO) return null;
      return new NumeroEntero((int)val);
  }


  /**
   * Resta el valor del segundo objeto (Sustrayendo) al valor del primero (Minuendo),
   * y regresa un objeto de tipo NumeroEntero con el resultado obtenido, o null si no es
   * una operación cerrada.
   * @return       NumeroEntero
   * @param        a Minuendo
   * @param        b Sustrayendo
   */
  public static NumeroEntero restar(NumeroEntero a, NumeroEntero b)
  {
      return NumeroEntero.sumar(a, new NumeroEntero(-b.obtenerValor()));
  }


  /**
   * Realiza un multiplicacion con los valores de los objetos recibidos y regresa un
   * objeto de tipo NumeroEntero con el resultado obtenido, o null si no es una operación
   * cerrada.
   * @return       NumeroEntero
   * @param        a Multiplicando.
   * @param        b Multiplicando.
   */
  public static NumeroEntero multiplicar(NumeroEntero a, NumeroEntero b)
  {
      long val = (long)a.obtenerValor()*b.obtenerValor();
      if(val<MINIMO || val>MAXIMO) return null;
      return new NumeroEntero((int)val);
  }


  /**
   * Divide el primer objeto (Dividendo) entre el segundo (Divisor), y regresa un
   * objeto de tipo NumeroEntero con el resultado obtenido, o null si no es una operación
   * cerrada.
   * @return       NumeroEntero
   * @param        a Dividendo.
   * @param        b Divisor.
   */
  public static NumeroEntero dividir(NumeroEntero a, NumeroEntero b)
  {
      return new NumeroEntero(a.obtenerValor()/b.obtenerValor());
  }


}
