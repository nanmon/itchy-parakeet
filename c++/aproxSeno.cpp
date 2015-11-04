/*
	Aproximacion a senx
	Roberto Montaño
	27/08/2014
 */

#include<iostream>
#include<cstdlib>
#include<cmath>
#include<iomanip>

using namespace std;

int main(){

	long double aprox=0, x, fact=1;
	int cifras;

	cout<<"Programa que calcula una aproximacion al sen(x) dado x y el numero de cifras significativas.\nx: ";
	cin>>x;
	do{
		cout<<"Cifras significativas: ";
		cin>>cifras;
	}while(cifras<0);

	cout<<"Valor real: "<<setprecision(cifras+1)<<sin(x)<<setiosflags(ios::left)<<endl;
	cout<<"i\t"<<setw(cifras+2)<<"Aprox"<<'\t'<<setw(cifras+2)<<"E"<<'\t'<<setw(cifras+2)<<"Er"<<endl;

	for(int i=0; abs((sin(x)-aprox)/sin(x))>5*pow(10, -cifras); ++i, fact*=(2*i+1)*2*i){
		aprox+=(i%2 ? -1 : 1)*pow(x, 2*i+1)/fact;
		cout<<(i+1)<<'\t'<<setw(cifras+2)<<aprox<<'\t'<<setw(cifras+2)<<abs(sin(x)-aprox)<<'\t'<<setw(cifras+2)<<abs((sin(x)-aprox)/sin(x))<<endl;
		cin.get();
	}

	system("PAUSE");
	return 0;
}
