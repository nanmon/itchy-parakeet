/*
	Autor: Nan 
	Fecha: 20/mar/2015
*/

#include <cstdlib>
#include <iostream>

using namespace std;

class Grafo{
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
private:

	int n;
	/////////////////////////////Estructura Nodo

	struct Nodo
	{
		Nodo(char i, Nodo *s = NULL):id(i),sig(s),primero(NULL) {}
		char id;
		Nodo *sig;

		bool insert(Nodo *id){
			Arista *ant = find(id, false), *aux = ant ? ant->sig : primero;
			if(aux && aux->extremo->id == id->id) return false;
			(ant ? ant->sig : primero) = new Arista(id, aux);
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

bool Grafo::insert(char id){
	Nodo *ant = find(id, false), *aux = ant ? ant->sig : primero;
	if(aux && aux->id == id) return false;
	(ant ? ant->sig : primero) = new Nodo(id, aux);
	++n;
	return true;
}

bool Grafo::insert(char a, char b){
	if(a==b) return false;
	insert(a);
	insert(b);
	Nodo *anodo = find(a,false), *bnodo = find(b,false);
	anodo = anodo ? anodo->sig : primero;
	bnodo = bnodo ? bnodo->sig : primero;
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

void insertar(Grafo&, const char*);
void remover(Grafo&, const char*);
void buscar(Grafo&, const char*);

int main(int argc, char const* argv[]){
	
	Grafo g, g2, g3, *select = &g;
	int op, sel;
	char in[100];

	cout<<"Instrucciones: \ninsertar/remover nodo: etiqueta[char]\ninsertar/remover arista: nodo[char],nodo[char]\n\n";

	do{
		cout<<"Grafo 1: "<<g.size()<<" nodos\n";
		cout<<"Grafo 2: "<<g2.size()<<" nodos\n";
		cout<<"Grafo 3: "<<g3.size()<<" nodos\n";
		cout<<"Seleccionado: Grafo "<<(select==&g?1:(select==&g2?2:3))<<endl<<endl;
		cout<<"1)Insertar\n2)Remover\n3)Seleccionar\n4)Imprimir\n5)Copiar\n6)Vaciar\n7)Buscar\n0)Salir\n";
		cin>>op;
		switch(op){
			case 1:
				cout<<"insert: ";
				cin>>in;
				insertar(*select, in);
				break;
			case 2:
				cout<<"remove: ";
				cin>>in;
				remover(*select, in);
				break;
			case 3:
				do{
					cout<<"select: Grafo ";
					cin>>sel;
				}while(sel<1 || sel>3);
				select = sel==1?&g:(sel==2?&g2:&g3);
				break;
			case 4:
				cout<<(*select);
				break;
			case 5:
				do{
					cout<<"copiar seleccionado en: Grafo ";
					cin>>sel;
				}while(sel<1 || sel>3);
				(sel==1?g:(sel==2?g2:g3)) = *select;
				break;
			case 6:
				select->empty();
				break;
			case 7:
				cout<<"buscar: ";
				cin>>in;
				buscar(*select, in);
		}
		cout<<endl;
	}while(op!=0);

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

/*
0111 3112 
1001 1221
1001 1221
1110 2113



*/