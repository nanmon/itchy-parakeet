/*
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>

using namespace std;

template <typename TIPO>
void Intercambio(TIPO &a, TIPO &b){
    TIPO aux = a;
    a = b;
    b = aux;
}

void Intercambio(float &a, float &b){
    float aux = a;
    a = b;
    b= aux;
}

int main(){

    int a=5, b=2;
    cout<<a<<","<<b<<endl;
    Intercambio(a,b);
    cout<<a<<","<<b<<endl;

    char c=50, d=20;
    cout<<c<<","<<d<<endl;
    Intercambio(c,d);
    cout<<c<<","<<d<<endl;

    float p=50.2, e=20.3;
    cout<<p<<","<<e<<endl;
    Intercambio(p,e);
    cout<<p<<","<<e<<endl;

	//system("PAUSE");
	return 0;
}
