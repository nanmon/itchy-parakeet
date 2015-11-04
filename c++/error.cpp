/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<math.h>
#include<iomanip>

using namespace std;

double Factorial(int);

int main()
{
    double exp = 0.5, euler=0;
    for(int i=0; i<11; ++i){
        euler+=pow(exp, i)/Factorial(i);
        cout<<setw(12)<<setprecision(10)<<fixed<<euler<<' ';
        cout<<setw(12)<<pow(M_E, exp)-euler<<' ';
        cout<<setw(12)<<(pow(M_E, exp)-euler)/pow(M_E, exp)<<' ';
        cout<<setw(12)<<(pow(M_E, exp)-euler)/pow(M_E, exp)*100<<"%\n";
    }


    //system("PAUSE");
    return 0;
}

double Factorial(int n)
{
    if(n==0) return 1;
    else return n*Factorial(n-1);
}
