/*
Autor: Nan Montaño
Fecha: 06/abr/2015
*/

#include <iostream>
#include <cstdlib>
#include <cmath>
#include <iomanip>

using namespace std;

class NoHayMemoria : public exception {
public: const char* what() const throw() { return "No hay memoria disponible."; }
};

class ColaVacia : public exception {
 public: const char* what() const throw(){ return "La cola esta vacia"; }
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

///****************************ARBOL BINARIO DE BUSQUEDA***********************///
/////////////////////////////////////////////////////////////////////////////////

template <class T>
class ABB{
public:
	ABB():raiz(NULL), n(0){}
	ABB(const ABB &a):raiz(NULL), n(0) { *this = a; }
	~ABB(){ vaciar(); }
	ABB& operator=(const ABB&);

	bool agregar(T);
	bool remover(T);
	void imprimir(int);
	void vaciar();
	int tamanio() { return n; }
private:
	int n;
	struct Nodo
	{
		Nodo(T i):info(i), hizq(NULL), hder(NULL){}

		Nodo& operator=(const Nodo &n){
			if(this!=&n){
				this->info = n.info;
				Nodo *auxh = hizq, *auxd = hder;
				if(n.hizq){
					this->hizq = new (nothrow) Nodo(n.hizq->info);
					if(!hizq) throw NoHayMemoria();
					*(this->hizq) = *(n.hizq);
				} if(auxh) delete auxh;
				if(n.hder){
					this->hder = new (nothrow) Nodo(n.hder->info);
					if(!hder) throw NoHayMemoria();
					*(this->hder) = *(n.hder);
				} if(auxd) delete auxd;
			}
			return *this;
		}

		T info;
		Nodo *hizq, *hder;
	} *raiz;

	void imprimirArbol(Nodo*, int, bool);
	void imprimirInorden(Nodo*);
	void imprimirInordenInv(Nodo*);
	void imprimirNiveles();
	void vaciar(Nodo*);
	Nodo* menorDeLosMayores(Nodo*);
	Nodo* mayorDeLosMenores(Nodo*);
};

//////////////////////////////////////operator=

template <class T>
ABB<T>& ABB<T>::operator=(const ABB<T> &a){
	if(this!=&a){
		vaciar();
		raiz = new (nothrow) Nodo(a.raiz->info);
		if(!raiz) throw NoHayMemoria();
		*raiz = *(a.raiz);
	}
	return *this;
}

////////////////////////////////////////////FUnciones miembro

template <class T>
bool ABB<T>::agregar(T valor){
	Nodo **r = &raiz;
	while(*r){
		if(valor < (*r)->info) r = &(*r)->hizq;
		else if(valor > (*r)->info) r = &(*r)->hder;
		else return false;
	}
	*r = new (nothrow) Nodo(valor);
	if(!*r) throw NoHayMemoria();
	++n;
	return true;
}

template <class T>
bool ABB<T>::remover(T valor){
	Nodo *r = raiz, *padre = NULL, *swapper;
	while(r){
		if(valor < r->info){
			padre = r;
			r = r->hizq;
		}else if(valor > r->info){
			padre = r;
		 	r = r->hder;
		}else break;
	}
	if(!r) return false;
	--n;
	swapper = mayorDeLosMenores(r);
	if(!swapper) {
		swapper = menorDeLosMayores(r);
		if(!swapper){
			if(!padre) raiz = NULL;
			else (padre->hizq == r ? padre->hizq : padre->hder) = NULL;
			delete r;
			return true;
		}else if(r != swapper){
			padre = swapper;
			swapper = swapper->hizq;
			padre->hizq = swapper->hder;
		}else {
			swapper = r->hder;
			r->hder = NULL;
		}
	}else if(r != swapper){
		padre = swapper;
		swapper = swapper->hder;
		padre->hder = swapper->hizq;
	}else {
		swapper = r->hizq;
		r->hizq = NULL;
	}
	r->info = swapper->info;
	delete swapper;
	return true;
}

template <class T>
struct ABB<T>::Nodo* ABB<T>::menorDeLosMayores(Nodo *r){
	Nodo *aux = r->hder, *padre = r;
	if(!aux) return NULL;
	while(aux->hizq){
		padre = aux;
		aux = aux->hizq;
	}
	return padre;
}

template <class T>
struct ABB<T>::Nodo* ABB<T>::mayorDeLosMenores(Nodo *r){
	Nodo *aux = r->hizq, *padre = r;
	if(!aux) return NULL;
	while(aux->hder){
		padre = aux;
		aux = aux->hder;
	}
	return padre;
}

template <class T>
void ABB<T>::vaciar(){
	if(!raiz) return;
	vaciar(raiz);
	raiz = NULL;
	n = 0;
}

template <class T>
void ABB<T>::vaciar(Nodo *r){
	if(r->hizq) vaciar(r->hizq);
	if(r->hder) vaciar(r->hder);
	delete r;
}

template <class T>
void ABB<T>::imprimirArbol(Nodo *r, int nivel, bool izq = true){
	if(r->hder) imprimirArbol(r->hder, nivel+1, false);
	if(nivel!=0){
		cout<<setw((nivel-1)*7)<<" "<<(izq?"^":"v")<<"----";
	}
	cout<<"["<<r->info<<"]"<<endl;
	if(r->hizq) imprimirArbol(r->hizq, nivel+1, true);
}

template <class T>
void ABB<T>::imprimirInorden(Nodo *r){
	if(r->hizq) imprimirInorden(r->hizq);
	cout<<r->info<<",";
	if(r->hder) imprimirInorden(r->hder);
}

template <class T>
void ABB<T>::imprimirInordenInv(Nodo *r){
	if(r->hder) imprimirInordenInv(r->hder);
	cout<<r->info<<",";
	if(r->hizq) imprimirInordenInv(r->hizq);
}

template <class T>
void ABB<T>::imprimirNiveles(){
	Cola<Nodo*> nodos;
	nodos.encolar(raiz);
	Nodo *aux;
	while(!nodos.estaVacia()){
		aux = nodos.sacar();
		cout<<aux->info<<",";
		if(aux->hizq) nodos.encolar(aux->hizq);
		if(aux->hder) nodos.encolar(aux->hder);
	}
}

template <class T>
void ABB<T>::imprimir(int tipo = 1){
	if(!raiz){
		cout<<"Arbol vacio."<<endl;
		return;
	}
	switch(tipo){
		case 0: 
			imprimirArbol(raiz, 0);
			break;
		case 1: 
			imprimirInorden(raiz);
			break;
		case 2:
			imprimirInordenInv(raiz);
			break;
		case 3:
			imprimirNiveles();
			break;
	}
}

///////////////////////////////////MEIN

template <class T>
void menu();

int main(int argc, char const *argv[])
{
	cout<<"Arbol binario de enteros"<<endl;
	menu<int>();
	cout<<"Arbol binario de caracteres"<<endl;
	menu<char>();

	system("PAUSE");
	return 0;
}

template <class T>
void menu(){
	ABB<T> a, b, c, *sel = &a;
	int op, select = 1, aux;
	T in;
	do{
		cout<<"Arbol 1: "<<a.tamanio()<<" nodos"<<endl;
		cout<<"Arbol 2: "<<b.tamanio()<<" nodos"<<endl;
		cout<<"Arbol 3: "<<c.tamanio()<<" nodos"<<endl;
		cout<<"\nSeleccionado: Arbol "<<select<<endl;
		cout<<"\n1)Seleccionar\n2)Agregar\n3)Remover\n4)Imprimir\n5)Copiar\n6)Vaciar\n0)Salir\n";
		cin>>op;
		switch(op){
			case 1:
				do{
					cout<<"seleccionar: ";
					cin>>select;
				}while(select<1 && select>3);
				sel = select == 1 ? &a : select == 2 ? &b : &c;
				break;
			case 2:
				cout<<"agregar: ";
				cin>>in;
				cout<<(sel->agregar(in)?"Nodo agregado":"Nodo ya existe")<<endl;
				break;
			case 3:
				cout<<"remover: ";
				cin>>in;
				cout<<(sel->remover(in)?"Nodo removido":"Nodo no existe")<<endl;
				break;
			case 4:
				do{
					cout<<"Imprimir:\n0)Arbol\t1)Ascendente\t2)Descendente\t3)Por Niveles\n";
					cin>>aux;
				}while(aux<0 && aux>4);
				sel->imprimir(aux);
				cout<<endl;
				break;
			case 5:
				do{
					cout<<"copiar seleccionado en: Arbol ";
					cin>>aux;
				}while(aux<1 && aux>3);
				(aux == 1 ? a : aux == 2 ? b : c) = *sel;
				break;
			case 6:
				sel->vaciar();
		}

	}while(op != 0);
}