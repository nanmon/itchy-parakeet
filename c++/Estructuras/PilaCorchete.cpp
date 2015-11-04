/**
	Nombre:
	Autor: L. Roberto Montaño Valdez
	Fecha:
	Descripción:
*/

#include<cstdlib>
#include<iostream>

using namespace std;
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

template <class T>
Pila<T>::~Pila(){ vaciar(); }

template <class T>
void Pila<T>::vaciar(){
    while(tope!=NULL) removerElemento();
}

template <class T>
void Pila<T>::insertarElemento(T i){
    Elemento *e = new (nothrow) Elemento(i, tope);
    if(e==NULL) throw false;
    tope = e;
    ++n;
}

template <class T>
T Pila<T>::removerElemento(){
    if(tope==NULL) throw "Error: Pila vacia";
    --n;
    Elemento *aux = tope;
    T r = aux->info;
    tope = tope->sig;
    delete aux;
    return r;
}

template <class T>
T Pila<T>::getElemento() {
    if(tope==NULL) return NULL;
    return tope->info;
}

template <class T>
int Pila<T>::getSize(){ return n; }

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

bool checarExp(char[]);

int main(int argc, char **argv){

    char expresion[100];
    cout<<"Inserte expresion matematica:\n";
    cin.getline(expresion, 100);

    if(checarExp(expresion)){
        cout<<"yey";
    }else cout<<":C";

	system("PAUSE");
	return 0;
}

bool checarExp(char ex[])
{

    Pila<char> pudin;

    for(int i=0; ex[i]!='\0'; ++i){
        if(ex[i]=='(' || ex[i]=='[' || i[ex]=='{') pudin.insertarElemento(ex[i]);
        if(ex[i]==')' || ex[i]==']' || ex[i]=='}'){
            char quitar;
            switch(ex[i]){
                case ')': quitar = '('; break;
                case ']': quitar = '['; break;
                case '}': quitar = '{';
            }
            if(pudin.getElemento()==quitar) pudin.removerElemento();
            else return false;
        }
    }

    return pudin.getSize()==0;
}
