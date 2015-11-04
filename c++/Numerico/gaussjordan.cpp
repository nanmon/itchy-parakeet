/*
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>
#include<fstream>
#include<iomanip>
#include<cmath>
#include"numerico.h"

using namespace std;

GaussJordan::GaussJordan(Matriz a){
    if(a.n!=a.m-1){
        throw "No es un sistema de ecuaciones";
    }
    m = Matriz::Identidad(a.n);
    b = Matriz::Cero(a.n, 1);
    for(int i=0; i<a.n; ++i){
        for(int j=0; j<a.n; ++j) m[i][j] = a[i][j];
        b[i][0] = a[i][a.n];
    }
    if(abs(m.escalada().Det())<0.5){
        throw "Matriz mal condicionada.";
    }

}

Matriz GaussJordan::solucionar(){
    x = m.Inv()*b;
    Matriz b2 = m*x - b;
    x = x + m.Inv()*b2;
    return x;
}
