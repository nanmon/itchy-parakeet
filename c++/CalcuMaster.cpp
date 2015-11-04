/*
Programa: CalcuMaster.cpp
Autor: Montaño Valdez L. Roberto
Fecha:
Descripción:
*/

#include <iostream>
#include <cstdlib>
#include <math.h>

using namespace std;
typedef float (*PtrOp)(float,float);

////////////protos//////////////////////7
float Suma(float,float);
float Resta(float,float);
float Producto(float,float);
float Cociente(float,float);
float Potencia(float,float);
void GestionOp(char*,PtrOp);
char Menu();
///////////////////////////////////////////7

int main()
{

    PtrOp funcionOp;
    char aGestion = Menu();
    switch(aGestion){
        case '+': funcionOp=Suma; break;
        case '-': funcionOp=Resta; break;
        case '*': funcionOp=Producto; break;
        case '/': funcionOp=Cociente; break;
        case '^': funcionOp=Potencia; break;
    }
    GestionOp(&aGestion, funcionOp);

    system("PAUSE");
    return 0;
}

char Menu()
{
    char operacion;
    cout<<"Seleccione el s\xA1mbolo de la operaci\xA2n que desea realizar:\n+) a+b\n-) a-b\n*) a*b\n/) a/b\n^) a^b\n";
    cin>>operacion;

    return operacion;
}

void GestionOp(char *simbolo,PtrOp Operacion)
{
    float a, b;
    cout<<"Introduzca valores:\na=";
    cin>>a;
    cout<<"b=";
    cin>>b;
    if(b!=0 || *simbolo!='/')
        cout<<a<<*simbolo<<b<<'='<<Operacion(a,b)<<endl;
    else cout<<"Error: Divisi\xA2n entre cero.";

}
float Suma(float a,float b)
{
   return a+b;
}

float Resta(float a,float b)
{
   return a-b;
}

float Producto(float a,float b)
{
   return a*b;
}

float Cociente(float a,float b)
{
   return a/b;
}

float Potencia(float a,float b)
{
    return pow(a, b);
}
