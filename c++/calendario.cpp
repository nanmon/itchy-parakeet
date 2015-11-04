/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<iomanip>

using namespace std;
int PrimerDiaSem(int, int);

int main()
{
    int mes, year, inicio, fin, calendario[6][7]={};
    cout<<"Programa que imprime el calendario mensual, dado un año y un mes.\n";

    do{
        cout<<"De un año: ";
        cin>>year;
    }while(year<0);

    do{
        cout<<"De el numero de un mes: ";
        cin>>mes;
    }while(mes<1 || mes>12);
    inicio=PrimerDiaSem(mes, year);
    switch(mes){
        case 2:
            fin=28;
            break;
        case 4:
        case 6:
        case 9:
        case 11:
            fin=30;
            break;
        default: fin=31;
    }

    for(int i=0, dia=1, j=inicio; dia<=fin; ++i){
        for(; dia<=fin; ++j, ++dia) calendario[i][j]=dia;
        j=0;
    }
    cout<<endl<<setw(3)<<'D'<<setw(3)<<'L'<<setw(3)<<'M'<<setw(3)<<'M';
    cout<<setw(3)<<'J'<<setw(3)<<'V'<<setw(3)<<'S'<<endl;
    for(int j=0; j<inicio; ++j) cout<<"   ";
    for(int i=0, j=inicio; i*7-inicio<fin; ++i){
        for(; j<7 && i*7 +j-inicio<fin; ++j) cout<<setw(3)<<calendario[i][j];
        j=0;
        cout<<endl;
    }

    //system("PAUSE");
    return 0;
}

int PrimerDiaSem(int mes, int year)
{
    int d, a, y, m;

    a = (14-mes) / 12;
    y = year - a;
    m = mes + 12*a - 2;
    d = 1 + y + int(y / 4) - int(y / 100) + int(y / 400) + int(31*m/12);
    return d%7;
}
