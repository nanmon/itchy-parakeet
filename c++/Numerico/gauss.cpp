/*
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>
#include<fstream>
#include"numerico.h"

using namespace std;

Gauss::Gauss(Matriz a){
    if(a.n!=a.m-1){
        throw "No es un sistema de ecuaciones";
    }
    m = a;
    x = Matriz::Cero(a.n, 1);
}

Matriz Gauss::solucionar(){
    for(int i=0; i<m.n; ++i){
        if(m[i][i]==0){
            int cambio=i+1;
            while(m[cambio][i]==0) ++cambio;
            m.Permutacion(i, cambio);
        }
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
