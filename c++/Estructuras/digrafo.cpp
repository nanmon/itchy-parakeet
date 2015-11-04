/*
  Autor: Roberto Montaño
  Fecha: 24/mar/2015
*/

#include <iostream>
#include <cstdlib>

using namespace std;

class Digrafo{
	friend ostream& operator<<(ostream&, const Digrafo&);
public:
	Digrafo():primero(NULL) {}

	bool insert(char);
	bool insert(char, char);
	bool remove(char);
	bool remove(char, char);

private:
	struct Nodo
	{
		Nodo(char i, Nodo *s = NULL):id(i),sig(s),saliente(NULL),entrante(NULL) {}

		bool insertIn(Nodo *n){
			return insert(entrante, n);
		}

		bool insertOut(Nodo *n){
			return insert(saliente, n);
		}

		bool removeIn(Nodo *n){
			return remove(entrante, n);
		}

		bool removeOut(Nodo *n){
			return remove(saliente, n);
		}

		void vaciar(){
			Arista *borrar;
			while(entrante){
				borrar = entrante;
				entrante = entrante->sig;
				borrar->extremo->removeOut(this);
				delete borrar;
			}while(saliente){
				borrar = saliente;
				saliente = saliente->sig;
				borrar->extremo->removeIn(this);
				delete borrar;
			}
		}

		void imprimir(){
			Arista *aux = entrante;
			while(aux){
				cout<<aux->extremo->id<<",";
				aux = aux->sig;
			}
			cout<<"\b <- "<<id<<" -> ";
			aux = saliente;
			while(aux){
				cout<<aux->extremo->id<<",";
				aux = aux->sig;
			}
			cout<<"\b ";
		}
		
		struct Arista
		{
			Arista(Nodo *e, Arista *s = NULL):extremo(e),sig(s){}
			Arista *sig;
			Nodo *extremo;
		} *saliente, *entrante;

		Nodo *sig;
		char id;

	private:

		Arista* find(Arista *inicio, Nodo *n){
			Arista *aux = entrante, *ant = NULL;
			while(aux && aux->extremo->id < n->id){
				ant = aux;
				aux = aux->sig;
			}
			return ant;
		}

		bool insert(Arista *&inicio, Nodo *n){
			Arista *ant = find(inicio, n);
			if(!ant){
				if(inicio && inicio->extremo == n) return false;
				inicio = new Arista(n, inicio);
			}else {
				if(ant->sig && ant->sig->extremo == n) return false;
				ant->sig = new Arista(n, ant->sig);
			}
			return true;
		}

		bool remove(Arista *&inicio, Nodo *n){
			Arista *ant = find(inicio, n), *aux = ant ? ant->sig : inicio;
			if(!aux || aux->extremo != n) return false;
			if(ant) ant->sig = aux->sig;
			else inicio = aux->sig;
			delete aux;
			return true;
		}

	} *primero;

	Nodo* find(char);
};

bool Digrafo::insert(char id){
	Nodo *ant = find(id);
	if(!ant){
		if(primero && primero->id == id) return false;
		primero = new Nodo(id, primero);
	}else{
		if(ant->sig && ant->sig->id == id) return false;
		ant->sig = new Nodo(id, ant->sig);
	}
	return true;
}

bool Digrafo::insert(char a, char b){
	if(a == b) return false;
	insert(a);
	insert(b);
	Nodo *anodo = find(a), *bnodo = find(b);
	anodo = anodo ? anodo->sig : primero;
	bnodo = bnodo ? bnodo->sig : primero;
	if(!anodo->insertOut(bnodo)) return false;
	bnodo->insertIn(anodo);
	return true;
}

bool Digrafo::remove(char id){
	Nodo *ant = find(id), *borrar;
	if(!ant){
		if(!primero || primero->id != id) return false;
		borrar = primero;
		primero = primero->sig;
	}else{
		if(!ant->sig || ant->sig->id != id) return false;
		borrar = ant->sig;
		ant->sig = borrar->sig;
	}
	borrar->vaciar();
	delete borrar;
	return true;
}

bool Digrafo::remove(char a, char b){
	if(a==b) return false;
	Nodo *anodo = find(a), *bnodo = find(b);
	anodo = anodo ? anodo->sig : primero;
	bnodo = bnodo ? bnodo->sig : primero;
	if(!anodo || anodo->id != a ||
	   !bnodo || bnodo->id != b ||
	   !anodo->removeOut(bnodo) ) return false;
	bnodo->removeIn(anodo);
	return true;
}

struct Digrafo::Nodo* Digrafo::find(char id){
	Nodo *aux = primero, *ant = NULL;
	while(aux && aux->id < id){
		ant = aux;
		aux = aux->sig;
	}
	return ant;
}

ostream& operator<<(ostream &dout, const Digrafo &d){
	if(!d.primero){
		dout<<"vacio"<<endl;
		return dout;
	}
	struct Digrafo::Nodo *aux = d.primero;
	while(aux){
		aux->imprimir();
		dout<<endl;
		aux = aux->sig;
	}
	return dout;
}

void insertar(Digrafo&, const char*);
void remover(Digrafo&, const char*);

int main(int argc, char const* argv[]){
	
	Digrafo g;
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

void insertar(Digrafo &g, const char *cadena){
	char a = cadena[0], b;
	if(cadena[1]=='\0' && !g.insert(a)) cout<<"El nodo ya existe"<<endl;
	else if(cadena[1]==','){
		b = cadena[2];
		if(a==b) cout<<"No permitido"<<endl;
		else if(!g.insert(a, b)) cout<<"La arista ya existe"<<endl;
	}

}

void remover(Digrafo &g, const char *cadena){
	char a=cadena[0], b;
	if(cadena[1]=='\0' && !g.remove(a)) cout<<"El nodo no existe"<<endl;
	else if(cadena[1]==','){
		b = cadena[2];
		if(!g.remove(a, b)) cout<<"La arista no existe"<<endl;
	}

}