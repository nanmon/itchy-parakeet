/*
	Autor: Nan 
	Fecha: 20/mar/2015
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

class Arbol;

class Grafo{
	friend class Arbol;
	friend ostream& operator<<(ostream&, const Grafo&);
public:
	Grafo():primero(NULL),n(0) {}
	Grafo(const Grafo &g):primero(NULL){ *this = g; }
	~Grafo(){ empty(); }
	Grafo& operator=(const Grafo&);

	bool insert(char);
	bool insert(char, char);
	bool remove(char);
	bool remove(char, char);
	bool find(char);
	bool find(char, char);
	void empty();
	bool estaVacio() const{ return primero==NULL; }
	int size() const{ return n; }
protected:

	int n;
	/////////////////////////////Estructura Nodo

	struct Nodo
	{
		Nodo(char i, Nodo *s = NULL):id(i),sig(s),primero(NULL) {}
		char id;
		Nodo *sig;

		bool insert(Nodo *id){
			Arista *ant = find(id, false), *&aux = ant ? ant->sig : primero;
			if(aux && aux->extremo->id == id->id) return false;
			aux = new (nothrow) Arista(id, aux);
			if(aux == NULL) throw NoHayMemoria();
			return true;
		}
		bool remove(Nodo *id){
			Arista *ant = find(id, true), *borrar;
			if(!ant){
				if(!primero || primero->extremo->id != id->id) return false;
				borrar = primero;
				primero = primero->sig;
			}else{
				if(!ant->sig || ant->sig->extremo->id != id->id) return false;
				borrar = ant->sig;
				ant->sig = borrar->sig;
			}
			delete borrar;
			return true;
		}
		void empty(){
			Arista *borrar;
			while(primero){
				borrar = primero;
				primero = primero->sig;
				if(borrar->extremo->id!=this->id) borrar->extremo->remove(this);
				delete borrar;
			}
			primero = NULL;
		}
		void imprimir() const {
			Arista *aux = primero;
			while(aux){
				cout<<aux->extremo->id<<",";
				aux = aux->sig;
			}
			cout<<"\b ";
		}

		////////////////////Estructura Arista

		struct Arista
		{
			Arista(Nodo *e, Arista *s = NULL):extremo(e), sig(s) {}
			Arista *sig;
			Nodo *extremo;
		} *primero;

		Arista* find(Nodo *id, bool exacto){
			Arista *aux = primero, *ant = NULL;
			while(aux && aux->extremo->id < id->id){
				ant = aux;
				aux = aux->sig;
			}
			return !exacto || (aux && aux->extremo->id == id->id) ? ant : NULL;
		}

	} *primero;

	Nodo* insertar(char);
	Nodo* find(char, bool);

};

//////////////////////////operator=

Grafo& Grafo::operator=(const Grafo &g){
	if(this!=&g){
		empty();
		if(!g.estaVacio()){
			Nodo *naux = g.primero;
			Nodo::Arista *aaux;
			while(naux){
				insert(naux->id);
				aaux = naux->primero;
				while(aaux){
					insert(naux->id, aaux->extremo->id);
					aaux = aaux->sig;
				}
				naux = naux->sig;
			}
		}
	}
	return *this;
}

/////////////////////////////funciones miembro

Grafo::Nodo* Grafo::insertar(char id){
	Nodo *ant = find(id, false), *&aux = ant ? ant->sig : primero;
	if(aux && aux->id == id) return aux;
	aux = new (nothrow) Nodo(id, aux);
	if(aux == NULL) throw NoHayMemoria();
	++n;
	return aux;
}

bool Grafo::insert(char id){
	return insertar(id);
}

bool Grafo::insert(char a, char b){
	if(a==b) return false;
	Nodo *anodo = insertar(a), *bnodo = insertar(b);
	if(!anodo->insert(bnodo)) return false;
	bnodo->insert(anodo);
	return true;
}

bool Grafo::remove(char a){
	Nodo *ant = find(a, true), *aux;
	if(!ant){
		if(!primero || primero->id != a) return false;
		aux = primero;
		primero = primero->sig;
	}else{
		aux = ant->sig;
		ant->sig = aux->sig;
	}
	aux->empty();
	delete aux;
	return true;
}

bool Grafo::remove(char a, char b){
	if(!primero || a==b ) return false;
	Nodo *anodo = find(a, true), *bnodo = find(b, true);
	anodo = anodo ? anodo->sig : primero;
	bnodo = bnodo ? bnodo->sig : primero;
	if(anodo->id != a || bnodo->id != b || !anodo->remove(bnodo)) return false;
	--n;
	bnodo->remove(anodo);
}

bool Grafo::find(char id){
	Nodo *ant = find(id, true);
	return ant || (primero && primero->id == id);
}

bool Grafo::find(char a, char b){
	if(estaVacio() || a==b ) return false;
	Nodo *anodo = find(a, true), *bnodo = find(b, true);
	anodo = anodo ? anodo->sig : primero;
	bnodo = bnodo ? bnodo->sig : primero;
	if(anodo->id != a || bnodo->id != b) return false;
	Nodo::Arista *ari = anodo->find(bnodo, true);
	return ari || (anodo->primero && anodo->primero->extremo == bnodo);
}

struct Grafo::Nodo* Grafo::find(char id, bool exacto){
	Nodo *aux = primero, *ant = NULL;
	while(aux && aux->id < id){
		ant = aux;
		aux = aux->sig;
	}
	return !exacto || (aux && aux->id == id)? ant : NULL;
}

void Grafo::empty(){
	Nodo *aux;
	while(primero){
		aux = primero;
		primero = primero->sig;
		aux->empty();
		delete aux;
	}
	n = 0;
}

ostream& operator<<(ostream &gout, const Grafo &g){
	if(!g.primero){
		gout<<"Grafo vacio"<<endl;
		return gout;
	}
	struct Grafo::Nodo *aux = g.primero;
	while(aux){
		gout<<aux->id<<" -> ";
		aux->imprimir();
		aux = aux->sig;
		gout<<endl;
	}
	return gout;
}

///*****************************ARBOL************************///

class Arbol : public Grafo {
public:
	Arbol():Grafo(){}
	bool expansionAncho(const Grafo&);
};

bool Arbol::expansionAncho(const Grafo &g){
	Cola<Nodo*> cola;
	insertar(g.primero->id);
	Nodo *naux;
	Nodo::Arista *aaux;
	cola.encolar(g.primero);
	while(this->n < g.n && !cola.estaVacia()){
		naux = cola.sacar();
		aaux = naux->primero;
		while(aaux){
			if(!find(aaux->extremo->id)){
				cola.encolar(aaux->extremo);
				this->insert(naux->id, aaux->extremo->id);
			}
			aaux = aaux->sig;
		}
	}
	return this->n == g.n;
}

/////////////////////////////////////mein

void insertar(Grafo&, const char*);
void remover(Grafo&, const char*);
void buscar(Grafo&, const char*);

int main(int argc, char const* argv[]){
	
	Grafo g;
	int op;
	char in[100];

	cout<<"Instrucciones: \ninsertar/remover nodo: etiqueta[char]\ninsertar/remover arista: nodo[char],nodo[char]\n\n";

	do{
		cout<<"Grafo 1: "<<g.size()<<" nodos\n";
		cout<<"1)Insertar\n2)Remover\n3)Imprimir\n4)Vaciar\n5)Buscar\n0)Expansion a lo ancho\n";
		cin>>op;
		switch(op){
			case 1:
				cout<<"insert: ";
				cin>>in;
				insertar(g, in);
				break;
			case 2:
				cout<<"remove: ";
				cin>>in;
				remover(g, in);
				break;
			case 3:
				cout<<(g);
				break;
			case 4:
				g.empty();
				break;
			case 5:
				cout<<"buscar: ";
				cin>>in;
				buscar(g, in);
		}
		cout<<endl;
	}while(op!=0);

	Arbol a;
	if(a.expansionAncho(g)) cout<<a;
	else cout<<"No hay arbol de expansion."<<endl;

	system("PAUSE");
	return 24;
}

void insertar(Grafo &g, const char *cadena){
	char a = cadena[0], b;
	if(cadena[1]=='\0' && !g.insert(a)) cout<<"El nodo ya existe"<<endl;
	else if(cadena[1]==','){
		b = cadena[2];
		if(a==b) cout<<"No permitido"<<endl;
		else if(!g.insert(a, b)) cout<<"La arista ya existe"<<endl;
	}

}

void remover(Grafo &g, const char *cadena){
	char a=cadena[0], b;
	if(cadena[1]=='\0' && !g.remove(a)) cout<<"El nodo no existe"<<endl;
	else if(cadena[1]==','){
		b = cadena[2];
		if(!g.remove(a, b)) cout<<"La arista no existe"<<endl;
	}

}

void buscar(Grafo &g, const char *cadena){
	char a=cadena[0], b;
	if(cadena[1]=='\0') cout<<(g.find(a)?"Se encontro el nodo":"El nodo no existe")<<endl;
	else if(cadena[1]==','){
		b = cadena[2];
		cout<<(g.find(a,b)?"Se encontro la arista":"La arista no existe")<<endl;
	}
}