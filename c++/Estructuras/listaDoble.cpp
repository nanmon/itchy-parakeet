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

///***************Impresion de ListaDoble********************************///

template <class T> class ListaDoble;

template <class T>
ostream& operator<<(ostream &lout, const ListaDoble<T> &l){
	if(!l.primero){
		lout<<"[]";
		return lout;
	}
	lout<<'[';
	struct ListaDoble<T>::Elemento *aux = l.primero;
	while(aux) {
	    cout<<aux->info<<',';
	    aux = aux->sig;
	}
	lout<<"\b]";
	return lout;
}

///***********************LISTA DOBLE**********************///

template <class T = int>
class ListaDoble{
	friend ostream& operator<< <>(ostream&, const ListaDoble<T>&);
public:

	ListaDoble():n(0), primero(NULL), ultimo(NULL) {}
	ListaDoble(const ListaDoble<T> &l):primero(NULL), ultimo(NULL) { *this = l; }
	ListaDoble<T>& operator=(const ListaDoble<T>&);
	~ListaDoble(){ empty(); }

	void insert(T);
	bool remove(T);
	bool find(T) const;
	bool estaVacia() const { return primero==NULL; }
	void empty();
    void imprimirAsc();
    void imprimirDesc();

	int getSize() const { return n; }
private:
	struct Elemento{
		Elemento(T i, Elemento *s = NULL, Elemento *a = NULL):info(i), sig(s), ant(a) {}
		T info;
		Elemento *sig, *ant;
	} *primero, *ultimo;
	Elemento* find(T, bool) const;
	int n;
};

///********************************OPERATOR=**********************************///

template<class T>
ListaDoble<T>& ListaDoble<T>::operator=(const ListaDoble<T> &l){
    if(this!=&l){
        this->empty();
        if(!l.estaVacia()){
            this->primero = new (nothrow) Elemento(l.primero->info);
            if(!this->primero) throw NoHayMemoria();
            this->n = l.n;
            Elemento *auxthat = l.primero->sig, *auxthis = this->primero;
            while(auxthat){
                auxthis->sig = new Elemento(auxthat->info, NULL, auxthis);
                if(!auxthis->sig) throw NoHayMemoria();
                auxthat = auxthat->sig;
                auxthis = auxthis->sig;
            }
            ultimo = auxthis;
        }
    }
    return *this;
}

///***************************************Funciones miembro*******************///

template<class T>
struct ListaDoble<T>::Elemento* ListaDoble<T>::find(T info, bool exacto) const {
    Elemento *aux = primero;
    while(aux && info > aux->info){
        aux = aux->sig;
    }
    return !exacto || (aux && aux->info==info) ? aux : NULL;
}

template<class T>
void ListaDoble<T>::insert(T valor){
	Elemento *aux = find(valor, false);//aux=primero
	Elemento *nuevo = new(nothrow) Elemento(valor, aux, aux?aux->ant:ultimo);//valor, primero, NULL
	if(!nuevo) throw NoHayMemoria();
	++n;
	if(aux){//sip
        aux->ant ? aux->ant->sig = nuevo : primero = nuevo;//primero = nuevo
        aux->ant = nuevo;//exprimero->ant = nuevo
	}else{
        estaVacia() ? primero = nuevo : ultimo->sig = nuevo;
        ultimo = nuevo;
    }
}

template <class T>
bool ListaDoble<T>::remove(T valor){
	Elemento *aux = find(valor, true);
	if(!aux) return false;
    if(aux->ant) aux->ant->sig = aux->sig;
    if(aux->sig) aux->sig->ant = aux->ant;
    if(aux==primero) primero = aux->sig;
    if(aux==ultimo) ultimo = aux->ant;
    delete aux;
    --n;
	return true;
}

template <class T>
bool ListaDoble<T>::find(T valor) const {
	return find(valor, true)!=NULL;
}

template <class T>
void ListaDoble<T>::empty(){
    Elemento *aux;
    while(primero) {
        aux = primero;
        primero = primero->sig;
        delete aux;
    }
    ultimo = NULL;
    n=0;
}

template <class T>
void ListaDoble<T>::imprimirAsc(){
    if(estaVacia()){
        cout<<"[]";
        return;
    }
    cout<<"[";
    Elemento *aux = primero;
    while(aux){
        cout<<aux->info<<',';
        aux = aux->sig;
    }
    cout<<"\b]";
}

template <class T>
void ListaDoble<T>::imprimirDesc(){
    if(estaVacia()){
        cout<<"[]";
        return;
    }
    cout<<"[";
    Elemento *aux = ultimo;
    while(aux){
        cout<<aux->info<<',';
        aux = aux->ant;
    }
    cout<<"\b]";
}

///*****************************MEIN*********************************///

template <typename T> void menu(bool);

int main(int argc, char const *argv[])
{

    cout<<"Listas dobles de enteros"<<endl<<endl;
    menu<int>(true);
    cout<<"Listas dobles de caracteres"<<endl<<endl;
    menu<char>(true);

	return 0;
}

template <typename T>
void menu(bool continuar){
    int op;
    T insertar;
    int sel;
    ListaDoble<T> Lint, Lint2, Lint3, *seleccionada = &Lint;

    do{
        cout<<"Lista 1: "<<Lint<<", n: "<<Lint.getSize()<<endl;
        cout<<"Lista 2: "<<Lint2<<", n: "<<Lint2.getSize()<<endl;
        cout<<"Lista 3: "<<Lint3<<", n: "<<Lint3.getSize()<<endl<<endl;
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
            case 5:
                do{
                    cout<<"1)ascendente\t2)descendente\n";
                    cin>>sel;
                }while(sel<1 || sel>2);
                cout<<"seleccionada: ";
                sel==1 ? seleccionada->imprimirAsc() : seleccionada->imprimirDesc();
                cout<<endl;
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
        //if(op!=3 && op!=5) cout<<"Seleccionada: "<<(*seleccionada)<<", n: "<<seleccionada->getSize()<<endl<<endl;
    } while (op!=0);
}
