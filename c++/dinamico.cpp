/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>

using namespace std;

int main()
{

    int * arreglo = new int[8];
    int * arr2;
    for(int i=0; i<8; ++i) arreglo[i]=i;

    for(int i=0; i<8; ++i)  cout<<*(arreglo+i)<<endl;
    arr2=arreglo;
    delete [] arreglo;
    for(int i=0; i<7; ++i)  cout<<*(arr2+i)<<endl;
    //system("PAUSE");
    return 0;
}
