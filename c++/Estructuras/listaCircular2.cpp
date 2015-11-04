/*
    Autor: Montaño Valdez L. Roberto
    Fecha: 20/mar/2015
*/

#include <cstdlib>
#include <iostream>

using namespace std;

class NoHayMemoria : public exception {
    const char* what() const throw() { return "No hay memoria disponible."; }
};

class RemoverDeListaVacia : public exception {
public: const char* what() const throw() { return "Error: Intento de remover elemento de lista vacia."; }
};

class DivisionEntreCero : public exception{
public: const char* what() const throw() { return "Error: División entre Cero."; }
};

///********************CLASE RACIONAL***********************////
class Racional{
    friend ostream& operator<<(ostream &, const Racional&);
    friend istream& operator>>(istream &, Racional&);
public:
    Racional(long int=0, long int=1);

    Racional operator+(const Racional&) const;
    Racional operator-(const Racional&) const;
    Racional operator*(const Racional&) const;
    Racional operator/(const Racional&) const;

    Racional& operator*=(const Racional &r) { *this = *this * r; return *this;}

    bool operator>(const Racional&) const;
    bool operator<(const Racional&) const;
    bool operator==(const Racional&) const;
    bool operator>=(const Racional&) const;
    bool operator<=(const Racional&) const;
    bool operator!=(const Racional&) const;
    operator bool() const;
private:
    void simplificar();
    void denPositivo();
    long int numerador, denominador;
};

///****************************Utileria**********************************///

void Racional::simplificar(){
    if(numerador==0){
        denominador = 1;
        return;
    }
    long int menor = numerador<denominador ? numerador : denominador;
    if(menor<0) menor*=-1;
    for(int i=2; i<=menor; ++i ){
        if(numerador%i==0 && denominador%i==0){
            numerador/=i;
            denominador/=i;
            --i;
        }
    }
}

void Racional::denPositivo(){
    if(denominador<0){
        denominador*=-1;
        numerador*=-1;
    }
}

///****************************Constructor*****************************///

Racional::Racional(long int num, long int den):numerador(num), denominador(den){
    if(denominador==0) throw DivisionEntreCero();
    denPositivo();
    simplificar();
}

///*******************************Operadores aritmeticos*********************************///

Racional Racional::operator+(const Racional &a) const{
    Racional r(numerador*a.denominador + a.numerador*denominador, denominador*a.denominador);
    return r;
}

Racional Racional::operator-(const Racional &a) const{
    Racional r(numerador*a.denominador - a.numerador*denominador, denominador*a.denominador);
    return r;
}

Racional Racional::operator*(const Racional &a) const{
    Racional r(numerador*a.numerador, denominador*a.denominador);
    return r;
}

Racional Racional::operator/(const Racional &a) const{
    Racional r(numerador*a.denominador, denominador*a.numerador);
    return r;
}

///*******************************Operadores relacionales***********************///

bool Racional::operator>(const Racional &a) const {
    long int x=numerador*a.denominador, y = a.numerador*denominador;
    return x>y;
}

bool Racional::operator<(const Racional &a) const{
    return a > *this;
}

bool Racional::operator>=(const Racional &a) const{
    return !(a > *this);
}

bool Racional::operator<=(const Racional &a) const{
    return !(*this > a);
}

bool Racional::operator==(const Racional &a) const{
    long int x = numerador*a.denominador, y = a.numerador*denominador;
    return x==y;
}

bool Racional::operator!=(const Racional &a) const{
    return !(*this == a);
}

Racional::operator bool() const{
    return (bool)numerador;
}

///****************************Impresion y captura********************************///
ostream& operator<<(ostream &rout, const Racional &r){
    rout<<r.numerador;
    if(r.denominador!=1) cout<<"/"<<r.denominador;
    return rout;
}

istream& operator>>(istream &rin, Racional &r){
    cout<<"Numerador: ";
    cin>>r.numerador;
    do{
        cout<<"Denominador [distinto de 0]: ";
        cin>>r.denominador;
        if(r.denominador==0) cout<<"Denominador invalido, vuelva a capturar. \n";
    }while(r.denominador==0);
    r.denPositivo();
    r.simplificar();
}

///*********************CON CABECERA FICTICIA**************************////
template <class T> class ListaCircular;

template <class T>
ostream& operator<<(ostream&, const ListaCircular<T>&);

template <class T = int>
class ListaCircular{
    friend ostream& operator<< <>(ostream&, const ListaCircular<T>&);
public:

    ListaCircular<T>();
    ListaCircular<T>(const ListaCircular<T>&);
    ListaCircular<T>& operator=(const ListaCircular<T>&);
    ~ListaCircular<T>(){ empty(); }

    void insert(T);
    bool remove(T);
    T remove();
    bool find(T) const;
    bool estaVacia() const { return cabecera->sig==cabecera; }
    void empty();

    int size() const { return n; }
private:
    struct Elemento{
        Elemento(T i, Elemento *s = NULL):info(i), sig(s) {}
        T info;
        Elemento *sig;
    } *cabecera;
    int n;
};

///*********************Constructores y operador=*************************///

template <class T>
ListaCircular<T>::ListaCircular():n(0) {
    cabecera = new (nothrow) Elemento(24);
    if(!cabecera) throw NoHayMemoria();
    cabecera->sig = cabecera;
}

template <class T>
ListaCircular<T>::ListaCircular(const ListaCircular<T> &l){
    cabecera = new (nothrow) Elemento(24);
    if(!cabecera) throw NoHayMemoria();
    cabecera->sig = cabecera;
    *this = l;
}

template<class T>
ListaCircular<T>& ListaCircular<T>::operator=(const ListaCircular<T> &l){
    if(this!=&l){
        this->empty();
        Elemento *auxthat = l.cabecera->sig;
        while(auxthat!=l.cabecera) {
            this->insert(auxthat->info);
            auxthat = auxthat->sig;
        }
    }
    return *this;
}

///**************************Funciones miembro************************///

template <class T>
void ListaCircular<T>::insert(T valor){
    Elemento *nuevo = new (nothrow) Elemento(valor, cabecera->sig);
    if(!nuevo) throw NoHayMemoria();
    cabecera->sig = nuevo;
    ++n;
}

template <class T>
bool ListaCircular<T>::remove(T valor){
    Elemento *aux = cabecera, *borrar;
    while(aux->sig!=cabecera && aux->sig->info != valor)
        aux = aux->sig;
    if(aux->sig==cabecera) return false;
    borrar = aux->sig;
    aux->sig = borrar->sig;
    delete borrar;
    --n;
    return true;
}

template <class T>
T ListaCircular<T>::remove(){
    if(estaVacia()) throw RemoverDeListaVacia();
    Elemento *borrar = cabecera->sig;
    cabecera->sig = borrar->sig;
    T info = borrar->info;
    delete borrar;
    --n;
    return info;
}

template <class T>
bool ListaCircular<T>::find(T valor) const{
    Elemento *aux = cabecera;
    while(aux->sig!=cabecera && aux->sig->info != valor)
        aux = aux->sig;
    return aux->sig!=cabecera;
}

template <class T>
void ListaCircular<T>::empty(){
    Elemento *aux = cabecera, *borrar;
    while(aux->sig!=cabecera){
        borrar = aux->sig;
        aux->sig = borrar->sig;
        delete borrar;
    }
    n=0;
}

///*************************sobrecarga de impresion*********************///

template <class T>
ostream& operator<<(ostream &lout, const ListaCircular<T> &l){
    if(l.estaVacia())
        return lout<<"[]";
    struct ListaCircular<T>::Elemento *aux = l.cabecera->sig;
    lout<<"[";
    while(aux != l.cabecera){
        lout<<aux->info<<',';
        aux = aux->sig;
    }
    return lout<<"\b]";
}

///*************************MEIN & MENU***************************///

template <typename T> void menu(bool);

int main(int argc, const char* argv[]){

    cout<<"Listas Circulares de enteros"<<endl;
    menu<int>(true);
    cout<<"Listas Circulares de caracteres"<<endl;
    menu<char>(true);
    cout<<"Listas Circulares de Racionales"<<endl;
    menu<Racional>(false);

    system("PAUSE");
    return 0;
}

template <typename T>
void menu(bool continuar){
    int op;
    T insertar;
    int sel;
    ListaCircular<T> Lint, Lint2, Lint3, *seleccionada = &Lint;

    do{
        cout<<"Lista 1: "<<Lint<<", n: "<<Lint.size()<<endl;
        cout<<"Lista 2: "<<Lint2<<", n: "<<Lint2.size()<<endl;
        cout<<"Lista 3: "<<Lint3<<", n: "<<Lint3.size()<<endl<<endl;
        cout<<"Seleccionada: Lista "<<(seleccionada==&Lint?1:seleccionada==&Lint2?2:3)<<endl<<endl;

        cout<<"1)Seleccionar\n2)Insertar\n3)Remover\n4)Buscar\n5)Imprimir\n6)Copiar en otra\n9)Vaciar\n0)";
        cout<<(continuar?"Continuar": "Salir")<<endl;
        cin>>op;
        switch(op){
            case 1:
                do{
                    cout<<"seleccionar lista: ";
                    cin>>sel;
                }while(sel<1 || sel>3);
                seleccionada = sel==1 ? &Lint : sel==2 ? &Lint2 : &Lint3;
                break;
            case 2:
                cout<<"insertar: ";
                cin>>insertar;
                seleccionada->insert(insertar);
                break;
            case 3:
                do{
                    cout<<"remover: 1)Primero de la lista\t2)Elemento especifico\n";
                    cin>>sel;
                }while(sel!=1 && sel!=2);
                if(sel==1){
                    try{
                        cout<<"Removido elemento:"<<seleccionada->remove();
                    }catch(RemoverDeListaVacia e){
                        cout<<e.what()<<endl;
                        cout<<"excepcion atrapada C:"<<endl;
                    }
                }
                else{
                    cout<<"remover: ";
                    cin>>insertar;
                    if(seleccionada->remove(insertar)) cout<<"Removido elemento: "<<insertar;
                    else cout<<"No se encontro el elemento";
                }
                cout<<endl;
                break;
            case 4:
                cout<<"buscar: ";
                cin>>insertar;
                cout<<(seleccionada->find(insertar)?"Si ta":"No ta")<<endl;
                break;
            case 6:
                do{
                    cout<<"copiar en lista: ";
                    cin>>sel;
                }while(sel<1 || sel>3);
                (sel==1 ? Lint : sel==2 ? Lint2 : Lint3) = *seleccionada;
                break;
            case 9:
                seleccionada->empty();
        }
        if(op!=3 && op!=0) cout<<"Seleccionada: "<<(*seleccionada)<<", n: "<<seleccionada->size()<<endl<<endl;
    } while (op!=0);
}
