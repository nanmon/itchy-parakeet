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

void NewtonRaphson(long double (*f)(long double), long double (*df)(long double)){

	long double a=0, b = 1;
	int n;

	cout<<"Newton-Raphson"<<endl<<endl;
	cout<<"Programa que calcula una aproximacion a la solucion de a la funcion 3x^3+3x+1, dado la tolerancia y valor inicial.";
	cout<<"\nNOTA: Agrande la ventana.\n";

	do{
        cout<<"Numero de cifras significativas: ";
        cin>>n;
	}while(n<=0);

    cout<<"Valor inicial: ";
    cin>>b;


    cout<<" n|"<<setw(11)<<"xn|"<<setw(11)<<"f(xn)|"<<setw(11)<<"f'(xn)|"<<setw(10)<<"Er"<<endl<<setprecision(5);
	for(int i=0; abs((b-a)/b)>pow(10, -n); ++i){
        a=b;
        b = a - f(a)/df(a);
        cout<<setw(2)<<i<<'|'<<setw(10)<<a<<'|'<<setw(10)<<f(a)<<'|'<<setw(10)<<df(a)<<'|'<<setw(10)<<abs((b-a)/b)<<endl;
	}

	cout<<endl<<"Solucion: "<<a<<", f(xn) = "<<f(a);

	cin.get();
}
