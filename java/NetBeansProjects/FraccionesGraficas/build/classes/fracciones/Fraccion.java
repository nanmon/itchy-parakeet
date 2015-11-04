/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fracciones;

/**
 *
 * @author nancio
 */
public class Fraccion {
    private int numerador;
    private int denominador;
    
    public Fraccion(int n, int d) throws FraccionNoValidaException, ArithmeticException{
        if(n>=d) throw new FraccionNoValidaException();
        if(d==0) throw new ArithmeticException();
        numerador = n;
        denominador = d;
        this.simplificar();
    }
    
    public Fraccion(int n) throws FraccionNoValidaException, ArithmeticException{
        this(n, 1);
    }
    
    public int obtenerNumerador(){
        return numerador;
    } 
    
    public int obtenerDenominador(){
        return denominador;
    }
    
    public static Fraccion sumar(Fraccion a, Fraccion b) throws FraccionNoValidaException, ArithmeticException{
        int n = a.obtenerNumerador()*b.obtenerDenominador();
        n+=b.obtenerNumerador()*a.obtenerDenominador();
        int d = a.obtenerDenominador()*b.obtenerDenominador();
        return new Fraccion(n, d);
    }
    
    private void simplificar(){
        if(this.numerador==0){
            this.denominador=1;
            return;
        }
        for(int i=2; i<=this.numerador; ++i){
            if(this.numerador%i==0 && this.denominador%i==0){
                this.numerador/=i;
                this.denominador/=i;
                i=2;
            }
        }
    }
}
