/*
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>
#include<iomanip>
#include<fstream>
#include<cmath>
#include"numerico.h"

using namespace std;

Gauss2::Gauss2(Matriz a){
    if(a.n!=a.m-1){
        throw "No es un sistema de ecuaciones.";
    }
    m = a;
    x = Matriz::Cero(a.n, 1);
    if(abs(m.desaumentada().escalada().Det())<0.5){
        throw "Matriz mal condicionada.";
    }
}

Matriz Gauss2::solucionar(){
    for(int i=0; i<m.n; ++i){
            int mayor=i;
            for(int k=i+1; k<m.n; ++k)
                if(m[k][i]>m[mayor][i]) mayor=k;
            m.Permutacion(i, mayor);
        for(int j=i+1; j<m.n; ++j)
            //(param1)*m[param2]+(param3)*m[param4] en (param5)
            m.Eliminacion(m[j][i]/m[i][i], i, -1, j, j);
    }
    for(int i=m.n-1; i>=0; --i){
        x[i][0] = m[i][m.n];
        for(int j=m.n-1; j>i; --j){
            x[i][0]-=m[i][j]*x[j][0];
        }
        x[i][0]/=m[i][i];
    }
    return x;
}
