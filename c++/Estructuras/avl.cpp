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

///****************************ARBOL AVL***********************///
/////////////////////////////////////////////////////////////////////////////////
/*


doble a la izq
rsd; rsi;

doble a la der
rsi; rsd;
*/
class AVL{
public:
	AVL():raiz(NULL), n(0){}
	AVL(const AVL &a):raiz(NULL), n(0) { *this = a; }
	~AVL(){ vaciar(); }
	AVL& operator=(const AVL&);

	bool agregar(int);
	bool remover(int);
	bool buscar(int);
	bool estaVacio(){ return raiz==NULL; }
	void imprimir(int);
	void vaciar();
	int tamanio() { return n; }
private:
	int n;
	struct Nodo
	{
		Nodo(int i, Nodo *p):info(i), padre(p), hder(NULL), hizq(NULL), altura(1){}

		Nodo& operator=(const Nodo &n){
			if(this!=&n){
				this->info = n.info;
				Nodo *auxh = hizq, *auxd = hder;
				if(n.hizq){
					this->hizq = new (nothrow) Nodo(n.hizq->info, this);
					if(!hizq) throw NoHayMemoria();
					*(this->hizq) = *(n.hizq);
				} if(auxh) delete auxh;
				if(n.hder){
					this->hder = new (nothrow) Nodo(n.hder->info, this);
					if(!hder) throw NoHayMemoria();
					*(this->hder) = *(n.hder);
				} if(auxd) delete auxd;
			}
			return *this;
		}

		int info, altura;
		Nodo *padre, *hizq, *hder;
	} *raiz;

	void imprimirArbol(Nodo*, int, bool);
	void imprimirInorden(Nodo*);
	void imprimirInordenInv(Nodo*);
	void imprimirNiveles();
	void vaciar(Nodo*);
	Nodo* menorDeLosMayores(Nodo*);
	Nodo* mayorDeLosMenores(Nodo*);
	void actualizarAlturas(Nodo*);
	Nodo* rsi(Nodo*);
	Nodo* rsd(Nodo*);
};

//////////////////////////////////////operator=

AVL& AVL::operator=(const AVL &a){
	if(this!=&a){
		vaciar();
		n = a.n;
		raiz = new (nothrow) Nodo(a.raiz->info, NULL);
		if(!raiz) throw NoHayMemoria();
		*raiz = *(a.raiz);
	}
	return *this;
}

////////////////////////////////////////////FUnciones miembro

bool AVL::agregar(int valor){
	Nodo **r = &raiz, *padre = NULL;
	while(*r){
		padre = *r;
		if(valor < (*r)->info) r = &(*r)->hizq;
		else if(valor > (*r)->info) r = &(*r)->hder;
		else return false;
	}
	*r = new (nothrow) Nodo(valor, padre);
	if(!*r) throw NoHayMemoria();
	++n;
	actualizarAlturas((*r)->padre);
	return true;
}

bool AVL::remover(int valor){
	Nodo *r = raiz, *sw;
	while(r){
		if(valor < r->info)
			r = r->hizq;
		else if(valor > r->info)
		 	r = r->hder;
		else break;
	}
	if(!r) return false;
	--n;
	sw = mayorDeLosMenores(r);
	if(!sw) {
		sw = menorDeLosMayores(r);
		if(!sw){
			if(!r->padre) raiz = NULL;
			else (r->padre->hizq == r ? r->padre->hizq : r->padre->hder) = NULL;
			sw = r->padre;
			delete r;
			actualizarAlturas(sw);
			return true;
		}else if(sw != r->hder)
			sw->padre->hizq = sw->hder;
		else r->hder = NULL;
	}else if(sw != r->hizq) 
		sw->padre->hder = sw->hizq;
	else r->hizq = NULL;
	
	r->info = sw->info;
	r = sw->padre;
	delete sw;
	actualizarAlturas(r);
	return true;
}

bool AVL::buscar(int valor){
	Nodo *r = raiz;
	while(r){
		if(valor < r->info)
			r = r->hizq;
		else if(valor > r->info)
		 	r = r->hder;
		else break;
	}
	return r!=NULL;
}

AVL::Nodo* AVL::menorDeLosMayores(Nodo *r){
	Nodo *aux = r->hder;
	if(!aux) return NULL;
	while(aux->hizq)
		aux = aux->hizq;
	return aux;
}

AVL::Nodo* AVL::mayorDeLosMenores(Nodo *r){
	Nodo *aux = r->hizq;
	if(!aux) return NULL;
	while(aux->hder)
		aux = aux->hder;
	return aux;
}

void AVL::vaciar(){
	if(!raiz) return;
	vaciar(raiz);
	raiz = NULL;
	n = 0;
}

void AVL::vaciar(Nodo *r){
	if(r->hizq) vaciar(r->hizq);
	if(r->hder) vaciar(r->hder);
	delete r;
}

void AVL::actualizarAlturas(Nodo *actual){
	int equilibrio = 0, equiAnt =0;
	Nodo *aux = NULL;
	int faltante;
	while(actual){
		equiAnt = equilibrio;
		if(!actual->hder){
			if(!actual->hizq){
				actual->altura = 1;
				equilibrio = 0;
			}else{
				equilibrio = -actual->hizq->altura;
				actual->altura = -equilibrio +1;
			}
		}else if(!actual->hizq){
			equilibrio = actual->hder->altura;
			actual->altura = equilibrio +1;
		}else{
			actual->altura = (actual->hder->altura > actual->hizq->altura ? actual->hder : actual->hizq)->altura +1;
			equilibrio = actual->hder->altura - actual->hizq->altura;
		}
		if(equilibrio > 1){
			if(equiAnt != -1) actual = rsi(actual);
			else {
				rsd(actual->hder);
				actual = rsi(actual);
			}
		}else if(equilibrio < -1){
			if(equiAnt != 1) actual = rsd(actual);
			else{
				rsi(actual->hizq);
				actual = rsd(actual);
			}
		}
		actual = actual->padre;
	}
}

AVL::Nodo* AVL::rsi(Nodo *a){

	Nodo *c = a->hder, *d = c->hizq, *padre = a->padre;
	if(d) d->padre = a;
	a->hder = d;
	if(!d){
		if(!a->hizq) a->altura = 1;
		else a->altura = a->hizq->altura +1;
	}else if(!a->hizq)
		a->altura = a->hder->altura + 1;
	else a->altura = (a->hizq->altura > a->hder->altura ? a->hizq : a->hder)->altura + 1;
	a->padre = c;
	c->hizq = a;
	if(!c->hder)
		c->altura = a->altura + 1;
	else c->altura = (a->altura > c->hder->altura ? a : c->hder)->altura + 1;
	c->padre = padre;
	if(padre) (padre->hizq == a ? padre->hizq : padre->hder) = c;
	else raiz = c;

	return c;
}

AVL::Nodo* AVL::rsd(Nodo *a){
	Nodo *b = a->hizq, *e = b->hder, *padre = a->padre;

	if(e) e->padre = a;
	a->hizq = e;
	if(!e){
		if(!a->hder) a->altura = 1;
		else a->altura = a->hder->altura +1;
	}else if(!a->hder)
		a->altura = e->altura + 1;
	else a->altura = (a->hder->altura > e->altura ? a->hder : e)->altura + 1;
	
	a->padre = b;
	b->hder = a;
	if(!b->hizq)
		b->altura = a->altura + 1;
	else b->altura = (a->altura > b->hizq->altura ? a : b->hizq)->altura + 1;
	b->padre = padre;
	if(padre) (padre->hizq == a ? padre->hizq : padre->hder) = b;
	else raiz = b;

	return b;
}

void AVL::imprimirArbol(Nodo *r, int nivel, bool izq = true){
	if(r->hder) imprimirArbol(r->hder, nivel+1, false);
	if(nivel!=0){
		cout<<setw((nivel-1)*7)<<" "<<(izq?"^":"v")<<"----";
	}
	cout<<"["<<r->info<<","<<r->altura<<"]"<<endl;
	if(r->hizq) imprimirArbol(r->hizq, nivel+1, true);
}

void AVL::imprimirInorden(Nodo *r){
	if(r->hizq) imprimirInorden(r->hizq);
	cout<<r->info<<",";
	if(r->hder) imprimirInorden(r->hder);
}

void AVL::imprimirInordenInv(Nodo *r){
	if(r->hder) imprimirInordenInv(r->hder);
	cout<<r->info<<",";
	if(r->hizq) imprimirInordenInv(r->hizq);
}

void AVL::imprimirNiveles(){
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

void AVL::imprimir(int tipo = 1){
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

int main(int argc, char const *argv[])
{
	AVL a, b, c, *sel = &a;
	int op, in, select = 1;
	do{
		cout<<"Arbol 1: "<<a.tamanio()<<" nodos"<<endl;
		cout<<"Arbol 2: "<<b.tamanio()<<" nodos"<<endl;
		cout<<"Arbol 3: "<<c.tamanio()<<" nodos"<<endl;
		cout<<"\nSeleccionado: Arbol "<<select<<endl;
		cout<<"\n1)Seleccionar\n2)Agregar\n3)Buscar\n4)Remover\n5)Imprimir\n6)Copiar\n7)Vaciar\n8)Revisar si esta vacio\n0)Salir\n";
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
				cout<<"buscar: ";
				cin>>in;
				cout<<(sel->buscar(in)?"Si se encuentra":"No se encuentra")<<endl;
				break;
			case 4:
				cout<<"remover: ";
				cin>>in;
				cout<<(sel->remover(in)?"Nodo removido":"Nodo no existe")<<endl;
				break;
			case 5:
				do{
					cout<<"Imprimir:\n0)Arbol\t1)Ascendente\t2)Descendente\t3)Por Niveles\n";
					cin>>in;
				}while(in<0 && in>4);
				sel->imprimir(in);
				cout<<endl;
				break;
			case 6:
				do{
					cout<<"copiar seleccionado en: Arbol ";
					cin>>in;
				}while(in<1 && in>3);
				(in == 1 ? a : in == 2 ? b : c) = *sel;
				break;
			case 7:
				sel->vaciar();
			case 8:
				cout<<(sel->estaVacio()?"El arbol esta vacio":"El arbol no esta vacio")<<endl;
		}

	}while(op != 0);


	system("PAUSE");
	return 0;
}