/*
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>
#include<cmath>
#include"numerico.h"

using namespace std;

Simpson::Simpson(long double (*f)(long double), long double x[2], int n):integral(0){
    int n1_3 = 1;
    long double h = (x[1]-x[0])/n;
    if(n%2==1){
        n1_3=4;
        integral+=3*h*(f(x[1]-3*h)+3*f(x[1]-2*h)+3*f(x[1]-h)+f(x[1]))/8;
    }if(n!=3){
        long double aux = f(x[0])+f(x[1]-(n1_3-1)*h);
        for(long double i=x[0]+h; i<x[1]-(n1_3-1)*h; i+=2*h) aux+=4*f(i);
        for(long double i=x[0]+2*h; i<x[1]-n1_3*h; i+=2*h) aux+=2*f(i);
        aux/=3*n;
        aux*=x[1]-x[0];
        integral+=aux;
    }
}

long double Simpson::getVal(){ return integral; }
