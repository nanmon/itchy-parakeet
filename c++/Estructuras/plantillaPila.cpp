/**
	Nombre: Plantilla de Pila
	Autor: L. Roberto Montaño Valdez
	Fecha: 13/feb/2015
	Descripción: c:
*/

#include<cstdlib>
#include<iostream>
#include<string>


using namespace std;

class NoHayMemoria : public exception {
    const char* what() const throw() { return "No hay memoria disponible"; }
};

class PilaVacia : public exception {
    const char* what() const throw() { return "La pila esta vacia"; }
};

template<class T> class Pila;
template<class T> ostream& operator<<(ostream &, const Pila<T>&);

template <class T>
class Pila{
    friend ostream& operator<< <>(ostream &, const Pila<T>&);
public:

    Pila();
    Pila(const Pila<T> &);
    Pila<T>& operator=(const Pila<T> &);
    ~Pila();

    void vaciar();
    void insertarElemento(T);
    T removerElemento();
    T getElemento();
    int getSize();
    bool estaVacia();
    void imprimir();

private:
    class Elemento;
    int n;
    Elemento *tope;
};

template<class T>
class Pila<T>::Elemento{
public:
    Elemento(T, Elemento * = NULL);
    //void setInfo(int i){ info=i; }
    //int getInfo() { return info; }
    //void setSig(Elemento *s){ sig=s; }
    //Elemento* getSig() { return sig; }
    T info;
    Elemento *sig;
};

template <class T>
Pila<T>::Elemento::Elemento(T i, Elemento *n):info(i), sig(n) {}

template <class T>
Pila<T>::Pila():tope(NULL), n(0){}

template <class T>
Pila<T>::Pila(const Pila<T> &p):tope(NULL), n(0){
    *this=p;
}

template <class T>
Pila<T>& Pila<T>::operator=(const Pila<T> &p){
    if(this!=&p){
        Elemento *e = tope, *c = p.tope, *aux = tope;
        if(tope==NULL){
            tope = new (nothrow) Elemento(c->info);
            if(tope==NULL) throw NoHayMemoria();
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
                e = new (nothrow) Elemento(c->info);
                if(e == NULL) throw NoHayMemoria();
                aux->sig = e;
                aux = e;
                c = c->sig;
            }

        }
    }
    return *this;
}

template <class T>
Pila<T>::~Pila(){ vaciar(); }

template <class T>
void Pila<T>::vaciar(){
    while(tope!=NULL) removerElemento();
}

template <class T>
void Pila<T>::insertarElemento(T i){
    Elemento *e = new (nothrow) Elemento(i, tope);
    if(e==NULL) throw NoHayMemoria();
    tope = e;
    ++n;
}

template <class T>
T Pila<T>::removerElemento(){
    if(estaVacia()) throw PilaVacia();
    --n;
    Elemento *aux = tope;
    T r = aux->info;
    tope = tope->sig;
    delete aux;
    return r;
}

template <class T>
T Pila<T>::getElemento() {
    if(estaVacia()) throw PilaVacia();
    return tope->info;
}

template <class T>
int Pila<T>::getSize(){ return n; }

template <class T>
bool Pila<T>::estaVacia(){
    return tope == NULL;
}

template <class T>
void Pila<T>::imprimir(){
    cout<<"-> ";
    Elemento *t = tope;
    while(t!=NULL){
        cout<<t->info<<",";
        t = t->sig;
    }
    cout<<"\b]"<<endl;
}

template <class T>
ostream& operator<<(ostream &pout, const Pila<T> &p){
    pout<<"-> ";
    class Pila<T>::Elemento *t;
    t= p.tope;
    while(t!=NULL){
        pout<<t->info<<",";
        t = t->sig;
    }
    pout<<"\b]"<<endl;
    return pout;
}

