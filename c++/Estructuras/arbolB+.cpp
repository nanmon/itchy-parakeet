/*
Autores: Roberto Montaño, Carlos Huguez
Fecha: 24/may/2015
*/

#include <iostream>
#include <cstdlib>
#include <string>

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

///*************************ARBOL B+********************************////

template <class T, int g>
class ArbolBMas{
public:
	ArbolBMas();
	ArbolBMas(const ArbolBMas<T, g>&);
	~ArbolBMas();
	ArbolBMas<T, g>& operator=(const ArbolBMas<T, g>&);

	bool insert(T);
	bool remove(T);
	bool find(T);
	void vaciar();
	void imprimir(int);
	int getSize();
private:
	struct Nodo;
	Nodo *raiz;
	int n;
	static const int MIN = (g-1)/2;

	void rebalancear(Nodo*);
	void imprimir(Nodo*, int);
	void imprimirAsc();
	void imprimirDesc(Nodo*);
	void imprimirNiveles();
};

///*************************NODO***********************///
template <class T, int g>
struct ArbolBMas<T, g>::Nodo
{
	Nodo(T, Nodo*);
	Nodo(Nodo*);
	Nodo& operator=(const Nodo&);
	void vaciar();

	bool insert(T, Nodo* = NULL);
	bool remove(T);
	void split();
	void merge();
	bool pedirPrestado();
	Nodo* getSon(T);
	bool tieneHijos() const { return hijos[1] != NULL; }
	Nodo* mayorDeLosMenores();
	T infos[g];
	int n;
	Nodo *hijos[g+1], *padre;
};

template <class T, int g>
ArbolBMas<T, g>::Nodo::Nodo(T i, Nodo *p):padre(p), n(1){
	infos[0] = i;
	for(int i=0; i<=g; ++i) hijos[i] = NULL;
}

template <class T, int g>
ArbolBMas<T, g>::Nodo::Nodo(Nodo *h):padre(NULL), n(0){
	hijos[0] = h;
	for(int i=1; i<=g; ++i) hijos[i] = NULL;
}

template <class T, int g>
struct ArbolBMas<T, g>::Nodo& ArbolBMas<T, g>::Nodo::operator=(const Nodo &nod){
	if(this!=&nod){
		n = nod.n;
		for(int i=0; i<n; ++i) infos[i] = nod.infos[i];
		if(nod.tieneHijos())
			for (int i = 0; i <= n; ++i){
				hijos[i] = new (nothrow) Nodo(nod.hijos[i]->infos[0], this);
				if(!hijos[i]) throw NoHayMemoria();
				*(hijos[i]) = *(nod.hijos[i]);
			}
	}
	return *this;
}

template <class T, int g>
void ArbolBMas<T, g>::Nodo::vaciar(){
	if(tieneHijos())
		for (int i = 0; i <= n; ++i)
			hijos[i]->vaciar();
	delete this;

}

template <class T, int g>
bool ArbolBMas<T, g>::Nodo::insert(T a, Nodo *h){
	T aux;
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

template <class T, int g>
bool ArbolBMas<T, g>::Nodo::remove(T a){
	int i=0;
	for (; i < n; ++i)
		if(infos[i] == a) break;
	if(i == n) return false;

	for (int j = i; j < n-1; ++j)
		infos[j] = infos[j+1];
	--n;
	return true;
}

template <class T, int g>
void ArbolBMas<T, g>::Nodo::split(){
	int mitad = g/2;
	Nodo *der = new (nothrow) Nodo(infos[mitad], padre);
	if(!der) throw NoHayMemoria();
	n = mitad;

	if(tieneHijos()){
		der->n--;
		for(int i=mitad+1; i<g; ++i){
			der->infos[der->n] = infos[i];
			hijos[i]->padre = der;
			der->hijos[der->n++] = hijos[i];
		}
		hijos[g]->padre = der;
		der->hijos[der->n] = hijos[g];
	}else{
		for(int i=mitad+1; i<g; ++i)
			der->infos[der->n++] = infos[i];
		//lista enlazada b+
		der->hijos[0] = hijos[0];
		hijos[0] = der;
	}
	padre->insert(infos[mitad], der);
	

}

template <class T, int g>
void ArbolBMas<T, g>::Nodo::merge(){
	//buscar indice en padre
	Nodo *hermano, *aux = padre;

	int i=0;
	for(; padre->hijos[i] != this; ++i);
	if(i==0){
		hermano = padre->hijos[i+1];

		if(hermano->tieneHijos()){

			infos[n++] = padre->infos[i];
			for(int j=0; j<hermano->n; ++j){
				hijos[n] = hermano->hijos[j];
				hijos[n]->padre = this;
				infos[n++] = hermano->infos[j];
			}
			hijos[n] = hermano->hijos[hermano->n];
			hijos[n]->padre = this;
		}else{
			for(int j=0; j<hermano->n; ++j)
				infos[n++] = hermano->infos[j];
			hijos[0] = hermano->hijos[0];
		}
		delete hermano;
	}else{
		hermano = padre->hijos[i-1];

		if(hermano->tieneHijos()){

			hermano->infos[hermano->n++] = padre->infos[i-1];
			hermano->hijos[hermano->n] = hijos[0];
			hermano->hijos[hermano->n]->padre = hermano;
			for(int j=0; j<n; ++j){
				hermano->infos[hermano->n++] = infos[j];
				hermano->hijos[hermano->n] = hijos[j+1];
				hermano->hijos[hermano->n]->padre = hermano;
			}
		}else{
			for(int j=0; j<n; ++j)
				hermano->infos[hermano->n++] = infos[j];
			hermano->hijos[0] = hijos[0];
		}
		delete this;
		--i;
	}
	for(; i<aux->n-1; ++i){
		aux->infos[i] = aux->infos[i+1];
		aux->hijos[i+1] = aux->hijos[i+2];
	}
	--aux->n;
}

template <class T, int g>
bool ArbolBMas<T, g>::Nodo::pedirPrestado(){
	Nodo *hermano;
	int i=0;

	for(; padre->hijos[i] != this; ++i);
	if(i==0 || (i!=padre->n && padre->hijos[i+1]->n > padre->hijos[i-1]->n)){
		hermano = padre->hijos[i+1];

		if(hermano->n == (g-1)/2) return false;
		if(hermano->tieneHijos()){

			infos[n++] = padre->infos[i];
			hijos[n] = hermano->hijos[0];
			hijos[n]->padre = this;
			padre->infos[i] = hermano->infos[0];
			for(int j=0; j<hermano->n -1; ++j){
				hermano->infos[j] = hermano->infos[j+1];
				hermano->hijos[j] = hermano->hijos[j+1];
			}
			hermano->hijos[hermano->n-1] = hermano->hijos[hermano->n];
		}else{
			infos[n++] = hermano->infos[0];
			padre->infos[i] = hermano->infos[1];
			for(int j=0; j<hermano->n -1; ++j)
				hermano->infos[j] = hermano->infos[j+1];
		}
		--hermano->n;
	}else{
		hermano = padre->hijos[i-1];

		if(hermano->n == (g-1)/2) return false;
		if(hermano->tieneHijos()){

			hijos[n+1] = hijos[n];
			for(int j=n++; j>0; --j){
				infos[j] = infos[j-1];
				hijos[j] = hijos[j-1];
			}
			infos[0] = padre->infos[i-1];
			hijos[0] = hermano->hijos[hermano->n];
			hijos[0]->padre = this;
			padre->infos[i-1] = hermano->infos[--hermano->n];
		}else{
			for(int j=n++; j>0; --j)
				infos[j] = infos[j-1];
			infos[0] = hermano->infos[--hermano->n];
			padre->infos[i-1] = infos[0];
		}
	}
	return true;
}

template <class T, int g>
struct ArbolBMas<T, g>::Nodo* ArbolBMas<T, g>::Nodo::getSon(T valor){
	for(int i=0; i<n; ++i)
		if(valor < infos[i]) return hijos[i];
	return hijos[n];
}

////////////////////////////////////////////////////CONSTRUCTORES

template <class T, int g>
ArbolBMas<T, g>::ArbolBMas():raiz(NULL), n(0){}

template <class T, int g>
ArbolBMas<T, g>::ArbolBMas(const ArbolBMas<T, g> &a):raiz(NULL), n(0){
	*this = a;
}

template <class T, int g>
ArbolBMas<T, g>& ArbolBMas<T, g>::operator=(const ArbolBMas<T, g> &a){
	if(this!=&a){
        vaciar();
		if(!a.raiz) return *this;
		n = a.n;
		raiz = new (nothrow) Nodo(a.raiz->infos[0], NULL);
		if(!raiz) throw NoHayMemoria();
		*raiz = *(a.raiz);
		//enlazado
		if(!raiz->tieneHijos()) 
			return *this;

		Cola<Nodo*> hojas;
		Nodo *aux = raiz->hijos[0];
		int i=0;
		while(aux != raiz || i<=raiz->n){
			if(aux->tieneHijos()){
				if(i<=aux->n){
				 	aux=aux->hijos[i];
				 	if(aux->tieneHijos()) i=0;
				}else {
					for(i=0; aux->padre->hijos[i]!=aux; ++i);
					aux = aux->padre;
					++i;
				}
			}else{
				hojas.encolar(aux);
				aux = aux->padre;
				++i;
			}
		}
		aux = hojas.sacar();
		while(!hojas.estaVacia()){
			aux->hijos[0] = hojas.get();
			aux = hojas.sacar();
		}

	}
	return *this;
}

template <class T, int g>
ArbolBMas<T, g>::~ArbolBMas<T, g>(){
	vaciar();
}

//////////////////////////////////////////////////FUNCIONES MIEMBRO

template <class T, int g>
bool ArbolBMas<T, g>::insert(T valor){
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

template <class T, int g>
bool ArbolBMas<T, g>::remove(T valor){
		Nodo *aux = raiz;
		while(aux)
			if(aux->tieneHijos()) aux = aux->getSon(valor);
			else if(aux->remove(valor)){
				rebalancear(aux);
				--n;
				return true;
			}else return false;
}

template <class T, int g>
bool ArbolBMas<T, g>::find(T valor){
    Nodo *actual = raiz;
    while(actual->tieneHijos())
        actual = actual->getSon(valor);
    for(int i=0; i<actual->n; ++i)
            if(actual->infos[i] == valor) return true;
    return false;
}

template <class T, int g>
void ArbolBMas<T, g>::vaciar(){
	if(!raiz) return;
	raiz->vaciar();
	raiz = NULL;
	n = 0;
}

template <class T, int g>
int ArbolBMas<T, g>::getSize(){
	return n;
}

template <class T, int g>
void ArbolBMas<T, g>::rebalancear(Nodo *r){
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
				raiz = r->hijos[0];
				delete r;
			}
			return;
		}
		if(!r->pedirPrestado()){
			Nodo *papi = r->padre;
			r->merge();
			rebalancear(papi);
		}
	}
}

template <class T, int g>
void ArbolBMas<T, g>::imprimir(int tipo){
	if(!raiz){
		cout<<"ARBOL VACIO"<<endl;
		return;
	}
	Nodo *aux = raiz;
	switch(tipo){
	case 0:
		imprimir(raiz, 0);
		break;
	case 1:
		imprimirAsc();
		break;
	case 2:
		cout<<"[";
		while(aux->tieneHijos()) aux = aux->hijos[0];
		imprimirDesc(aux);
		cout<<"\b]"<<endl;
		break;
	case 3:
		imprimirNiveles();
		break;
    default:
        cout<<"No existe la opcion"<<endl;
	}
}

template <class T, int g>
void ArbolBMas<T, g>::imprimir(Nodo *r, int lvl){
	if(r->tieneHijos()){
		cout<<endl;
		for(int i=0; i<lvl; ++i) cout<<"  ";
	}
	cout<<"[";
	for(int i=0; i<r->n; ++i)
		cout<<r->infos[i]<<",";
	cout<<"\b]";
	if(r->tieneHijos()){
		cout<<" -> ";
		for(int i=0; i<=r->n; ++i){
			imprimir(r->hijos[i], lvl+1);
			if(!r->hijos[i]->tieneHijos()) cout<<"->";
		}
		cout<<endl;
	}
}

template <class T, int g>
void ArbolBMas<T, g>::imprimirAsc(){
	Nodo *aux = raiz;
	cout<<"[";
	while(aux->tieneHijos()) aux = aux->hijos[0];
	while(aux){
		for(int i=0; i<aux->n; ++i)
			cout<<aux->infos[i]<<",";
		aux = aux->hijos[0];
	}
	cout<<"\b]"<<endl;
}

template <class T, int g>
void ArbolBMas<T, g>::imprimirDesc(Nodo *r){
	if(r->hijos[0] != NULL) imprimirDesc(r->hijos[0]);
	for(int i=r->n-1; i>=0; --i) cout<<r->infos[i]<<",";
}

template <class T, int g>
void ArbolBMas<T, g>::imprimirNiveles(){
	Cola<Nodo*> nodos;
	int enEsteNivel = 0, saltarLineaEn = 1, conteo = 0;
	nodos.encolar(raiz);
	Nodo *actual;
	while(!nodos.estaVacia()){
		actual = nodos.sacar();
		cout<<"|";
		for(int i=0; i<actual->n; ++i)
			cout<<actual->infos[i]<<",";
		cout<<"\b|,";

		if(actual->tieneHijos()) {
			enEsteNivel+= actual->n+1;
			for(int i=0; i<=actual->n; ++i)
				nodos.encolar(actual->hijos[i]);
		}
		if(++conteo == saltarLineaEn){
			cout<<"\b "<<endl<<endl;
			saltarLineaEn = enEsteNivel;
			conteo = enEsteNivel = 0;
		}
	}
	cout<<endl;
}

///**********************************MEIN****************************/////

template <class, int>
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
			menu<int,3>();
			break;
		case 4:
			menu<int,4>();
			break;
		case 5:
			menu<int,5>();
			break;
		case 6:
			menu<int,6>();
			break;
		case 7:
			menu<int,7>();
	}

	system("PAUSE");
	return 0;
}

template <class T, int g>
void menu(){

	int op, op2;
	T in;
	ArbolBMas<T, g> a, b, c, *select = &a;

	for(int i=0; i<24; ++i) a.insert(i);

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
			cin>>op2;
			select->imprimir(op2);
			break;
        case 5:
            cout<<"select: Arbol ";
            cin>>op2;
            if(op2>=1 && op2<=3) select = op2 == 1 ? &a : op2 == 2 ? &b : &c;
            break;
        case 6:
            cout<<"copiar en: Arbol ";
            cin>>op2;
            if(op2>=1 && op2<=3)
                (op2 == 1 ? a : op2 == 2 ? b : c) = *select;
            break;
        case 7:
            select->vaciar();
		}
	}while(op!=0);
}

