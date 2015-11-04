/*
  Programa:
  Autor:
  Fecha:
  Descripcion:
*/

#include<cstdlib>
#include<iostream>
#include<cmath>

using namespace std;

int main(int argc, char argv[]){

    double aprox=0, pot=0.5, fact=1;
    cout<<"e^"<<pot<<" = "<<pow(M_E,pot)<<endl;
    for(int i=0;(pow(M_E,pot)-aprox)/pow(M_E,pot)>abs(5*pow(10, -6));++i,fact*=i){
        aprox+=pow(pot, i)/fact;
        cout<<"Aprox: "<<aprox<<"; E: "<<pow(M_E,pot)-aprox<<"; Er: "<<(pow(M_E,pot)-aprox)/pow(M_E,pot)<<endl;
        cin.get();
    }
	//system("PAUSE");
	return 0;
}
