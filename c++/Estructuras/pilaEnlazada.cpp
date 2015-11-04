/**
	Nombre:
	Autor: L. Roberto Montaño Valdez
	Fecha:
	Descripción:
*/

#include<cstdlib>
#include<iostream>

using namespace std;

class Pila{
public:
    ///Constructor c:
    Pila():tope(NULL), n(0){}

    Pila(const Pila &p):tope(NULL), n(0){
        *this=p;
    }

    Pila& operator=(const Pila &p){
        if(this!=&p){
            Elemento *e = tope, *c = p.tope, *aux = tope;
            if(tope==NULL){
                tope = new Elemento(c->info);
                aux = tope;
                c = c->sig;
            }
            while(e!=NULL && c!=NULL){
                e->info = c->info;
                aux = e;
                e = e->sig;
                c = c->sig;
            }
            if(e!=NULL){
                aux->sig = NULL;
                while(e!=NULL){
                    aux = e;
                    e = e->sig;
                    delete aux;
                }
            }
            else {
                while(c!=NULL){
                    e = new Elemento(c->info);
                    aux->sig = e;
                    aux = e;
                    c = c->sig;
                }
            }
        }
        return *this;
    }

    ~Pila(){ vaciar(); }
    void vaciar(){
        while(tope!=NULL) removerElemento();
    }

    void insertarElemento(int i){
        Elemento *e = new (nothrow) Elemento(i, tope);
        if(e==NULL) throw false;
        tope = e;
        ++n;
    }

    int removerElemento(){
        if(tope==NULL) throw "Error: Pila vacia";
        --n;
        Elemento *aux = tope;
        int r = aux->info;
        tope = tope->sig;
        delete aux;
        return r;
    }

    int getElemento() {
        if(tope==NULL) throw "Error: Pila vacia";
        return tope->info;
    }

    int getSize(){ return n; }

    void imprimir(){
        cout<<"-> ";
        Elemento *t = tope;
        while(t!=NULL){
            cout<<t->info<<",";
            t = t->sig;
        }
        cout<<"\b]"<<endl;
    }

private:
    struct Elemento{
        Elemento(int i, Elemento *n=NULL):info(i), sig(n) {}
        //void setInfo(int i){ info=i; }
        //int getInfo() { return info; }
        //void setSig(Elemento *s){ sig=s; }
        //Elemento* getSig() { return sig; }
        int info;
        Elemento *sig;
    };

    int n;
    Elemento *tope;
};

int main(int argc, char *argv){

    Pila p;
    p.insertarElemento(1);
    p.imprimir();
    p.insertarElemento(2);
    p.imprimir();

    Pila p2 = p, p3;
    p2.insertarElemento(3);
    p2.imprimir();
    p2.insertarElemento(4);
    p2.imprimir();
    p3 = p2 = p;
    p.imprimir();
    p2.imprimir();
    p3.imprimir();
    p2.removerElemento();
    p2.imprimir();
    p2.removerElemento();
    p2.imprimir();
	//system("PAUSE");
	return 0;
}
