/*
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>
#include<cmath>
#include<fstream>
#include<iomanip>
#include"numerico.h"

using namespace std;

Regresion::Regresion(float **x, int n, int grado){
    Matriz sistema(grado+1, grado+2);
    for(int i=0; i<sistema.n; ++i){
        for(int j=0; j<sistema.m; ++j){
            sistema[i][j] = 0;
            for(int k=0; k<n; ++k){
                sistema[i][j]+=pow(x[k][0], i+j);
            }
        }
        sistema[i][grado+1] = 0;
        for(int k=0; k<n; ++k){
            sistema[i][grado+1]+=pow(x[k][0], i)*x[k][1];
        }
    }
    FactorizacionLU lu(sistema);
    polinomio = lu.SolucionarL();
}

float Regresion::operator()(float x){
    float f=0;
    for(int i=0; i<polinomio.n; ++i){
        f+=pow(x, i)*polinomio[i][0];
    }
    return f;
}

ostream& operator<<(ostream &fout, Regresion f){
    if(f.polinomio.n>2) cout<<f.polinomio[f.polinomio.n-1][0]<<"x^"<<f.polinomio.n-1<<" ";
    for(int i=f.polinomio.n-2; i>=2; --i)
        if(f.polinomio[i][0]!=0)
            cout<<(f.polinomio[i][0]>0?"+":"")<<f.polinomio[i][0]<<"x^"<<i<<" ";
    cout<<(f.polinomio[1][0]>0?"+":"")<<f.polinomio[1][0]<<"x"<<(f.polinomio[0][0]>0?" +":" ");
    cout<<f.polinomio[0][0]<<endl;
    return fout;
}
///////////////////////
