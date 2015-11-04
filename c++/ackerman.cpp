/*
Nombre: Inversion
Autor: Montaño Valdez L. Roberto
Descripcion: Calcula una inversion de 1000 con 12% de interes en n años
Fecha: 26/05/2014
*/

#include<iostream>
#include<cstdlib>

using namespace std;

long double Ackerman(long double, long double);

int main()
{

    int m, n;
    cout<<"Se calculara la funcion de Ackerman en A(m,n).\n";
    do{
        cout<<"Valor de m: ";
        cin>>m;
    }while(m<0);

    do{
        cout<<"Valor de n: ";
        cin>>n;
    }while(n<0);

    cout<<Ackerman(m, n);


    //system("PAUSE");
    return 0;
}

long double Ackerman(long double m, long double n)
{
    if(m==0) return n+1;
    else if(n==0) return Ackerman(m-1,1);
    else return Ackerman(m-1, Ackerman(m, n-1));
}
