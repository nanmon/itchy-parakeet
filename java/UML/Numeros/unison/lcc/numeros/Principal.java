package unison.lcc.numeros;


/*En este documento se presenta la clase Principal, ya actualizada
Asimismo se presenta la salida esperada al ejecutar este programa.
NO modificar esta clase, ajustar las clases de la tarea para que
este programa funcione y proporcione las salidas esperadas.
=========================================================
************  CLASE PRINCIPAL **************************
=========================================================
import unison.lcc.numeros.*;
*/
public class Principal{
    private Principal(){ }
    
    public static void main(String args[]){
        pruebaDeNaturales();
        pruebaDeEnteros();
    }
    
    private static void pruebaDeNaturales(){
        System.out.println("Pruebas de numeros NumeroNaturales:");
        //System.out.println("valorMaximo:"+Integer.MAX_VALUE);
        //System.out.println("2*valorMaximo:"+ (Integer.MAX_VALUE+Integer.MAX_VALUE));
        NumeroNatural[] numero = new NumeroNatural[4];
        numero[0] = new NumeroNatural(Integer.MAX_VALUE/2 + 5);
        numero[1] = new NumeroNatural(new NumeroNatural(Integer.MAX_VALUE/2 + 10));
        numero[2] = new NumeroNatural("12345");
        numero[3] = new NumeroNatural();

        NumeroNatural[] resultado = new NumeroNatural[9]; 
        String operacion[] = new String[9]; 
        //Casos de prueba para CERRADURA
        //n1 + n2 > maximo (null)
        resultado[0] = NumeroNatural.sumar(numero[0],numero[1]);
        operacion[0] = new String("("+numero[0].obtenerValor() + ") + (" + numero[1].obtenerValor() + ") = ");
        //n1 - n2 < 0 (null)
        resultado[1] = NumeroNatural.restar(numero[0],numero[1]);
        operacion[1] = new String("("+numero[0].obtenerValor() + ") - (" + numero[1].obtenerValor() + ") = ");
        //n1 - n2 = 0 (null)
        resultado[2] = NumeroNatural.restar(numero[0],numero[0]);
        operacion[2] = new String("("+numero[0].obtenerValor() + ") - (" + numero[0].obtenerValor() + ") = ");
        //n1*n2 > maximo (null)
        resultado[3] = NumeroNatural.multiplicar(numero[0],numero[1]);        
        operacion[3] = new String("("+numero[0].obtenerValor() + ") * (" + numero[1].obtenerValor() + ") = ");
        //n1/n2 = 0 (null)
        resultado[4] = NumeroNatural.dividir(numero[0],numero[1]);
        operacion[4] = new String("("+numero[0].obtenerValor() + ") / (" + numero[1].obtenerValor() + ") = ");
        //validos 
        resultado[5] = NumeroNatural.sumar(numero[0],numero[2]);
        operacion[5] = new String("("+numero[0].obtenerValor() + ") + (" + numero[2].obtenerValor() + ") = ");
        resultado[6] = NumeroNatural.restar(numero[0],numero[2]);
        operacion[6] = new String("("+numero[0].obtenerValor() + ") - (" + numero[2].obtenerValor() + ") = ");
        resultado[7] = NumeroNatural.multiplicar(numero[2],numero[2]);
        operacion[7] = new String("("+numero[2].obtenerValor() + ") * (" + numero[2].obtenerValor() + ") = ");
        resultado[8] = NumeroNatural.dividir(numero[1],numero[2]);
        operacion[8] = new String("("+numero[1].obtenerValor() + ") / (" + numero[2].obtenerValor() + ") = ");
        
        imprimir(resultado, operacion);
    }
    
    private static void pruebaDeEnteros(){
        System.out.println("\nPruebas de numeros Enteros:");
        NumeroEntero[] numero = new NumeroEntero[5];
        //numero[0] = new NumeroEntero(new NumeroNatural(Integer.MAX_VALUE/2 + 5)); Esa onda profe?
        numero[0] = new NumeroEntero(Integer.MAX_VALUE/2 +5);
        numero[1] = new NumeroEntero(new NumeroEntero(-Integer.MAX_VALUE/2 - 10));
        numero[2] = new NumeroEntero("12345");
        numero[3] = new NumeroEntero();
        numero[4] = new NumeroEntero(0);
        
        NumeroEntero[] resultado = new NumeroEntero[18]; 
        String operacion[] = new String[18]; 
        //Casos de prueba para CERRADURA
        //SUMA
        //n1 + n2 > maximo, ambos positivos grandes (null)
        resultado[0] = NumeroEntero.sumar(numero[0],numero[0]);
        operacion[0] = new String("("+numero[0].obtenerValor() + ") + (" + numero[0].obtenerValor() + ") = ");
        //n1 + n2 < minimo, ambos negativos grandes (null)
        resultado[1] = NumeroEntero.sumar(numero[1],numero[1]);
        operacion[1] = new String("("+numero[1].obtenerValor() + ") + (" + numero[1].obtenerValor() + ") = ");
        //n1, n2 de signo contrario (no hay problema) (valores)
        resultado[2] = NumeroEntero.sumar(numero[0],numero[1]);
        operacion[2] = new String("("+numero[0].obtenerValor() + ") + (" + numero[1].obtenerValor() + ") = ");
        resultado[3] = NumeroEntero.sumar(numero[1],numero[0]);
        operacion[3] = new String("("+numero[1].obtenerValor() + ") + (" + numero[0].obtenerValor() + ") = ");

        //RESTA
        //Signos iguales, sin problema (valores)
        resultado[4] = NumeroEntero.restar(numero[0],numero[2]);
        operacion[4] = new String("("+numero[0].obtenerValor() + ") - (" + numero[2].obtenerValor() + ") = ");
        resultado[5] = NumeroEntero.restar(numero[2],numero[0]);
        operacion[5] = new String("("+numero[2].obtenerValor() + ") - (" + numero[0].obtenerValor() + ") = ");
        //n1-n2>MAXIMO, grandes y signos contrarios n2<0 (null)
        resultado[6] = NumeroEntero.restar(numero[0],numero[1]);
        operacion[6] = new String("("+numero[0].obtenerValor() + ") - (" + numero[1].obtenerValor() + ") = ");
        //n1-n2<MINIMO, grandes y signos contrarios n1<0 (null)
        resultado[7] = NumeroEntero.restar(numero[1],numero[0]);
        operacion[7] = new String("("+numero[1].obtenerValor() + ") - (" + numero[0].obtenerValor() + ") = ");
        
        //MULTIPLICACION
        //signos iguales > Maximo (null)
        resultado[8] = NumeroEntero.multiplicar(numero[0], numero[0]);
        operacion[8] = new String("("+numero[0].obtenerValor() + ") * (" + numero[0].obtenerValor() + ") = ");
        //Signos iguales < minimo (null)
        resultado[9] = NumeroEntero.multiplicar(numero[1], numero[1]);
        operacion[9] = new String("("+numero[1].obtenerValor() + ") * (" + numero[1].obtenerValor() + ") = ");
        //signos contrarios < minimo (null)
        resultado[10] = NumeroEntero.multiplicar(numero[0], numero[1]);
        operacion[10] = new String("("+numero[0].obtenerValor() + ") * (" + numero[1].obtenerValor() + ") = ");
        resultado[11] = NumeroEntero.multiplicar(numero[1], numero[0]);
        operacion[11] = new String("("+numero[1].obtenerValor() + ") * (" + numero[0].obtenerValor() + ") = ");
        //un numero con cero (0)
        resultado[12] = NumeroEntero.multiplicar(numero[1], numero[4]);
        operacion[12] = new String("("+numero[1].obtenerValor() + ") * (" + numero[4].obtenerValor() + ") = ");
        //multiplicaciones normales (valores)
        resultado[13] = NumeroEntero.multiplicar(numero[2], numero[2]);
        operacion[13] = new String("("+numero[2].obtenerValor() + ") * (" + numero[2].obtenerValor() + ") = ");
        
        //DIVISION
        //sin problemas.
        //n1, n2, positivos
        resultado[14] = NumeroEntero.dividir(numero[2], numero[0]);
        operacion[14] = new String("("+numero[2].obtenerValor() + ") / (" + numero[0].obtenerValor() + ") = ");
        //n1 positivo, n2 negativo
        resultado[15] = NumeroEntero.dividir(numero[0], numero[1]);
        operacion[15] = new String("("+numero[0].obtenerValor() + ") / (" + numero[1].obtenerValor() + ") = ");
        //n1 negativo, n2 positivo
        resultado[16] = NumeroEntero.dividir(numero[1], numero[2]);
        operacion[16] = new String("("+numero[1].obtenerValor() + ") / (" + numero[2].obtenerValor() + ") = ");
        //n1 negativo, n2 negativo
        resultado[17] = NumeroEntero.dividir(numero[1], numero[1]);
        operacion[17] = new String("("+numero[1].obtenerValor() + ") / (" + numero[1].obtenerValor() + ") = ");
        
        imprimir(resultado, operacion);
    }
    
    private static void imprimir(NumeroNatural resultado[], String[] operacion){        
        for(int i=0; i<resultado.length; i++){
            if(resultado[i]!=null){
                System.out.print(operacion[i]);
                System.out.println(resultado[i].obtenerValor());
            }else{
                System.out.print(operacion[i]);
                System.out.println(resultado[i]);
            }
        }    
    }
    
}

/*=========================================================
************  SALIDA ESPERADA  **************************
=========================================================
(1073741828) + (-1073741833) = -5
(-1073741833) + (1073741828) = -5
(1073741828) - (12345) = 1073729483
(12345) - (1073741828) = -1073729483
(1073741828) - (-1073741833) = null
(-1073741833) - (1073741828) = null
(1073741828) * (1073741828) = null
(-1073741833) * (-1073741833) = null
(1073741828) * (-1073741833) = null
(-1073741833) * (1073741828) = null
(-1073741833) * (0) = 0
(12345) * (12345) = 152399025
(12345) / (1073741828) = 0
(1073741828) / (-1073741833) = 0
(-1073741833) / (12345) = -86977
(-1073741833) / (-1073741833) = 1
Principal.main({ });
Pruebas de numeros NumeroNaturales:
(1073741828) + (1073741833) = null
(1073741828) - (1073741833) = null
(1073741828) - (1073741828) = null
(1073741828) * (1073741833) = null
(1073741828) / (1073741833) = null
(1073741828) + (12345) = 1073754173
(1073741828) - (12345) = 1073729483
(12345) * (12345) = 152399025
(1073741833) / (12345) = 86977

Pruebas de numeros Enteros:
(1073741828) + (1073741828) = null
(-1073741833) + (-1073741833) = null
(1073741828) + (-1073741833) = -5
(-1073741833) + (1073741828) = -5
(1073741828) - (12345) = 1073729483
(12345) - (1073741828) = -1073729483
(1073741828) - (-1073741833) = null
(-1073741833) - (1073741828) = null
(1073741828) * (1073741828) = null
(-1073741833) * (-1073741833) = null
(1073741828) * (-1073741833) = null
(-1073741833) * (1073741828) = null
(-1073741833) * (0) = 0
(12345) * (12345) = 152399025
(12345) / (1073741828) = 0
(1073741828) / (-1073741833) = 0
(-1073741833) / (12345) = -86977
(-1073741833) / (-1073741833) = 1
*/