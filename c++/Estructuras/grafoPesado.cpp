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
	Grafo():primero(NULL) {} 

	bool insert(char);
	bool insert(char, char, int);
	bool remove(char);
	bool remove(char, char);
	void empty();
private:

	struct Nodo
	{
		Nodo(char i, Nodo *s = NULL):id(i),sig(s),primero(NULL) {}
		char id;
		Nodo *sig;

		bool insert(Nodo *id, int peso){
			Arista *ant = find(id, false), *aux = ant ? ant->sig : primero;
			if(aux && aux->extremo->id == id->id) return false;
			(ant ? ant->sig : primero) = new Arista(id, peso, aux);
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
			cout<<this->id<<" -> ";
			while(aux){
				cout<<'['<<aux->extremo->id<<","<<aux->peso<<"], ";
				aux = aux->sig;
			}
			cout<<"\b\b ";
		}

		struct Arista
		{
			Arista(Nodo *e, int p, Arista *s = NULL):extremo(e),peso(p),sig(s) {}
			Arista *sig;
			Nodo *extremo;
			int peso;
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

bool Grafo::insert(char id){
	Nodo *ant = find(id, false), *aux = ant ? ant->sig : primero;
	if(aux && aux->id == id) return false;
	(ant ? ant->sig : primero) = new Nodo(id, aux);
	return true;
}

bool Grafo::insert(char a, char b, int peso){
	if(a==b) return false;
	insert(a);
	insert(b);
	Nodo *anodo = find(a,false), *bnodo = find(b,false);
	anodo = anodo ? anodo->sig : primero;
	bnodo = bnodo ? bnodo->sig : primero;
	if(!anodo->insert(bnodo, peso)) return false;
	bnodo->insert(anodo, peso);
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
	bnodo->remove(anodo);
}

struct Grafo::Nodo* Grafo::find(char id, bool exacto){
	Nodo *aux = primero, *ant = NULL;
	while(aux && aux->id < id){
		ant = aux;
		aux = aux->sig;
	}
	return !exacto || (aux && aux->id == id)? ant : NULL;
}

ostream& operator<<(ostream &gout, const Grafo &g){
	if(!g.primero){
		gout<<"Grafo vacio"<<endl;
		return gout;
	}
	struct Grafo::Nodo *aux = g.primero;
	while(aux){
		aux->imprimir();
		aux = aux->sig;
		gout<<endl;
	}
	return gout;
}

void insertar(Grafo&, const char*);
void remover(Grafo&, const char*);

int main(int argc, char const* argv[]){
	
	Grafo g;
	int op;
	char in[100];
	do{
		cout<<"1)Insertar\t2)Remover\t0)Salir\n";
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
		}
		cout<<g;
	}while(op!=0);

	system("PAUSE");
	return 24;
}

void insertar(Grafo &g, const char *cadena){
	char a = cadena[0], b;
	if(cadena[1]=='\0' && !g.insert(a)) cout<<"El nodo ya existe"<<endl;
	else if(cadena[1]==','){
		int peso=0, n=4;
		b = cadena[2];
		if(a==b) cout<<"No permitido"<<endl;
		while(cadena[n]!='\0'){
			peso*=10;
			peso+=cadena[n++] - 48;
		}
		if(!g.insert(a, b, peso)) cout<<"La arista ya existe"<<endl;
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

/*
0111 3112 
1001 1221
1001 1221
1110 2113



*/