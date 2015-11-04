/*
	Autor: Montaño Valdez L. Roberto
	Fecha: 29/Sep/2014
	Descripcion: Punto Fijo
*/

#include<cstdlib>
#include<iostream>
#include<cmath>
#include<iomanip>

using namespace std;

long double fdex(long double x){
    return exp(x/2)/3;
}

long double fPrima(long double x){
    return exp(x/2)/6;//
}

void PuntoFijo(){

    long double p=0.8, pAnt=0.619, pCero = 0.8, pUno=fdex(pCero), k=abs(fPrima(p));
    cout<<"Punto Fijo"<<endl<<endl;
    cout<<"Programa que apoxima la raiz de 3x^2-e^x con el metodo de punto fijo.\n\n";
	cout<<"\nNOTA: Agrande la ventana.\n";

	cin.get();


    cout<<" n|"<<setw(10)<<"pn"<<"|"<<setw(10)<<"f(pn)"<<"|"<<setw(10)<<"Error"<<endl<<setprecision(6);
    for(int i=0; pow(10, -4)<pow(k, i)/(1-k)*abs(pUno-pCero)/p; ++i){
        cout<<setw(2)<<i<<'|'<<setw(10)<<p<<'|'<<setw(10)<<fdex(p)<<'|'<<setw(10)<<abs(p-fdex(p))<<endl;
        pAnt = p;
        p = fdex(p);
    }
    cin.get();
}
