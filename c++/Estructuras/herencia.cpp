/**
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>

using namespace std;

template<class T=int>
class Numero{
public:
    Numero(T _n):n(_n) {}
    void Imprimir() { cout<<n;}
    void Capturar() { cin>>n; }
    Numero<T> Suma(Numero<T> num){ return Numero(n+num.n); }
private:
    T n;
};

class Coordenada{
public:
    Coordenada(Numero<> _x=0, Numero<> _y=0):x(_x),y(_y){}
    void Imprimir(){
        cout<<"(";
        x.Imprimir();
        cout<<",";
        y.Imprimir();
        cout<<")";
    }
private:
    Numero<> x,y;
};

int main() {
    (Coordenada (Numero<>(2), 3)).Imprimir();
    //c.Imprimir();
	//system("PAUSE");
	return 0;
}
