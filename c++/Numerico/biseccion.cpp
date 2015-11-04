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

void Biseccion(long double (*f)(long double)){

	int n;
	long double a, b, p, c, p_1=0;
    cout<<"Biseccion"<<endl<<endl;
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

	if(a>b){
        c = a;
        a=b;
        b=c;
	}
        c=b-a;

        cout<<"i | ";
        cout<<setw(n+4)<<"an"<<" | ";
        cout<<setw(n+4)<<"bn"<<" | ";
        cout<<setw(n+4)<<"pn"<<" | ";
        cout<<setw(n+4)<<"f(an)"<<" | ";
        cout<<setw(n+4)<<"f(bn)"<<" | ";
        cout<<setw(n+4)<<"f(pn)"<<" | ";
        cout<<setw(n+4)<<"Er"<<endl;


	for(int i=1; f(p)!=0 && i<=log2(c*pow(10, n)); ++i){
        p=(a+b)/2;
        cout<<setprecision(n)<<fixed<<i<<" | ";
        cout<<setw(n+4)<<a<<" | ";
        cout<<setw(n+4)<<b<<" | ";
        cout<<setw(n+4)<<p<<" | ";
        cout<<setw(n+4)<<f(a)<<" | ";
        cout<<setw(n+4)<<f(b)<<" | ";
        cout<<setw(n+4)<<f(p)<<" | ";
        cout<<setw(n+4)<<abs(p-p_1)/p<<endl;

        f(p)*f(a)<0 ? (b=p) : (a=p);
        p_1=p;
        cin.get();
	}

	cout<<"Solucion: "<<p<<endl;

	cin.get();
}
