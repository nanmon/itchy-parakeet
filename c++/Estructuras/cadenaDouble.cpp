/**
	Nombre: Convertir Cadena a Double
	Autor: L. Roberto Montaño Valdez
	Fecha: 12/Feb/2015
	Descripción:
*/

#include<cstdlib>
#include<iostream>
#include<cmath>
#include<iomanip>

using namespace std;

class NoEsNatural : public exception {
    virtual const char* what() const throw(){ return "La cadena no representa un numero natural."; }
};

class NoEsEntero : public NoEsNatural{
    virtual const char* what() const throw(){ return "La cadena no representa un numero entero."; }
};

class NoEsReal : public NoEsEntero {
    virtual const char* what() const throw(){ return "La cadena no representa un numero real."; }
};

class NoEsDouble : public NoEsReal {
    virtual const char* what() const throw(){ return "La cadena no representa un numero."; }
};


long double parseNatural(char n[]) throw(NoEsNatural){
    int length = 0;
    while(n[length]!='\0') ++length;
    int exp = length-1;
    long double numero = 0;
    int cifra;
    for(int i=0; i<length; ++i, --exp){
        cifra = n[i] - '0';
        if(cifra<0 || cifra>9) throw NoEsNatural();
        numero+=cifra*pow(10, exp);
    }

    return numero;
}

long double parseInt(char n[]) throw(NoEsEntero){
    try{
        return parseNatural(n);
    }catch(NoEsNatural ex){
        throw NoEsEntero();
    }
}

long double parseReal(char n[]) throw(NoEsReal){
    int punto = 0;
    while(n[punto]!='.' && n[punto]!='\0') ++punto;
    try{
        if(n[punto]=='\0') return parseInt(n);
    }catch(NoEsEntero ex){
        throw NoEsReal();
    }
    char *entero = n, *decimal = n+punto+1;
    entero[punto] = '\0';

    long double real=0;

    int cifra, exp=-1;
    for(int i=0; decimal[i]!='\0'; ++i, --exp){
        cifra = decimal[i] - '0';
        if(cifra<0 || cifra>9) throw NoEsReal();
        real+=cifra*pow(10, exp);
    }

    try{
        real += parseInt(entero);
    }catch(NoEsEntero ex){
        n[punto] = '.';
        throw NoEsReal();
    }
    n[punto] = '.';
    return real;
}

long double parseDouble(char n[]) throw(NoEsDouble){
    int e = 0;
    while(n[e]!='e' && n[e]!='\0') ++e;
    try{
        if(n[e]=='\0') return parseReal(n);
    }catch(NoEsReal ex){
        throw NoEsDouble();
    }
    char *entero = n, *exponente = n+e+1;
    entero[e] = '\0';

    long double real;
    try{
        real = parseReal(entero);
        real *= pow(10, parseInt(exponente+(exponente[0]=='+'?1:0)));
    }catch(NoEsEntero ex){
        n[e] = 'e';
        throw NoEsDouble();
    }
    n[e] = 'e';
    return real;
}

bool esNatural(char n[]){
    if(n[0]=='\0') return false;
    for(int i=0; n[i]!='\0'; ++i)
        if(n[i]<'0' || n[i]>'9') return false;

    return true;
}

bool esEntero(char n[]){
    //if(n[0]=='-') return esNatural(n+1);
    return esNatural(n);
}

bool esReal(char n[]){
    int punto = 0;
    while(n[punto]!='.' && n[punto]!='\0') ++punto;
    if(n[punto]=='\0') return esEntero(n);
    n[punto] = '\0';
    bool entero = esEntero(n);
    n[punto] = '.';
    return entero && esNatural(n+punto+1);
}

bool esDouble(char n[]){
    int e = 0;
    while(n[e]!='e' && n[e]!='\0') ++e;
    if(n[e]=='\0') return esReal(n);
    n[e] = '\0';
    bool real = esReal(n);
    n[e] = 'e';
    return real && esEntero(n+e+(n[e+1]=='+'?2:1));
}

//presedencia: (), ^, */, +-



