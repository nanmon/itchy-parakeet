/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>

using namespace std;

struct Carta{
    int palo, den;
};

int main()
{
    Carta Baraja[52];
    cin.get();
    for(int i=0; i<4; ++i)
        for(int j=0; j<13; ++j){
            Baraja[i*13+j].palo = i+3;
            Baraja[i*13+j].den = j+1;
        }

    for(int i=0; i<52; ++i)
        cout<<Baraja[i].palo<<','<<Baraja[i].den<<'\t';
    //system("PAUSE");
    return 0;
}
