/**
	Autor: Montaño Valdez L. Roberto
	Fecha: 23/feb/2015 si, en esa fecha empezamos
*/

#include <iostream>
#include <cstdlib>

using namespace std;

class NoHayMemoria : public exception{
public: const char* what() const throw() { return "No hay memoria disponible."; }
};

class DivisionEntreCero : public exception{
public: const char* what() const throw() { return "Error: División entre Cero."; }
};

///********************RACIONAL***********************************///

class Racional{
    friend ostream& operator<<(ostream &, const Racional&);
    friend istream& operator>>(istream &, Racional&);
public:
    Racional(long int, long int);
    Racional():numerador(0), denominador(1){}

    Racional operator+(const Racional&) const;
    Racional operator-(const Racional&) const;
    Racional operator*(const Racional&) const;
    Racional operator/(const Racional&) const;

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
    long int x = numerador*a.denominador, y = a.numerador*denominador;
    return x<y;
}

bool Racional::operator==(const Racional &a) const{
    long int x = numerador*a.denominador, y = a.numerador*denominador;
    return x==y;
}

bool Racional::operator>=(const Racional &a) const{
    if(*this > a) return true;
    return *this == a;
}

bool Racional::operator<=(const Racional &a) const{
    if(*this < a) return true;
    return *this == a;
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

///***************Impresion de ListaOrdenada********************************///

template <class T, bool (*C)(T,T)> class ListaOrdenada;

template <class T, bool (*C)(T,T)>
ostream& operator<<(ostream &lout, const ListaOrdenada<T, C> &l){
	if(!l.primero){
		lout<<"[]";
		return lout;
	}
	lout<<'[';
	struct ListaOrdenada<T, C>::Elemento *aux = l.primero;
	while(aux) {
	    cout<<aux->info<<',';
	    aux = aux->sig;
	}
	lout<<"\b]";
	return lout;
}

///*******************Criterios de busqueda y ordenamiento***********************///

template <class T>
bool ascendente(T a, T b){ return a > b; }

template <class T>
bool descendente(T a, T b){ return a < b; }

template <class T>
bool distintoDe(T a, T b){ return a != b; }

///***********************LISTA ORDENADA**********************///

template <class T = int, bool (*C)(T,T) = ascendente >
class ListaOrdenada{
	friend ostream& operator<< <>(ostream&, const ListaOrdenada<T, C>&);
public:

	ListaOrdenada(bool ascend=true):n(0), primero(NULL) {}
	ListaOrdenada(const ListaOrdenada<T,C> &l):primero(NULL){ *this = l; }
	ListaOrdenada<T,C>& operator=(const ListaOrdenada<T,C>&);
	~ListaOrdenada(){ empty(); }

	void insert(T);
	bool remove(T);
	bool find(T);
	bool estaVacia() const {return primero==NULL;}
	void empty();

	int getSize(){ return n; }
private:
	struct Elemento{
		Elemento(T i, Elemento *s = NULL):info(i), sig(s) {}
		T info;
		Elemento *sig;
	} *primero;
	Elemento* find(T, bool (*)(T,T));
	int n;
};

///********************************Funciones miembro**********************************///

template<class T, bool(*C)(T,T)>
ListaOrdenada<T,C>& ListaOrdenada<T,C>::operator=(const ListaOrdenada<T,C> &l){
    if(this!=&l){
        this->empty();
        if(!l.estaVacia()){
            this->primero = new (nothrow) Elemento(l.primero->info);
            if(!this->primero) throw NoHayMemoria();
            ++this->n;
            Elemento *auxthat = l.primero->sig, *auxthis = this->primero;
            while(auxthat){
                auxthis->sig = new Elemento(auxthat->info);
                if(!auxthis->sig) throw NoHayMemoria();
                auxthat = auxthat->sig;
                auxthis = auxthis->sig;
                ++this->n;
            }
        }
    }
    return *this;
}

template<class T, bool (*C)(T,T)>
struct ListaOrdenada<T, C>::Elemento* ListaOrdenada<T, C>::find(T info, bool (*criterio)(T,T)){
    Elemento *aux = primero, *ant = NULL;
    while(aux && criterio(info,aux->info)){
        ant = aux;
        aux = aux->sig;
    }
    return ant;
}

template<class T, bool (*C)(T,T)>
void ListaOrdenada<T, C>::insert(T valor){
	Elemento *ant =find(valor, C), *aux = ant? ant->sig : primero;
	Elemento *nuevo = new Elemento(valor, aux);
	if(!nuevo) throw NoHayMemoria();
	if(ant) ant->sig = nuevo;
	else primero = nuevo;
	++n;
}

template <class T, bool (*C)(T,T)>
bool ListaOrdenada<T, C>::remove(T valor){
    if(estaVacia()) return false;
	Elemento *ant = find(valor, distintoDe), *aux = NULL;
	if(!ant){
        aux = primero->sig;
        delete primero;
        primero = aux;
        --n;
        return true;
	}
	if(!ant->sig) return false;
	aux = ant->sig;
	ant->sig = aux->sig;
	delete aux;
	--n;
	return true;

}

template <class T, bool (*C)(T,T)>
bool ListaOrdenada<T, C>::find(T valor){
    if(estaVacia()) return false;
	Elemento *ant = find(valor, distintoDe);
	return !ant || ant->sig;
}

template <class T, bool (*C)(T,T)>
void ListaOrdenada<T, C>::empty(){
    Elemento *aux;
    while(primero) {
        aux = primero;
        primero = primero->sig;
        delete aux;
    }
    n=0;
}

///*****************************MEIN*********************************///

int main(int argc, char const *argv[])
{

    int op, insertar;
	ListaOrdenada<int> Lint, Lint2, Lint3, *seleccionada = &Lint;

	cout<<"Listas de enteros ordenadas ascendentemente."<<endl<<endl;
	do
	{
        cout<<"Lista 1: "<<Lint<<", n: "<<Lint.getSize()<<endl;
        cout<<"Lista 2: "<<Lint2<<", n: "<<Lint2.getSize()<<endl;
        cout<<"Lista 3: "<<Lint3<<", n: "<<Lint3.getSize()<<endl<<endl;
        cout<<"Seleccionada: Lista "<<(seleccionada==&Lint?1:seleccionada==&Lint2?2:3)<<endl<<endl;

        cout<<"1)Seleccionar\n2)Insertar\n3)Remover\n4)Buscar\n5)Imprimir\n6)Copiar en otra\n9)Vaciar\n0)Continuar con listas de caracteres\n";
        cin>>op;
        switch(op){
            case 1:
                do{
                cout<<"seleccionar lista: ";
                cin>>insertar;
                }while(insertar<1 || insertar>3);
                seleccionada = insertar==1 ? &Lint : insertar==2 ? &Lint2 : &Lint3;
                break;
            case 2:
                cout<<"insertar: ";
                cin>>insertar;
                seleccionada->insert(insertar);
                break;
            case 3:
                cout<<"remover: ";
                cin>>insertar;
                if(seleccionada->remove(insertar)) cout<<"Removido elemento: "<<insertar;
                else cout<<"No se encontro el elemento";
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
                    cin>>insertar;
                }while(insertar<1 || insertar>3);
                (insertar==1 ? Lint : insertar==2 ? Lint2 : Lint3) = *seleccionada;
                break;
            case 9:
                seleccionada->empty();
        }
        if(op!=3 && op!=0) cout<<"Seleccionada: "<<(*seleccionada)<<", n: "<<seleccionada->getSize()<<endl<<endl;
	} while (op!=0);

    char c;
	ListaOrdenada<char, descendente> Lchar, Lchar2, Lchar3, *charseleccionada = &Lchar;

	cout<<"Listas de caracteres ordenadas descendentemente."<<endl<<endl;
	do
	{
        cout<<"Lista 1: "<<Lchar<<", n: "<<Lchar.getSize()<<endl;
        cout<<"Lista 2: "<<Lchar2<<", n: "<<Lchar2.getSize()<<endl;
        cout<<"Lista 3: "<<Lchar3<<", n: "<<Lchar3.getSize()<<endl<<endl;
        cout<<"Seleccionada: Lista "<<(charseleccionada==&Lchar?1:charseleccionada==&Lchar2?2:3)<<endl<<endl;

        cout<<"1)Seleccionar\n2)Insertar\n3)Remover\n4)Buscar\n5)Imprimir\n6)Copiar en otra\n9)Vaciar\n0)Continuar con listas de racionales\n";
        cin>>op;
        switch(op){
            case 1:
                do{
                cout<<"seleccionar lista: ";
                cin>>insertar;
                }while(insertar<1 || insertar>3);
                charseleccionada = insertar==1 ? &Lchar : insertar==2 ? &Lchar2 : &Lchar3;
                break;
            case 2:
                cout<<"insertar: ";
                cin>>c;
                charseleccionada->insert(c);
                break;
            case 3:
                cout<<"remover: ";
                cin>>c;
                if(charseleccionada->remove(c)) cout<<"Removido elemento: "<<c;
                else cout<<"No se encontro el elemento";
                cout<<endl;
                break;
            case 4:
                cout<<"buscar: ";
                cin>>c;
                cout<<(charseleccionada->find(c)?"Si ta":"No ta")<<endl;
                break;
            case 6:
                do{
                    cout<<"copiar en lista: ";
                    cin>>insertar;
                }while(insertar<1 || insertar>3);
                (insertar==1 ? Lchar : insertar==2 ? Lchar2 : Lchar3) = *charseleccionada;
                break;
            case 9:
                charseleccionada->empty();
        }
        if(op!=3 && op!=0) cout<<"Seleccionada: "<<(*charseleccionada)<<", n: "<<charseleccionada->getSize()<<endl<<endl;
	} while (op!=0);

    Racional r;
	ListaOrdenada<Racional, descendente> Lracional, Lracional2, Lracional3, *racionalseleccionada = &Lracional;

	cout<<"Listas de racionales ordenadas descendentemente."<<endl<<endl;
	do
	{
        cout<<"Lista 1: "<<Lracional<<", n: "<<Lracional.getSize()<<endl;
        cout<<"Lista 2: "<<Lracional2<<", n: "<<Lracional2.getSize()<<endl;
        cout<<"Lista 3: "<<Lracional3<<", n: "<<Lracional3.getSize()<<endl<<endl;
        cout<<"Seleccionada: Lista "<<(racionalseleccionada==&Lracional?1:racionalseleccionada==&Lracional2?2:3)<<endl<<endl;

        cout<<"1)Seleccionar\n2)Insertar\n3)Remover\n4)Buscar\n5)Imprimir\n6)Copiar en otra\n9)Vaciar\n0)Salir\n";
        cin>>op;
        switch(op){
            case 1:
                do{
                cout<<"seleccionar lista: ";
                cin>>insertar;
                }while(insertar<1 || insertar>3);
                racionalseleccionada = insertar==1 ? &Lracional : insertar==2 ? &Lracional2 : &Lracional3;
                break;
            case 2:
                cout<<"insertar: ";
                cin>>r;
                racionalseleccionada->insert(r);
                break;
            case 3:
                cout<<"remover: ";
                cin>>r;
                if(racionalseleccionada->remove(r)) cout<<"Removido elemento: "<<r;
                else cout<<"No se encontro el elemento";
                cout<<endl;
                break;
            case 4:
                cout<<"buscar: ";
                cin>>r;
                cout<<(racionalseleccionada->find(r)?"Si ta":"No ta")<<endl;
                break;
            case 6:
                do{
                    cout<<"copiar en lista: ";
                    cin>>insertar;
                }while(insertar<1 || insertar>3);
                (insertar==1 ? Lracional : insertar==2 ? Lracional2 : Lracional3) = *racionalseleccionada;
                break;
            case 9:
                racionalseleccionada->empty();
        }
        if(op!=3 && op!=0) cout<<"Seleccionada: "<<(*racionalseleccionada)<<", n: "<<racionalseleccionada->getSize()<<endl<<endl;
	} while (op!=0);

	return 0;
}
