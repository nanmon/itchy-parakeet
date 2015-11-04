/*
	Autor: Montaño Valdez L. Roberto
	Fecha:
	Descripcion:
*/

#include<cstdlib>
#include<iostream>
#include<cmath>
#include<iomanip>

using namespace std;

long double rdnd(long double, int);

int main(int argc, char* argv[])
{
    double real=1, aprox=1;
    int n;

    cout<<"Programa que tabula los valores de xn=(1/3)^n\n";
    do{
        cout<<"Numero de iteraciones: ";
        cin>>n;
    }while(n<1 || n>20);

    for(int i=1; i<=n; ++i){
        cout<<setw(7)<<i<<'\t';
        cout<<setw(7)<<real<<'\t';
        cout<<setw(7)<<aprox<<'\t';
        cout<<setw(7)<<real-aprox<<'\t';
        cout<<setw(7)<<(real-aprox)/real<<'\t';
        cout<<setw(7)<<(real-aprox)*100/real<<'\n';
        real*= 1.0/3;
        aprox= rdnd(aprox*rdnd(1.0/3, 5), 5);
    }


	//system("PAUSE");
	return 0;
}


 long double rdnd(long double numero, int cifras)
{
    if(!numero) return 0;
    int i=0;
    if(abs(numero)>1)
        for(; abs(numero)>1; ++i) numero/=10;
    else
        for(; abs(numero)<0.1; --i) numero*=10;
    numero=round(numero*pow(10.0, cifras))/pow(10.0, cifras);
    numero*=pow(10.0, i);
    return numero;
}
