/*
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>

using namespace std;

template<class TIPO>
class Numero{
public:
    void imprimir() {cout<<n;}
    void Capturar() {cin>>n;}
    ///<TIPO> no necesario
    Numero<TIPO> operator+(Numero<TIPO> a){ return Numero(n+a.n);}

    Numero(TIPO _n):n(_n) {}
private:
 TIPO n;
};

template<>
class Numero<int>{
public:
    void imprimir() {cout<<n<<":int";}
    void Capturar() {cin>>n;}
    ///<TIPO> no necesario
    Numero operator+(Numero a){ return Numero(n+a.n);}

    Numero(int _n):n(_n) {}
private:
 int n;
};

int main(){

    Numero<int> a(5), b(2);
    a.imprimir();
    cout<<endl;
    b.imprimir();
    cout<<endl;
    Numero<int> c = a+b;
    c.imprimir();

	//system("PAUSE");
	return 0;
}
