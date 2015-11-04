/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<cmath>
#include"numerico.h"

using namespace std;

FactorizacionLU::FactorizacionLU(Matriz _m):a(_m.n,_m.n+1){
    if(_m.n!=_m.m-1) throw "No es un sistema de ecuaciones.";
    for(int i=0, j=0; i<_m.n; ++i, j=0){
        for(; j<_m.n+1; ++j) a[i][j]=_m[i][j];
    }
}

Matriz FactorizacionLU::SolucionarL(){
    Matriz u(a.n, a.n), l(a.n, a.n);
    int aux, p;
    float maxsum, sum;
    for(int i=0; i<a.n; ++i){
        maxsum=0;
        for(p=i, aux=i; p<a.n; ++p){
            sum=-a[p][i];
            for(int k=0; k<i; ++k) sum+=a[k][i]*a[p][k];
            if(abs(sum)>maxsum){
                aux=p;
                maxsum=abs(sum);
            }
        }
        if(i!=aux){
            a.Permutacion(i, aux);
            l.Permutacion(i, aux);
        }
        l[i][i]=1;
        u[i][i] = a[i][i];
        for(int k=0; k<i; ++k)
            u[i][i]-=l[i][k]*u[k][i];
        for(int j=i+1; j<a.n; ++j){
            u[i][j]=a[i][j];
            l[j][i]=a[j][i];
            for(int k=0; k<i; ++k)
                u[i][j]-=l[i][k]*u[k][j];
            for(int k=0; k<i; ++k)
                l[j][i]-=l[j][k]*u[k][i];
            l[j][i]/=u[i][i];
        }
        cout<<l<<endl<<u<<endl;
        cin.get();
    }

    float y[a.n];
    for(int i=0; i<a.n; ++i){
        y[i] = a[i][a.n];
        for(int j=0; j<i; ++j) y[i]-=l[i][j]*y[j];
        //y[i]/=l[i][i];
    }

    Matriz x(a.n, 1);
    for(int i=a.n-1; i>=0; --i){
        x[i][0]=y[i];
        for(int j=i+1; j<a.n; ++j) x[i][0]-=u[i][j]*x[j][0];
        x[i][0]/=u[i][i];
    }

    return x;
}
