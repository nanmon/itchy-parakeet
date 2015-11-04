package unison.lcc.numeros;


/**
 * Class NumeroNatural
 * Autor Luis Roberto Montaño Valdez
 */
public class NumeroNatural {

  //
  // Fields
  //
  /** Valor maximo */
  public final static int MAXIMO = Integer.MAX_VALUE;
  /** Valor actual */
  protected int valor;
  
  //
  // Constructors
  //
  public NumeroNatural () {
      this.valor = (int)(Math.random()*MAXIMO);
  };
  
  public NumeroNatural(int val) {
      this();
      if(val>0) this.valor = val;
  }
  
  public NumeroNatural(String val){
      this(Integer.parseInt(val));
  }
  
  public NumeroNatural(NumeroNatural a){
      this(a.obtenerValor());
  }
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Get the value of valor
   * @return the value of valor
   */
  public int obtenerValor () {
    return valor;
  }

  //
  // Other methods
  //

  /**
   * Suma el valor de los objetos recibidos y regresa un objeto de tipo NumeroNatural con el
   * resultado obtenido, o null si no es una operación cerrada.
   * @return       NumeroNatural
   * @param        a Sumando.
   * @param        b Sumando.
   */
  public static NumeroNatural sumar(NumeroNatural a, NumeroNatural b)
  {
      if(MAXIMO - a.obtenerValor() - b.obtenerValor() < 0) return null;
      return new NumeroNatural(a.obtenerValor() + b.obtenerValor());
  }


  /**
   * Resta el valor del segundo objeto (Sustrayendo) al valor del primero (Minuendo),
   * y regresa un objeto de tipo NumeroNatural con el resultado obtenido, o null si no es
   * una operación cerrada.
   * @return       NumeroNatural
   * @param        a Minuendo
   * @param        b Sustrayendo
   */
  public static NumeroNatural restar(NumeroNatural a, NumeroNatural b)
  {
      if(a.obtenerValor() - b.obtenerValor() <= 0) return null;
      return new NumeroNatural(a.obtenerValor() - b.obtenerValor());
  }


  /**
   * Realiza un multiplicacion con los valores de los objetos recibidos y regresa un
   * objeto de tipo NumeroNatural con el resultado obtenido, o null si no es una operación
   * cerrada.
   * @return       NumeroNatural
   * @param        a Multiplicando.
   * @param        b Multiplicando.
   */
  public static NumeroNatural multiplicar(NumeroNatural a, NumeroNatural b)
  {
      long val=(long)a.obtenerValor()*b.obtenerValor();
      if(val>MAXIMO) return null;
      return new NumeroNatural((int)val);
  }


  /**
   * Divide el primer objeto (Dividendo) entre el segundo (Divisor), y regresa un
   * objeto de tipo NumeroNatural con el resultado obtenido, o null si no es una operación
   * cerrada.
   * @return       NumeroNatural
   * @param        a Dividendo.
   * @param        b Divisor.
   */
  public static NumeroNatural dividir(NumeroNatural a, NumeroNatural b)
  {
      return new NumeroNatural(a.obtenerValor()/b.obtenerValor());
  }


}
