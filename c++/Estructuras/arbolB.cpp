/*
Autor: Roberto Montaño
Fecha: 17/abr/2015
*/

#include <iostream>
#include <cstdlib>

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

///*************************ARBOL B********************************////

template <int grado>
class ArbolB{
public:
	ArbolB();
	ArbolB(const ArbolB<grado>&);
	~ArbolB();
	ArbolB<grado>& operator=(const ArbolB<grado>&);

	bool insert(int);
	bool remove(int);
	bool find(int);
	void vaciar();
	void imprimir(int);
	int getSize();
private:
	struct Nodo;
	Nodo *raiz;
	int n;
	static const int MIN = (grado-1)/2;

	void rebalancear(Nodo*);
	void imprimir(Nodo*);
	void imprimirAsc(Nodo*);
	void imprimirDesc(Nodo*);
	void imprimirNiveles();
};

///*************************NODO***********************///
template <int grado>
struct ArbolB<grado>::Nodo
{
	Nodo(int, Nodo*);
	Nodo(Nodo*);
	Nodo& operator=(const Nodo&);
	void vaciar();

	bool insert(int, Nodo* = NULL);
	bool remove(int);
	void split();
	void merge();
	bool pedirPrestado();
	Nodo* getSon(int);
	bool tieneHijos() const { return hijos[0] != NULL; }
	Nodo* mayorDeLosMenores();
	int infos[grado], n;
	Nodo *hijos[grado+1], *padre;
};

template <int g>
ArbolB<g>::Nodo::Nodo(int i, Nodo *p):padre(p), n(1){
	infos[0] = i;
}

template <int g>
ArbolB<g>::Nodo::Nodo(Nodo *h):padre(NULL), n(0){
	hijos[0] = h;
}

template <int g>
struct ArbolB<g>::Nodo& ArbolB<g>::Nodo::operator=(const Nodo &nod){
	if(this!=&nod){
		n = nod.n;
		Nodo *aux;
		for(int i=0; i<n; ++i) infos[i] = nod.infos[i];
		if(nod.tieneHijos())
			for (int i = 0; i <= n; ++i){
				hijos[i] = new (nothrow) Nodo(-1, this);
				if(!hijos[i]) throw NoHayMemoria();
				*(hijos[i]) = *(nod.hijos[i]);
			}
	}
	return *this;
}

template <int g>
void ArbolB<g>::Nodo::vaciar(){
	if(tieneHijos())
		for (int i = 0; i <= n; ++i)
			hijos[i]->vaciar();
	delete this;

}

template <int g>
bool ArbolB<g>::Nodo::insert(int a, Nodo *h){
	int aux;
	Nodo *naux;
	if(n==0 || infos[n-1] < a){
		infos[n++] = a;
		hijos[n] = h;
		return true;
	}
	for(int i=0; i<n; ++i){
		if(infos[i] > a){
			infos[n] = infos[i];
			infos[i] = a;
			hijos[n+1] = hijos[i+1];
			hijos[i+1] = h;
			for(int j=n++ -1; j>i; --j){
				aux = infos[j];
				infos[j] = infos[j+1];
				infos[j+1] = aux;
				naux = hijos[j+1];
				hijos[j+1] = hijos[j+2];
				hijos[j+2] = naux;
			}
			break;
		}else if(infos[i] == a) return false;
	}
	return true;
}

template <int g>
bool ArbolB<g>::Nodo::remove(int a){
	int i=0;
	for (; i < n; ++i)
		if(infos[i] == a) break;
	if(i == n) return false;
	if(!tieneHijos()){
		for (int j = i; j < n-1; ++j)
			infos[j] = infos[j+1];
		if(--n == 0){
			///:c
		}
		return true;
	}
	Nodo *swap = mayorDeLosMenores();
	infos[i] = swap->infos[swap->n-1];
	--swap->n;
	return true;
}

template <int g>
void ArbolB<g>::Nodo::split(){
	int min = (g-1)/2;
	Nodo *der = new (nothrow) Nodo(infos[min+1], padre);
	if(!der) throw NoHayMemoria();
	n = min;
	for(int i=min+2; i<g; ++i)
		der->infos[der->n++] = infos[i];

	if(tieneHijos()){
		for(int i=min+1; i<=g; ++i){
			hijos[i]->padre = der;
			der->hijos[i-min-1] = hijos[i];
		}
	}
	padre->insert(infos[min], der);
}

template <int g>
void ArbolB<g>::Nodo::merge(){
	//buscar indice en padre
	Nodo *hermano;
	int i=0;
	for(; padre->hijos[i] != this; ++i);
	if(i==0){
		hermano = padre->hijos[i+1];
		infos[n++] = padre->infos[i];
		for(int j=0; j<hermano->n; ++j)
			infos[n++] = hermano->infos[j];
		for(int j=i; j<padre->n-1; ++j){
			padre->infos[j] = padre->infos[j+1];
			padre->hijos[j+1] = padre->hijos[j+2];
		}
		--padre->n;
		delete hermano;
	}else{
		hermano = padre->hijos[i-1];
		hermano->infos[hermano->n++] = padre->infos[i-1];
		for(int j=0; j<n; ++j)
			hermano->infos[hermano->n++] = infos[j];
		for(int j=i-1; j<padre->n-1; ++j){
			padre->infos[j] = padre->infos[j+1];
			padre->hijos[j+1] = padre->hijos[j+2];
		}
		--padre->n;
		delete this;
	}
}

template <int g>
bool ArbolB<g>::Nodo::pedirPrestado(){
	Nodo *hermano;
	int i=0;
	for(; padre->hijos[i] != this; ++i);
	if(i==0 || (i!=n && padre->hijos[i+1] > padre->hijos[i-1])){
		hermano = padre->hijos[i+1];
		if(hermano->n == (g-1)/2) return false;
		infos[n++] = padre->infos[i];
		padre->infos[i] = hermano->infos[0];
		for(int j=0; j<hermano->n -1; ++j)
			hermano->infos[j] = hermano->infos[j+1];
		--hermano->n;
	}else{
		hermano = padre->hijos[i-1];
		if(hermano->n == (g-1)/2) return false;
		for(int j=n++; j>0; --j)
			infos[j] = infos[j-1];
		infos[0] = padre->infos[i-1];
		padre->infos[i-1] = hermano->infos[--hermano->n];
	}
	return true;
}

template <int g>
struct ArbolB<g>::Nodo* ArbolB<g>::Nodo::getSon(int valor){
	for(int i=0; i<n; ++i)
		if(valor < infos[i]) return hijos[i];
		else if(valor == infos[i]) return NULL;
	return hijos[n];
}

template <int g>
struct ArbolB<g>::Nodo* ArbolB<g>::Nodo::mayorDeLosMenores(){
	Nodo *aux = hijos[0];
	while(aux->tieneHijos())
		aux = aux->hijos[aux->n];
	return aux;
}

////////////////////////////////////////////////////CONSTRUCTORES

template <int g>
ArbolB<g>::ArbolB():raiz(NULL), n(0){}

template <int g>
ArbolB<g>::ArbolB(const ArbolB<g> &a):raiz(NULL), n(0){
	*this = a;
}

template <int g>
ArbolB<g>& ArbolB<g>::operator=(const ArbolB<g> &a){
	if(this!=&a){
        vaciar();
		n = a.n;
		raiz = new (nothrow) Nodo(-1, NULL);
		if(!raiz) throw NoHayMemoria();
		*raiz = *(a.raiz);
	}
	return *this;
}

template <int g>
ArbolB<g>::~ArbolB<g>(){
	vaciar();
}

//////////////////////////////////////////////////FUNCIONES MIEMBRO

template <int g>
bool ArbolB<g>::insert(int valor){
	Nodo *aux = raiz;
	while(aux){
		if(!aux->tieneHijos()){
			if(aux->insert(valor)){
				rebalancear(aux);
				++n;
				return true;
			}else return false;
		}else {
			aux = aux->getSon(valor);
			if(!aux) return false;
		}
	}
	raiz = new (nothrow) Nodo(valor, NULL);
	if(!raiz) throw NoHayMemoria();
	++n;
	return true;
}

template <int g>
bool ArbolB<g>::remove(int valor){
	Nodo *aux = raiz;
	while(aux)
		if(aux->remove(valor)){
			if(aux->tieneHijos()) aux = aux->mayorDeLosMenores();
			rebalancear(aux);
			--n;
			return true;
		}else aux = aux->getSon(valor);
	return false;
}

template <int g>
bool ArbolB<g>::find(int valor){
    Nodo *actual = raiz;
    int i;
    while(actual){
        for(i=0; i<actual->n; ++i){
            if(actual->infos[i] == valor) return true;
            else if(actual->infos[i] < valor) break;
        }
        actual = actual->hijos[i];
    }
    return false;
}

template <int g>
void ArbolB<g>::vaciar(){
	if(!raiz) return;
	raiz->vaciar();
	raiz = NULL;
	n = 0;
}

template <int g>
int ArbolB<g>::getSize(){
	return n;
}

template <int g>
void ArbolB<g>::rebalancear(Nodo *r){
	if(r->n == g){
		//>=MAX
		if(r==raiz){
			r->padre = new (nothrow) Nodo(r);
			if(!r->padre) throw NoHayMemoria();
			raiz = r->padre;
		}
		r->split();
		if(r->padre) rebalancear(r->padre);
	}else if (r->n < MIN){
		if(r == raiz){
			if(r->n == 0){
				raiz = NULL;
				delete r;
			}
			return;
		}
		if(!r->pedirPrestado()) r->merge();
		if(r->padre) rebalancear(r->padre);
	}
}

template <int g>
void ArbolB<g>::imprimir(int tipo){
	if(!raiz){
		cout<<"ARBOL VACIO"<<endl;
		return;
	}
	switch(tipo){
	case 0:
		imprimir(raiz);
		break;
	case 1:
		cout<<"[";
		imprimirAsc(raiz);
		cout<<"\b]"<<endl;
		break;
	case 2:
		cout<<"[";
		imprimirDesc(raiz);
		cout<<"\b]"<<endl;
		break;
	case 3:
		imprimirNiveles();
		break;
    default:
        cout<<"No existe la opcion"<<endl;
	}
}

template <int g>
void ArbolB<g>::imprimir(Nodo *r){
	cout<<"[";
	for(int i=0; i<r->n; ++i)
		cout<<r->infos[i]<<",";
	cout<<"\b]";
	if(r->tieneHijos()){
		cout<<" -> ";
		for(int i=0; i<=r->n; ++i)
			cout<<r->hijos[i]->infos[0]<<",";
		cout<<"\b \n";
		for(int i=0; i<=r->n; ++i)
			imprimir(r->hijos[i]);
	}else cout<<endl;
}

template <int g>
void ArbolB<g>::imprimirAsc(Nodo *r){
	if(r->tieneHijos()){
		for(int i=0; i<r->n; ++i){
			imprimirAsc(r->hijos[i]);
			cout<<r->infos[i]<<",";
		}
		imprimirAsc(r->hijos[r->n]);
	}else{
		for (int i = 0; i < r->n; ++i)
			cout<<r->infos[i]<<",";
	}
}

template <int g>
void ArbolB<g>::imprimirDesc(Nodo *r){
	if(r->tieneHijos()){
		imprimirDesc(r->hijos[r->n]);
		for (int i = r->n - 1; i >= 0; --i){
			cout<<r->infos[i]<<",";
			imprimirDesc(r->hijos[i]);
		}
	}else{
		for (int i = r->n - 1; i >= 0; --i)
			cout<<r->infos[i]<<",";
	}
}

template <int g>
void ArbolB<g>::imprimirNiveles(){
	Cola<Nodo*> nodos;
	int enEsteNivel = 0, saltarLineaEn = 1, conteo = 0;
	nodos.encolar(raiz);
	Nodo *actual;
	while(!nodos.estaVacia()){
		actual = nodos.sacar();
		cout<<"|";
		for(int i=0; i<actual->n; ++i)
			cout<<actual->infos[i]<<",";
		cout<<"\b|;";
		if(actual->tieneHijos()) {
			enEsteNivel+= actual->n+1;
			for(int i=0; i<=actual->n; ++i)
				nodos.encolar(actual->hijos[i]);
		}
		if(++conteo == saltarLineaEn){
			cout<<endl;
			saltarLineaEn = enEsteNivel;
			conteo = enEsteNivel = 0;
		}
	}
	cout<<endl;
}

///**********************************MEIN****************************/////

template <int>
void menu();

int main(int argc, char const *argv[])
{
	int op;
	do{
		cout<<"gradito del arbolito: ";
		cin>>op;
	}while(op<3 || op>7);
	/*
		No me dejaba solo poner:
			menu<op>();
		me decia que op no es un constexpr :C
    */
	switch(op){
		case 3:
			menu<3>();
			break;
		case 4:
			menu<4>();
			break;
		case 5:
			menu<5>();
			break;
		case 6:
			menu<6>();
			break;
		case 7:
			menu<7>();
	}

	system("PAUSE");
	return 0;
}

template <int g>
void menu(){

	int op, in;
	ArbolB<g> a, b, c, *select = &a;

	do{
		cout<<"Arbol 1: "<<a.getSize()<<" valores"<<endl;
		cout<<"Arbol 2: "<<b.getSize()<<" valores"<<endl;
		cout<<"Arbol 3: "<<c.getSize()<<" valores"<<endl;
		cout<<endl<<"Seleccionado: Arbol "<<(select==&a ? 1 : select==&b ? 2 : 3)<<endl;
		cout<<"1)Insertar\n2)Remover\n3)Buscar\n4)Imprimir\n5)Seleccionar\n6)Copiar\n7)Vaciar\n0)Salir"<<endl;
		cin>>op;
		switch(op){
		case 1:
			cout<<"insertar: ";
			cin>>in;
			cout<<(select->insert(in) ? "Insertado" : "Ya taba")<<endl;
			break;
		case 2:
			cout<<"remove: ";
			cin>>in;
			cout<<(select->remove(in) ? "Removido" : "No taba")<<endl;
			break;
        case 3:
            cout<<"find: ";
            cin>>in;
            cout<<(select->find(in)? "Si ta" : "No ta" )<<endl;
            break;
		case 4:
			cout<<"1)Ascendente\t2)Descendente\t3)Por Niveles\n";
			cin>>in;
			select->imprimir(in);
			break;
        case 5:
            cout<<"select: Arbol ";
            cin>>in;
            if(in>=1 && in<=3) select = in == 1 ? &a : in == 2 ? &b : &c;
            break;
        case 6:
            cout<<"copiar en: Arbol ";
            cin>>in;
            if(in>=1 && in<=3)
                (in == 1 ? a : in == 2 ? b : c) = *select;
            break;
        case 7:
            select->vaciar();
		}

		//c.imprimir();
	}while(op!=0);
}
