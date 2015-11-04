/*
  Nombre: Luis Roberto Montaño Valdez.
  Programa: Secante
  Fecha: 01/Oct/2014
  Descripcion:
*/

#include<iostream>
#include<cstdlib>
#include<cmath>
#include<iomanip>

using namespace std;

void Secante(long double (*f)(long double))
{
   long double x=0, xAnt=1, aux;
   int n;

   cout<<"Secante"<<endl<<endl;
	cout<<"Programa que calcula una aproximacion a la solucion de a la funcion 3x^3+3x+1, dado la tolerancia y el rango.";
	cout<<"\nNOTA: Agrande la ventana.\n";

	do{
        cout<<"Numero de cifras significativas: ";
        cin>>n;
	}while(n<=0);

    cout<<"Rango [Dos numeros]: ";
    cin>>x>>xAnt;

    cout<<"i | ";
    cout<<setw(n+4)<<"xn-1"<<" | ";
    cout<<setw(n+4)<<"xn"<<" | ";
    cout<<setw(n+4)<<"f(xn-1)"<<" | ";
    cout<<setw(n+4)<<"f(xn)"<<" | ";
    cout<<setw(n+4)<<"Er"<<endl;

   for(int i=0; abs((x-xAnt)/x)>=pow(10.0, -n); ++i){
      aux = x;
      x = x - f(x)*(x-xAnt)/(f(x)-f(xAnt));
      xAnt = aux;
      cout<<setw(2)<<i<<"| ";
      cout<<setw(n+4)<<xAnt<< " | ";
      cout<<setw(n+4)<<x<<" | ";
      cout<<setw(n+4)<<f(xAnt)<<" | ";
      cout<<setw(n+4)<<f(x)<<" | ";
      cout<<setw(n+4)<<abs((x-xAnt)/x)<<endl;
   }

   cin.get();
}
