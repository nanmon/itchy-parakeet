/**
	Nombre:
	Autor: L. Roberto Montaño Valdez
	Fecha: 22/feb/2015
	Descripción:
*/

#include <cstdlib>
#include <iostream>

using namespace std;

///******************Excepciones*************************///

class ColaVacia : public exception {
    const char* what() const throw(){ return "La cola esta vacia"; }
};

class NoHayMemoria : public exception {
    const char* what() const throw(){ return "No hay memoria disponible"; }
};

///***********************Sobrecarga cout*************************///
template <class T> class Cola;

template <class T>
ostream& operator<<(ostream &qout, const Cola<T> &q){
    qout<<"<<";
    if(!q.estaVacia()){
        struct Cola<T>::Elemento *aux = q.ultimo->sig;
        do{
            cout<<aux->info<<',';
            aux = aux->sig;
        }while(aux!=q.ultimo->sig);
    }
    cout<<"\b<";
    return qout;
}

///****************************Clase Cola*****************************///
template <class T>
class Cola{
    friend ostream& operator<< <>(ostream&, const Cola<T>&);
public:
    Cola();
    Cola(const Cola&);
    Cola& operator=(const Cola<T>&);
    ~Cola();

    bool estaVacia() const{ return ultimo==NULL; }
    void vaciar();

    void encolar(T);
    T sacar();
    T get() const;
    int size() const;
private:
    struct Elemento{
        Elemento(T i, Elemento *n=NULL):info(i), sig(n){}
        Elemento *sig;
        T info;
    } *ultimo;
    int n;
};

///*****************Constructores, destructor, operator=******************///

template <class T>
Cola<T>::Cola():n(0), ultimo(NULL){}

template <class T>
Cola<T>::Cola(const Cola<T> &c):n(0), ultimo(NULL){
    *this = c;
}

template <class T>
Cola<T>& Cola<T>::operator=(const Cola<T> &c){
    if(this!=&c){
        vaciar();
        if(!c.estaVacia()){
            Elemento *aux = c.ultimo->sig;
            do{
                encolar(aux->info);
                aux = aux->sig;
            }while(aux != c.ultimo->sig);
        }
    }
    return *this;
}

template <class T>
Cola<T>::~Cola(){
    vaciar();
}

///*********************Funciones miembro*******************///

template <class T>
void Cola<T>::vaciar(){
    while(!estaVacia()) sacar();
}

template <class T>
void Cola<T>::encolar(T i) {
    Elemento *aux = new (nothrow) Elemento(i, estaVacia() ? NULL : ultimo->sig);
    if(!aux) throw NoHayMemoria();
    if(estaVacia()) aux->sig = aux;
    else ultimo->sig = aux;
    ultimo = aux;
    ++n;
}

template <class T>
T Cola<T>::sacar(){
    if(estaVacia()) throw ColaVacia();
    --n;
    Elemento *aux = ultimo->sig;
    T i = aux->info;
    ultimo->sig = aux->sig;
    if(ultimo==aux) ultimo = NULL;
    delete aux;
    return i;
}

template <class T>
T Cola<T>::get() const{
    if(estaVacia()) throw ColaVacia();
    return ultimo->sig->info;
}

template <class T>
int Cola<T>::size() const{
    return n;
}

///**************************MEIN**************************///

int main(int argc, char *argv[]){

    Cola<int> c;
    c.encolar(1);
    cout<<c<<endl;
    c.encolar(2);
    cout<<c<<endl;

    Cola<int> c2 = c, c3;
    c2.encolar(3);
    cout<<c2<<endl;
    c2.encolar(4);
    cout<<c2<<endl<<c2.size()<<endl;
    c3 = c2 = c;
    cout<<c<<endl<<c2<<endl<<c3<<endl;
    cout<<c.get()<<endl;
    cout<<c2.size()<<endl;
    c2.sacar();
    cout<<c2<<endl;
    c2.sacar();
    cout<<c2<<endl;
    //system("PAUSE");
    return 0;

	system("PAUSE");
	return 0;
}
