/*
	Autor: Montaño Valdez L. Roberto
	Fecha: 21/mar/2015
*/

#include <cstdlib>
#include <iostream>
#include <cstring>

using namespace std;

template <class T> class ListaCircular;

////////////////////////////////Excepciones

class NoHayMemoria: public exception{
public:
    const char* what() const throw() { return "No hay memoria disponible"; }
};

class IndiceNoValido: public exception{
public:
    const char* what() const throw() { return "El indice no fue valido."; }
};

class ListaVacia : public exception {
public: const char* what() const throw() { return "Error: Intento de remover de lista vacio"; }
};

//////////////////////////////Clase Cadena


class Cadena{
   friend ostream& operator << (ostream &, const Cadena &);
   friend istream & operator >> (istream &, Cadena &);
public:

   Cadena(char* C =  NULL);
   Cadena(const Cadena &);
   ~Cadena(){delete [] cadena;}
   void operator +=(Cadena);
   Cadena(int);
   bool operator !() const {return !(cadena[0] == '\0');}
   bool operator == (Cadena) const;
   char & operator [](int );
   char operator[](int) const;
   bool operator!=(Cadena C) const{return !(*this == C);}
   Cadena& operator = (const Cadena &);
   int longitudCadena(){return tam;}
   Cadena subCadena(int = 0, int = 0);
   void convertirMinusculas();
   void convertirMayusculas();
private:
   char * cadena;
   int tam;

};
///////////////////////////////////////////////////Constructor normal
Cadena::Cadena(char* C):tam(0){
   if(C == NULL){
      cadena = new (nothrow) char[1];
      if(cadena == NULL) throw NoHayMemoria();
      cadena[0] = '\0';
   }
   else{
        for(int i = 0; C[i] != '\0';i++,tam++);
        cadena = new (nothrow) char[tam+1];
        if(cadena == NULL) throw NoHayMemoria();
        cadena[tam] = '\0';
        for (int i = 0; i < tam; i++) cadena[i] = C[i];
   }
}

//////////////////////////////////////////////////Constructor de copias
Cadena::Cadena(const Cadena &C){
   tam = -1;
   cadena = NULL;
   *this = C;
}
//////////////////////////////////////////////////////Sobrecarga del operador=
Cadena& Cadena::operator=(const Cadena &C){
   if (!(this==&C)){
      if (tam != C.tam){
         delete [] cadena;
         cadena = new (nothrow) char[C.tam+1];
         if(cadena == NULL) throw NoHayMemoria();
         cadena[C.tam] = '\0';
         tam = C.tam;
      }
      for(int i = 0; i < tam; i++) cadena[i] = C.cadena[i];
   }
   return *this;
}
///////////////////////////////////Sobrecarga del operador de comparacion
bool Cadena::operator==(Cadena C) const{
   if (this==&C) return true;
   if (tam != C.tam) return false;
   bool esIgual = true;
   for(int i = 0; i < tam; i++) if (cadena[i] != C.cadena[i]){
      esIgual = false;
      break;
   }
   return esIgual;
}
////////////////////////////////////////////////lvalue y rvalue
char & Cadena::operator [](int x){
    if (x < 0 || x >=tam) throw IndiceNoValido();
    return cadena[x];
}
char Cadena::operator[](int x) const{
    if (x < 0 || x >=tam) throw IndiceNoValido();
    return cadena[x];
}
//////////////////////////////////////////Convertir a mayusculas/minusculas
void Cadena::convertirMayusculas(){
   for(int i = 0; i < tam; i++){
      if ((int)cadena[i] >= 97 && (int)cadena[i] <= 122) cadena[i] = char((int)cadena[i] - 32);
   }
}
void Cadena::convertirMinusculas(){
   for(int i = 0; i < tam; i++){
      if ((int)cadena[i] >= 65 && (int)cadena[i] <= 90) cadena[i] = char((int)cadena[i] + 32);
   }
}
//////////////////////////////////////////////////Operadores cout y cin
ostream& operator << (ostream &out, const Cadena &C){
   for (int i = 0; i < C.tam ;i++) out << C.cadena[i];
   return out;
}

istream & operator >> (istream &in, Cadena &C){
      char* aux = new (nothrow) char[100];
      if(aux == NULL) throw NoHayMemoria();
      in.sync();
      in.getline(aux,100);
      C = aux;
      delete [] aux;
      return in;
}
///////////////////////////////////////////////////Subcadena
Cadena Cadena::subCadena(int indice, int length){

    if(indice  < 0 || indice >tam) {
        throw IndiceNoValido();
    }
   //caso de prueba: indice = 0, length = 6
   char * aux = new (nothrow) char[length+1];
   if(aux == NULL) throw NoHayMemoria();
   aux[length] = '\0';
   int i = 0;
   for(; i < length;i++) {
		if(indice + i >= tam) break;
      aux[i] = cadena[indice+i];
   }
   aux[i] = '\0';
   Cadena auxiliar(aux);
   delete [] aux;
   return auxiliar;
}
////////////////////////////////////////////////////Sobrecarga del operador +=
void Cadena::operator += (Cadena C){
   Cadena aux = *this;
   delete[]cadena;
   cadena = new (nothrow) char[aux.tam+C.tam + 1];
   if(cadena == NULL) throw NoHayMemoria();
   cadena[aux.tam+C.tam] = '\0';
   tam = aux.tam+C.tam;
   for(int i = 0; i < aux.tam; i++) cadena[i] = aux.cadena[i];
   for(int i = 0; i < C.tam; i++) cadena[aux.tam+i] = C.cadena[i];
}

///************************Persona************************************///

struct Persona{
	Persona(Cadena n, int l=-1):nombre(n), lugar(l){}
	const Cadena nombre;
	const int lugar;
};

ostream& operator<<(ostream &pout, const Persona &p){
	return pout<<p.nombre;
}

///****************************Sobrecarga para impresion (ListaCircular)************************///

template <class T>
ostream& operator<<(ostream &lout, const ListaCircular<T> &l){

	if(l.estaVacia())
		return lout<<"[]";
	lout<<'[';
	struct ListaCircular<T>::Elemento *aux = l.cabecera->sig;
	while(l.cabecera!=aux){
	    lout<<aux->info<<',';
	    aux = aux->sig;
	}
	return lout<<aux->info<<"]";
}

///***********************LISTA CIRCULAR SIN CABECERA FICTICIA**********************///


template <class T = int>
class ListaCircular{
	friend ostream& operator<< <>(ostream&, const ListaCircular<T>&);
public:

	ListaCircular<T>():n(0), cabecera(NULL){}
	ListaCircular<T>(const ListaCircular<T> &l):cabecera(NULL){ *this = l; }
	ListaCircular<T>& operator=(const ListaCircular<T>&);
	~ListaCircular<T>(){ empty(); }

	void insert(T);
	bool remove(T);
	T removeNext();
	bool find(T);
	bool estaVacia() const { return cabecera==NULL; }
	void empty();
	void next(){ if(!estaVacia()) cabecera = cabecera->sig; }
	T getActual() const;
	int size() const { return n; }
private:
	struct Elemento{
		Elemento(T i, Elemento *s = NULL):info(i), sig(s) {}
		T info;
		Elemento *sig;
	} *cabecera;
	int n;
};

///**********************Operador=************************///

template<class T>
ListaCircular<T>& ListaCircular<T>::operator=(const ListaCircular<T> &l){
    if(this!=&l){
        this->empty();
        if(!l.estaVacia()){
            this->insert(l.cabecera->info);
            Elemento *aux = l.cabecera->sig;
            while(aux!=l.cabecera){
                this->insert(aux->info);
                aux = aux->sig;
            }
        }
    }
    return *this;
}

///***************************Funciones miembro*************************///

template<class T>
void ListaCircular<T>::insert(T valor){
    Elemento *nuevo = new (nothrow) Elemento(valor);
    if(!nuevo) throw NoHayMemoria();
    estaVacia() ? cabecera = nuevo : nuevo->sig = cabecera->sig;
    cabecera->sig = nuevo;
    ++n;
}

template <class T>
bool ListaCircular<T>::remove(T valor){
    if(!find(valor)) return false;
    removeNext();
    return true;
}

template <class T>
T ListaCircular<T>::removeNext(){
	if(estaVacia()) throw ListaVacia();
	Elemento *borrar = cabecera->sig;
    if(cabecera->sig==cabecera) cabecera = NULL;
    else cabecera->sig = borrar->sig;
    T r = borrar->info;
    delete borrar;
    --n;
    return r;
}

template <class T>
bool ListaCircular<T>::find(T valor){
    if(estaVacia()) return false;
    Elemento *aux = cabecera;
    do 
        if(cabecera->sig->info!=valor)
            next();
        else return true;
    while(cabecera!=aux);
    return false;
}

template <class T>
T ListaCircular<T>::getActual() const {
	if(estaVacia()) throw ListaVacia();
	return cabecera->info;
}

template <class T>
void ListaCircular<T>::empty(){
    if(estaVacia()) return;
    Elemento *aux;
    while(cabecera->sig!=cabecera) {
        aux = cabecera->sig;
        cabecera->sig = aux->sig;
        delete aux;
    }
    delete cabecera; 	
    cabecera = NULL;
    n=0;
}

int main(int argc, const char* argv[]){

	ListaCircular<Persona> judios;
	int n, m;

	cout<<"Simulador de suicido colectivo c:"<<endl;
	cout<<"n personas forman un circulo y empiezan a matarse empezando de la primera persona y saltando m personas"<<endl;

	do{
		cout<<"n: ";
		cin>>n;
	}while(n<2);
	
	do{
		cout<<"m: ";
		cin>>m;
	}while(m<1);

	cin.ignore();

	Cadena k;

	for(int i=0; i<n; ++i){
		cout<<"Persona "<<i+1<<": Nombre = ";
		cin>>k;
		judios.insert(Persona(k, i));
		judios.next();
	}

	while(judios.size()>1){
		cout<<endl<<"Circulo: "<<judios<<endl;
		for(int i=1; i<m; ++i) judios.next();
		cout<<"Muere: "<<judios.removeNext()<<endl;
	}
	Persona macizo = judios.getActual();
	cout<<endl<<"Sobrevive: "<<macizo<<", posicion original: "<<macizo.lugar+1<<endl;

	system("PAUSE");
	return 0;
}