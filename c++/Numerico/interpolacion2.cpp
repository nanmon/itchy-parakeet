/*
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>
#include<cmath>
#include<iomanip>
#include<fstream>
#include"numerico.h"

using namespace std;

Lagrange::Lagrange(float *_x, int _n, float (*_f)(float)):f(_f), n(_n){
    x = _x;
    coef = new float[n];

    for(int i=0; i<n; ++i){
        coef[i] = f(x[i]);
        for(int j=0; j<n; ++j){
            if(j!=i) coef[i]/=x[i]-x[j];
        }
    }

}

Lagrange::Lagrange(float **_x, int _n): n(_n){
    //x = _x;
    x = new float[n];
    coef = new float[n];

    for(int i=0; i<n; ++i){
        x[i] = _x[i][0];
        coef[i] = _x[i][1];
        for(int j=0; j<n; ++j)
            if(j!=i) coef[i]/=x[i]-_x[j][0];
    }

}

float Lagrange::operator()(float x){
    float r=0, s;
    for(int i=0; i<n; ++i){
        s=coef[i];
        for(int j=0; j<n; ++j)
            if(j!=i) s*=x-this->x[j];
        r+=s;
    }
    return r;
}

void Lagrange::liberar(){
    delete[] coef;
}

ostream& operator<<(ostream &fout, Lagrange f){
    for(int i=0; i<f.n-1; ++i){
        fout<<f.coef[i];
        for(int j=0; j<f.n; ++j)
            if(j!=i) fout<<"(x-"<<f.x[j]<<")";
        fout<<(f.coef[i+1]>0?"+":"");
    }
    fout<<f.coef[f.n-1];
        for(int j=0; j<f.n-1; ++j)
            fout<<"(x-"<<f.x[j]<<")";
    return fout;
}

//////////////////////////////////////////////////////////////////////////////
/*void operator>>(ifstream &leer, float **&x){
    int n;
    leer>>n;
    x = new float* [n+1];
    x[0] = new float[1];
    x[0][0] = n;
    for(int i=1; i<n+1; ++i){
        x[i] = new float [2];
        leer>>x[i][0]>>x[i][1];
    }
}*/
///////////////////////
