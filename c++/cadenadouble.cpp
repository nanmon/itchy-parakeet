/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<cmath>
#include<iomanip>

using namespace std;

#define MAX 50

int main()
{
    char cadena[MAX];
    double numero=0, exponente=0;
    int nulo_i=0, punto_i=0, exp_i=0, pe_i=0, cifra;
    bool posible=true;

    cout<<"Introduzca un número [Hasta de ]: ";
    cin>>cadena;

    //Encuentra el indice donde termina la cadena
    for(; cadena[nulo_i]!=NULL; ++nulo_i);

    //Encuentra el punto decimal
    for(; cadena[exp_i]!='e' && exp_i<nulo_i; ++exp_i);
    for(; cadena[punto_i]!='.' && punto_i<exp_i; ++punto_i);
    for(pe_i=exp_i; cadena[pe_i]!='.' && pe_i<nulo_i; ++pe_i);

    if((int)cadena[0]<45 || (int)cadena[0]>57 || cadena[0]== '/') posible=false;

    //Termina el proceso si no se puede convertir a double
    for(int i=1; i<punto_i; ++i)
        if((int)cadena[i]<48 || (int)cadena[i]>57) posible=false;
    for(int i=punto_i+1; i<exp_i; ++i)
        if((int)cadena[i]<48 || (int)cadena[i]>57) posible=false;

    if( exp_i+1<nulo_i && (int)cadena[exp_i+1]<45 || (int)cadena[exp_i+1]>57 || cadena[exp_i+1]== '/') posible=false;

    for(int i=exp_i+2; i<pe_i; ++i)
        if((int)cadena[i]<48 || (int)cadena[i]>57) posible=false;
    for(int i=pe_i+1; i<nulo_i; ++i)
        if((int)cadena[i]<48 || (int)cadena[i]>57) posible=false;

    if(punto_i+1==exp_i || pe_i+1==nulo_i) posible=false;

    if(!posible){
        cout<<"No se pudo transformar a double.";
        cout<<int(cadena[0])<<','<<punto_i<<','<<exp_i<<','<<pe_i<<','<<nulo_i;
        system("PAUSE");
        return 0;
    }


    //Primer indice, por si es negativo
    if(cadena[0]!='.' && cadena[0]!='e'){
        cifra=cadena[0]=='-' ? 0 : cadena[0]-48;
        numero+=cifra*pow(10, punto_i-1);
    }
    //Del segundo indice al que contiene el punto
    for(int i=1, n=punto_i-2; i<punto_i;++i,--n){//2.1
        cifra=cadena[i]-48;
        numero+=cifra*pow(10, n);
    }
    //Del punto al exponente
    for(int i=punto_i+1, n=1; i<exp_i;++i,++n){
        cifra=cadena[i]-48;
        numero+=cifra/pow(10, n);
    }

    //Primer indice, por si es negativo
    if(exp_i<nulo_i){
        if(cadena[exp_i+1]!='.'){
            cifra=cadena[exp_i+1]=='-' ? 0 : cadena[exp_i+1]-48;
            exponente+=cifra*pow(10, pe_i - exp_i-2);
        }
        for(int i=exp_i+2, n=pe_i-exp_i-3; i<pe_i;++i,--n){
            cifra=cadena[i]-48;
            exponente+=cifra*pow(10, n);
        }

        for(int i=pe_i+1, n=1; i<nulo_i;++i,++n){
            cifra=cadena[i]-48;
            exponente+=cifra/pow(10, n);
        }
    }

    //Negativizacion
    if(cadena[0]=='-') numero*=-1;
    if(cadena[exp_i+1]=='-') exponente*=-1;
    numero*= pow(10, exponente);
    cout<<numero;

    system("PAUSE");
    return 0;
}
