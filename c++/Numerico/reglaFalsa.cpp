/*
	Autor: Montaño Valdez L. Roberto
	Fecha:
	Descripcion:
*/

#include<cstdlib>
#include<iostream>
#include<iomanip>
#include<cmath>

using namespace std;

void ReglaFalsa(long double (*f)(long double)){

	int n, iter;
	long double a, b, p, c;

	cout<<"Regla Falsa"<<endl<<endl;
	cout<<"Programa que calcula una aproximacion a la solucion de a la funcion 3x^3+3x+1, dado la tolerancia y el rango.";
	cout<<"\nNOTA: Agrande la ventana.\n";

	do{
        cout<<"Numero de cifras significativas: ";
        cin>>n;
	}while(n<=0);

	do{
        cout<<"Rango [Dos numeros]: ";
        cin>>a>>b;
        if(f(a)*f(b)>0) cout<<"No hay raices en el rango, o el rango es muy grande.\n";
	}while(f(a)*f(b)>0);

	do{
        cout<<"Numero de iteraciones maximo: ";
        cin>>iter;
	}while(iter<1);

	if(a>b){
        c = a;
        a=b;
        b=c;
	}
    c=a;
    p=b;

    cout<<setprecision(n)<<fixed<<"i | ";
    cout<<setw(n+4)<<"a"<<" | ";
    cout<<setw(n+4)<<"b"<<" | ";
    cout<<setw(n+4)<<"p"<<" | ";
    cout<<setw(n+4)<<"f(a)"<<"| ";
    cout<<setw(n+4)<<"f(b)"<<" | ";
    cout<<setw(n+4)<<"f(p)"<<" | ";
    cout<<setw(n+4)<<"Er"<<endl;
    int i=1;
	for(; i<=iter && abs((p-c)/p)>=pow(10, -n); ++i){
        c=p;
        p=a-f(a)*(b-a)/(f(b)-f(a));
        cout<<i<<" | ";
        cout<<setw(n+4)<<a<<" | ";
        cout<<setw(n+4)<<b<<" | ";
        cout<<setw(n+4)<<p<<" | ";
        cout<<setw(n+4)<<f(a)<<" | ";
        cout<<setw(n+4)<<f(b)<<" | ";
        cout<<setw(n+4)<<f(p)<<" | ";
        cout<<setw(n+4)<<abs(p-c)/p<<endl;
        f(p)*f(a)<0 ? (b=p) : (a=p);
        cin.get();
	}
    if(i>iter) cout<<"Numero maximo de iteraciones alcanzado"<<endl;
	cout<<"Solucion: "<<p<<endl;

	cin.get();
}
