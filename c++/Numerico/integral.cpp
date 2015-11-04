/*
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>
#include<cmath>
#include"numerico.h"

using namespace std;

Trapecios::Trapecios(long double (*f)(long double), float intervalo[2], int n){
    valor=(intervalo[1]-intervalo[0])/(2*n);
    float sumatoria = f(intervalo[0])+f(intervalo[1]);
    for(int i=1; i<n; ++i){
        sumatoria+=2*f(intervalo[0]+(intervalo[1]-intervalo[0])*i/n);
    }
    valor*=sumatoria;
}

ostream& operator<<(ostream& iout, Trapecios i){
    iout<<i.valor;
    return iout;
}
