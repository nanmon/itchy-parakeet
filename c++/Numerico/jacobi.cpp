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

Jacobi::Jacobi(Matriz _m): mat(_m), soluciones(_m.n,1){

    if(_m.n!=_m.m-1){
        throw "No es un sistema de ecuaciones.";
    }
    Matriz test(_m.n, _m.n);
    for(int i=0; i<test.n; ++i)
        for(int j=0; j<test.n; ++j) test[i][j]=_m[i][j];
    if(test.Det()==0) {
        throw "Sistema de ecuaciones sin solucion";
    }
    float sum;
    for(int i=0; i<_m.n; ++i){
        sum=-abs(_m[i][i]);
        for(int j=0; j<_m.n; ++j){
            sum+=abs(_m[i][j]);
        }
        if(sum>abs(_m[i][i])){
            throw "La matriz no es diagonal dominante";
        }
    }

    for(int j=0; j<mat.n; ++j){
        if(mat[j][j]==0){
            int k=j+1;
            for(; mat[k][j]==0; ++k);
            mat.Permutacion(j, k);
        }
    }
}

Matriz Jacobi::Solucionar(float error_rel){
    cout<<mat;
    Matriz aux;
    float error=1, nuevoErr, bj;
    for(int i=0; error>error_rel; ++i){
        error=0;
        aux=soluciones;
        for(int j=0; j<mat.n; ++j){
            soluciones[j][0]=mat[j][mat.n];
            for(int k=0; k<mat.n-1; ++k){
                soluciones[j][0]-=mat[j][k>=j?k+1:k]*aux[k>=j?k+1:k][0];
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
