/**
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>
#include<cstring>

using namespace std;

class Excepcion{
public:
    Excepcion(char *m){ strcpy(mensaje, m); }
    char *getMensaje(){ return mensaje; }

private:
    char mensaje[100];
};

class NoHayMemoria : public Excepcion{
public:
    NoHayMemoria():Excepcion("No hay memoria disponible..."){}
};

int main(){

    try{
        throw NoHayMemoria();///new NoHayMemoria ... catch(Excepcion *e) cout<<e->getMensaje();
    }catch(Excepcion &e){
        cout<<e.getMensaje();
    }

	//system("PAUSE");
	return 0;
}
