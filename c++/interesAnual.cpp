/*
Nombre: Inversion
Autor: Montaño Valdez L. Roberto
Descripcion: Calcula una inversion de 1000 con 12% de interes en n años
Fecha: 26/05/2014
*/

#include<iostream>
#include<cstdlib>

using namespace std;

float InteresRec(int);

int main()
{

    int years;
    cout<<"En una inversion de $1000, con un interes anual del 12%, se calculara el total acumulado en n años.\n";
    do{
        cout<<"Valor de n: ";
        cin>>years;
    }while(years<0);

    cout<<InteresRec(years);


    //system("PAUSE");
    return 0;
}

float InteresRec(int n)
{
    if(n==0) return 1000;
    else return 1.12*InteresRec(n-1);
}
