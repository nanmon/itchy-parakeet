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

DifDiv::DifDiv(float *_x, int _n, float (*_f)(float)):f(_f), n(_n){
    dd = new float* [n];
    for(int i=0; i<n; ++i){
        dd[i] = new float[n+1];
        dd[i][0] = _x[i];
        dd[i][1] = f(dd[i][0]);
    }

    //f[x0,...xn]
    for(int i=1; i<n; ++i){
        for(int j=2; j<=i+1; ++j){
            dd[i][j] = (dd[i][j-1]-dd[i-1][j-1])/(dd[i][0]-dd[i-j+1][0]);
        }
    }

}

DifDiv::DifDiv(float **_x, int _n): n(_n){
    dd = new float* [n];
    for(int i=0; i<n; ++i){
        dd[i] = new float[n+1];
        dd[i][0] = _x[i][0];
        dd[i][1] = _x[i][1];
    }

    //f[x0,...xn]
    for(int i=1; i<n; ++i){
        for(int j=2; j<=i+1; ++j){
            dd[i][j] = (dd[i][j-1]-dd[i-1][j-1])/(dd[i][0]-dd[i-j+1][0]);
        }
    }

}

float DifDiv::operator()(float x){

    float r=dd[0][1], s;
    for(int i=1; i<n; ++i){
        s=dd[i][i+1];
        for(int j=0; j<i; ++j)
            s*=x-dd[j][0];
        r+=s;
    }
    return r;
}

void DifDiv::add(float xn){
    ++n;
    float** aux = dd;
    dd = new float* [n];
    for(int i=0; i<n-1; ++i){
        dd[i] = new float [n+1];
        for(int j=0; j<=i+1; ++j) dd[i][j] = aux[i][j];
        delete[] aux[i];
    }
    delete[] aux;
    dd[n-1]= new float [n+1];
    dd[n-1][0] = xn;
    dd[n-1][1] = f(xn);

    for(int j=2; j<=n; ++j){
        dd[n-1][j] = (dd[n-1][j-1]-dd[n-2][j-1])/(dd[n-1][0]-dd[n-j][0]);
    }

}

void DifDiv::liberar(){
    for(int i=0; i<n; ++i) delete[] dd[i];
    delete[] dd;
}

//////////////////////////////////////////////////////////////////////////////
void operator>>(ifstream &leer, float **&x){
    int n;
    leer>>n;
    x = new float* [n+1];
    x[0] = new float[1];
    x[0][0] = n;
    for(int i=1; i<n+1; ++i){
        x[i] = new float [2];
        leer>>x[i][0]>>x[i][1];
    }
}

ostream& operator<<(ostream &fout, DifDiv f){
    for(int i=0;i<f.n-1;++i){
        cout<<f.dd[i][i+1];
        for(int j=0; j<i; ++j) cout<<"(x-"<<f.dd[j][0]<<")";
        cout<<(f.dd[i+1][i+2]>0?"+":"");
    }
    cout<<f.dd[f.n-1][f.n];
    for(int j=0; j<f.n-1; ++j) cout<<"(x-"<<f.dd[j][0]<<")";
    return fout;
}
///////////////////////
