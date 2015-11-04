/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<ctime>

using namespace std;

srand(time(NULL));

struct Arista{
    int distancia;
    Arista *nodA;
    Arista *nodB;
};

struct Nodo{
    Arista con[10];
    int tray;
    Arista *bef;
};

class Grafo{
public:
    Nodos nods[10];
    Grafo();
    int n;
};

Grafo::Grafo(){
    n=rand()%10+1;
    for(int i=0; i<n; ++i)
        for(int j=i+1; j<n; ++j)
            if(rand()%2{
            nods[i].con[e]
            }
}

int main()
{


    //system("PAUSE");
    return 0;
}
