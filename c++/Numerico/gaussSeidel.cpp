
/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<iomanip>
#include<fstream>
#include<cmath>
#include"numerico.h"

using namespace std;


GaussSeidel::GaussSeidel(Matriz _m): mat(_m), soluciones(_m.n,1){
     if(_m.n!=_m.m-1) throw "No es un sistema de ecuaciones";
    Matriz test(_m.n, _m.n);
    for(int i=0; i<test.n; ++i)
        for(int j=0; j<test.n; ++j) test[i][j]=_m[i][j];
    if(abs(test.escalada().Det())<0.5) throw "Matriz mal condicionada";
    for(int j=0; j<mat.n; ++j){
        if(mat[j][j]==0){
            int k=j+1;
            for(; mat[k][j]==0; ++k);
            mat.Permutacion(j, k);
        }
    }
}

Matriz GaussSeidel::Solucionar(float error_rel){
    cout<<mat;
    float error=1, nuevoErr, bj;
    for(int i=0; error>error_rel; ++i){
        error=0;
        for(int j=0; j<mat.n; ++j){
            soluciones[j][0]=mat[j][mat.n];
            for(int k=0; k<mat.n-1; ++k){
                soluciones[j][0]-=mat[j][k>=j?k+1:k]*soluciones[k>=j?k+1:k][0];
            }
            soluciones[j][0]/=mat[j][j];
        }
        for(int j=0; j<mat.n; ++j){
            bj=0;
            for(int k=0; k<mat.n; ++k){
                bj+=mat[j][k]*soluciones[k][0];
            }
            nuevoErr=abs((mat[j][mat.n]-bj)/mat[j][mat.n]);
            if(nuevoErr>error) error=nuevoErr;
        }
    }
    return soluciones;
}
