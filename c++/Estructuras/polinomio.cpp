/**
	Autor: Montaño Valdez L. Roberto
	Fecha: 25/mar/2015
*/

#include <iostream>
#include <cstdlib>
#include <cmath>

using namespace std;

class NoHayMemoria : public exception{
public: const char* what() const throw() { return "No hay memoria disponible."; }
};

class DivisionEntreCero : public exception{
public: const char* what() const throw() { return "Error: División entre Cero."; }
};

class MonomiosInoperables : public exception{
public: const char* what() const throw() { return "Error: Los monomios son inoperables."; }
};

///********************RACIONAL***********************************///

class Racional{
    friend ostream& operator<<(ostream &, const Racional&);
    friend istream& operator>>(istream &, Racional&);
public:
    Racional(long int=0, long int=1);

    Racional operator+(const Racional&) const;
    Racional operator-(const Racional&) const;
    Racional operator*(const Racional&) const;
    Racional operator/(const Racional&) const;

    Racional& operator*=(const Racional &r) { *this = *this * r; return *this;}
    Racional operator-() const { return Racional(-numerador, denominador);}

    bool operator>(const Racional&) const;
    bool operator<(const Racional&) const;
    bool operator==(const Racional&) const;
    bool operator>=(const Racional&) const;
    bool operator<=(const Racional&) const;
    bool operator!=(const Racional&) const;
    operator bool() const;
    operator float() const {return (float)numerador/denominador; }

    const static Racional Cero;
    const static Racional Uno;
private:
    void simplificar();
    void denPositivo();
    long int numerador, denominador;
};

const Racional Racional::Cero = Racional(0);
const Racional Racional::Uno = Racional(1);

///****************************Utileria**********************************///

void Racional::simplificar(){
    if(numerador==0){
        denominador = 1;
        return;
    }
    long int menor = numerador<denominador ? numerador : denominador;
    if(menor<0) menor*=-1;
    for(int i=2; i<=menor; ++i ){
        if(numerador%i==0 && denominador%i==0){
            numerador/=i;
            denominador/=i;
            --i;
        }
    }
}

void Racional::denPositivo(){
    if(denominador<0){
        denominador*=-1;
        numerador*=-1;
    }
}

///****************************Constructor*****************************///

Racional::Racional(long int num, long int den):numerador(num), denominador(den){
    if(denominador==0) throw DivisionEntreCero();
    denPositivo();
    simplificar();
}

///*******************************Operadores aritmeticos*********************************///

Racional Racional::operator+(const Racional &a) const{
    Racional r(numerador*a.denominador + a.numerador*denominador, denominador*a.denominador);
    return r;
}

Racional Racional::operator-(const Racional &a) const{
    Racional r(numerador*a.denominador - a.numerador*denominador, denominador*a.denominador);
    return r;
}

Racional Racional::operator*(const Racional &a) const{
    Racional r(numerador*a.numerador, denominador*a.denominador);
    return r;
}

Racional Racional::operator/(const Racional &a) const{
    Racional r(numerador*a.denominador, denominador*a.numerador);
    return r;
}

///*******************************Operadores relacionales***********************///

bool Racional::operator>(const Racional &a) const {
    long int x=numerador*a.denominador, y = a.numerador*denominador;
    return x>y;
}

bool Racional::operator<(const Racional &a) const{
    return a > *this;
}

bool Racional::operator>=(const Racional &a) const{
    return !(a > *this);
}

bool Racional::operator<=(const Racional &a) const{
    return !(*this > a);
}

bool Racional::operator==(const Racional &a) const{
    long int x = numerador*a.denominador, y = a.numerador*denominador;
    return x==y;
}

bool Racional::operator!=(const Racional &a) const{
    return !(*this == a);
}

Racional::operator bool() const{
    return (bool)numerador;
}

///****************************Impresion y captura********************************///
ostream& operator<<(ostream &rout, const Racional &r){
    rout<<r.numerador;
    if(r.denominador!=1) cout<<"/"<<r.denominador;
    return rout;
}

istream& operator>>(istream &rin, Racional &r){
    cout<<"Numerador: ";
    cin>>r.numerador;
    do{
        cout<<"Denominador [distinto de 0]: ";
        cin>>r.denominador;
        if(r.denominador==0) cout<<"Denominador invalido, vuelva a capturar. \n";
    }while(r.denominador==0);
    r.denPositivo();
    r.simplificar();
}

///***************Impresion de ListaOrdenada********************************///

template <class T, bool (*C)(T,T)> class ListaOrdenada;

template <class T, bool (*C)(T,T)>
ostream& operator<<(ostream &lout, const ListaOrdenada<T, C> &l){
	if(!l.primero){
		lout<<"[]";
		return lout;
	}
	lout<<'[';
	struct ListaOrdenada<T, C>::Elemento *aux = l.primero;
	while(aux) {
	    cout<<aux->info<<',';
	    aux = aux->sig;
	}
	lout<<"\b]";
	return lout;
}

///*******************Criterios de busqueda y ordenamiento***********************///

template <class T>
bool ascendente(T a, T b){ return a > b; }

template <class T>
bool descendente(T a, T b){ return a < b; }

template <class T>
bool distintoDe(T a, T b){ return a != b; }

///***********************LISTA ORDENADA**********************///

template <class T = int, bool (*C)(T,T) = ascendente >
class ListaOrdenada{
	friend ostream& operator<< <>(ostream&, const ListaOrdenada<T, C>&);
    friend class Iterador;
public:

	ListaOrdenada(bool ascend=true):n(0), primero(NULL) {}
	ListaOrdenada(const ListaOrdenada<T,C> &l):primero(NULL){ *this = l; }
	ListaOrdenada<T,C>& operator=(const ListaOrdenada<T,C>&);
	~ListaOrdenada(){ empty(); }

	void insert(T);
	bool remove(T);
	bool find(T) const;
	bool estaVacia() const {return primero==NULL;}
	void empty();

	int getSize() const { return n; }

    class Iterador{
    public:
        Iterador(struct ListaOrdenada::Elemento *primero=NULL):sel(primero){}

        Iterador& operator++() { sel = sel->sig; return *this; }
        T get() { return sel->info; }
        void begin(const ListaOrdenada<T, C> &l) { sel = l.primero; }
        bool hasNext() { return sel->sig != NULL; }
        operator bool() { return sel!=NULL; }
    private:
        struct ListaOrdenada::Elemento *sel;
    };

    Iterador getIterador() const;
private:
	struct Elemento{
		Elemento(T i, Elemento *s = NULL):info(i), sig(s) {}
		T info;
		Elemento *sig;
	} *primero;
	Elemento* find(T, bool (*)(T,T)) const;
	int n;
};

///********************************Funciones miembro**********************************///

template<class T, bool(*C)(T,T)>
ListaOrdenada<T,C>& ListaOrdenada<T,C>::operator=(const ListaOrdenada<T,C> &l){
    if(this!=&l){
        this->empty();
        if(!l.estaVacia()){
            this->primero = new (nothrow) Elemento(l.primero->info);
            if(!this->primero) throw NoHayMemoria();
            ++this->n;
            Elemento *auxthat = l.primero->sig, *auxthis = this->primero;
            while(auxthat){
                auxthis->sig = new (nothrow) Elemento(auxthat->info);
                if(!auxthis->sig) throw NoHayMemoria();
                auxthat = auxthat->sig;
                auxthis = auxthis->sig;
                ++this->n;
            }
        }
    }
    return *this;
}

template<class T, bool (*C)(T,T)>
struct ListaOrdenada<T, C>::Elemento* ListaOrdenada<T, C>::find(T info, bool (*criterio)(T,T)) const{
    Elemento *aux = primero, *ant = NULL;
    while(aux && criterio(info,aux->info)){
        ant = aux;
        aux = aux->sig;
    }
    return ant;
}

template<class T, bool (*C)(T,T)>
void ListaOrdenada<T, C>::insert(T valor){
	Elemento *ant =find(valor, C), *aux = ant? ant->sig : primero;
	Elemento *nuevo = new (nothrow) Elemento(valor, aux);
	if(!nuevo) throw NoHayMemoria();
	if(ant) ant->sig = nuevo;
	else primero = nuevo;
	++n;
}

template <class T, bool (*C)(T,T)>
bool ListaOrdenada<T, C>::remove(T valor){
    if(estaVacia()) return false;
	Elemento *ant = find(valor, distintoDe), *aux = NULL;
	if(!ant){
        aux = primero->sig;
        delete primero;
        primero = aux;
        --n;
        return true;
	}
	if(!ant->sig) return false;
	aux = ant->sig;
	ant->sig = aux->sig;
	delete aux;
	--n;
	return true;

}

template <class T, bool (*C)(T,T)>
bool ListaOrdenada<T, C>::find(T valor) const {
    if(estaVacia()) return false;
	Elemento *ant = find(valor, distintoDe);
	return !ant || ant->sig;
}

template <class T, bool (*C)(T,T)>
void ListaOrdenada<T, C>::empty(){
    Elemento *aux;
    while(primero) {
        aux = primero;
        primero = primero->sig;
        delete aux;
    }
    n=0;
}

template <class T, bool (*C)(T,T)>
class ListaOrdenada<T,C>::Iterador ListaOrdenada<T,C>::getIterador() const{
    return Iterador(primero);
}

///**********************MONOMIO***************************///

class Polinomio;
//class Monomio;
//ostream& operator<<(ostream&, const Monomio&);

class Monomio{
    friend ostream& operator<<(ostream&, const Monomio&);
    friend ostream& operator<<(ostream&, const Polinomio&);
    friend class Polinomio;
public:
    Monomio(Racional r=1, int e=0):coeficiente(r), exp(e) {
        if(coeficiente==Racional(0)) exp=0;
    }

    bool operator>(const Monomio&) const;
    bool operator<(const Monomio&) const;
    bool operator>=(const Monomio&) const;
    bool operator<=(const Monomio&) const;
    bool operator==(const Monomio&) const;
    bool operator!=(const Monomio&) const;

    Monomio operator+(const Monomio&) const;
    Monomio operator-(const Monomio&) const;
    Monomio operator*(const Monomio&) const;
    Monomio operator/(const Monomio&) const;

    Monomio& operator +=(const Monomio&);
    Monomio& operator -=(const Monomio&);
    Monomio& operator *=(const Monomio&);
    Monomio& operator /=(const Monomio&);

    Monomio operator -() const {return Monomio(coeficiente*Racional(-1), this->exp);}
    float operator()(float);
    operator bool() const { coeficiente!=Racional::Cero; }

    const static Monomio Cero;
private:
    Racional coeficiente;
    int exp;
};

const Monomio Monomio::Cero = Monomio(0, 0);

ostream& operator<<(ostream &mout, const Monomio &m){
    if(m.coeficiente!=Racional::Uno && m.coeficiente!=-Racional::Uno || m.exp==0) mout<<m.coeficiente;
    else if(m.coeficiente==-Racional::Uno) mout<<'-';
    if(m.exp!=0){
        mout<<"x";
        if(m.exp!=1) mout<<'^'<<m.exp;
    }
    return mout;
}

///**************************Operadores relacionales***********************///

bool Monomio::operator>(const Monomio &m) const {
    return exp > m.exp;
}

bool Monomio::operator<(const Monomio &m) const {
    return m > *this;
}

bool Monomio::operator>=(const Monomio &m) const {
    return !(m > *this);
}

bool Monomio::operator<=(const Monomio &m) const {
    return !(*this > m);
}

bool Monomio::operator==(const Monomio &m) const {
    return this->exp == m.exp; // && this->coeficiente == m.coeficiente;
}

bool Monomio::operator!=(const Monomio &m) const{
    return !(*this == m);
}

///*********************Operadores aritmeticos***************************///

Monomio Monomio::operator+(const Monomio &m) const {
    if(exp!=m.exp) throw MonomiosInoperables();
    return Monomio(coeficiente+m.coeficiente, exp);
}

Monomio Monomio::operator-(const Monomio &m) const {
    if(exp!=m.exp) throw MonomiosInoperables();
    return Monomio(coeficiente-m.coeficiente, exp);
}

Monomio Monomio::operator *(const Monomio &m) const {
    return Monomio(coeficiente*m.coeficiente, exp+m.exp);
}

Monomio Monomio::operator/(const Monomio &m) const {
    return Monomio(coeficiente/m.coeficiente, exp-m.exp);
}

///**********************Operadores de asignacion***************************///

Monomio& Monomio::operator+=(const Monomio &m){
    return *this = *this + m;
}

Monomio& Monomio::operator-=(const Monomio &m){
    return *this = *this - m;
}

Monomio& Monomio::operator*=(const Monomio &m){
    return *this = *this * m;
}

Monomio& Monomio::operator/=(const Monomio &m){
    return *this = *this / m;
}

///*******************Evaluar***********************///

float Monomio::operator()(float x){
    float f = coeficiente;
    f *= pow(x, this->exp);
    return f;
}

///****************************POLINOMIO****************************///
//////////////////////////////////////////////////////////////////////

ostream& operator<<(ostream&, const Polinomio&);

class Polinomio{
    friend ostream& operator<<(ostream&, const Polinomio&);
public:
    Polinomio(){}
    //Polinomio(const char*);
    Polinomio(const Monomio &m){ if(m) lista.insert(m); }
    Polinomio(int a){ lista.insert(Monomio(1, 0)); }
    Polinomio(const Polinomio&);
    Polinomio& operator=(const Polinomio&);
    ~Polinomio();

    Polinomio& operator+=(const Polinomio&);
    Polinomio& operator*=(const Polinomio&);

    Polinomio operator+(const Polinomio&) const;
    Polinomio operator*(const Polinomio&) const;

    float operator()(float);
private:
    ListaOrdenada<Monomio, descendente> lista;
};

///******************Constructor de copias, operator=, destructor***********************///

Polinomio::Polinomio(const Polinomio &p){
    *this = p;
}

Polinomio& Polinomio::operator=(const Polinomio &p){
    if(this!=&p){
        lista = p.lista;
    }
    return *this;
}

Polinomio::~Polinomio(){
    lista.empty();
}

///***********************Operadores de asignacion***************************///

Polinomio& Polinomio::operator+=(const Polinomio &p){
    class ListaOrdenada<Monomio, descendente>::Iterador iter = p.lista.getIterador(), aux;
    Monomio insertar;
    while(iter){
        insertar = iter.get();
        if(lista.find(insertar)){
            aux = lista.getIterador();
            while(aux && aux.get()>insertar) ++aux;
            Monomio guardar = aux.get();
            lista.remove(guardar);
            insertar += guardar;
        }
        if(insertar.coeficiente!=Racional::Cero)
        lista.insert(insertar);
        ++iter;
    }
    return *this;
}

Polinomio& Polinomio::operator *=(const Polinomio &p){
    *this = (*this) * p;
}

///*************************Aritmetica***************************///

Polinomio Polinomio::operator+(const Polinomio &p) const {
    Polinomio poli = *this;
    poli += p;
    return poli;
}

Polinomio Polinomio::operator *(const Polinomio &p) const {
    Polinomio poli;
    class ListaOrdenada<Monomio, descendente>::Iterador iter = p.lista.getIterador(), aux = lista.getIterador();
    Monomio mono;
    while(iter){
        while(aux){
            mono = iter.get()*aux.get();
            poli+=mono;
            ++aux;
        }
        ++iter;
        aux.begin(lista);
    }
    return poli;
}

///***************************Evaluar****************************///

float Polinomio::operator()(float x){
    float f = 0;
    class ListaOrdenada<Monomio, descendente>::Iterador iter = lista.getIterador();
    Monomio mono;
    while(iter){
        mono = iter.get();
        f += mono(x);
        ++iter;
    }
    return f;
}

///*********************************IMPRESION***********************///

ostream& operator<<(ostream &pout, const Polinomio &p){
    if(p.lista.estaVacia()) return pout<<0;
    class ListaOrdenada<Monomio, descendente>::Iterador iter = p.lista.getIterador();
    pout<<iter.get();
    ++iter;
    while(iter){

        pout<<' ';
        if(iter.get().coeficiente>Racional::Cero) pout<<'+';
        pout<<iter.get();
        ++iter;
    }
    return pout;
}

///*****************************MEIN*********************************///
///////////////////////////////////////////////////////////////////////

Polinomio validarPolinomio(const char*);
bool esPolinomio(const char*);

int main(int argc, char const *argv[])
{

    char poli[1024];
    int op=-1; 
    cout<<"Se capturan dos polinomios y se suman o multiplican\n";
    do{
        cout<<"Formato: nx^n+n/nx^-n-x+n\n";
        cout<<"polinomio: ";
        cin>>poli;
        if(!esPolinomio(poli)){
            cout<<"El polinomio no es valido"<<endl;
            continue;
        }
        Polinomio p = validarPolinomio(poli);
        do{
            cout<<"\n1)Sumar\t2)Producto\t3)Imprimir\t0)Salir\n";
            cin>>op;
        }while(op<0 || op>3);
        if(op==0) continue;
        if(op==3){
            cout<<"Polinomio capturado: "<<p<<endl<<endl;
            continue;
        }
        cout<<"segundo polinomio: ";
        cin>>poli;
        if(!esPolinomio(poli)){
            cout<<"El polinomio no es valido"<<endl<<endl;
            continue;
        }
        
        if(op==1) cout<<"suma: "<< p + validarPolinomio(poli)<<endl<<endl;
        else if(op==2) cout<<"producto: "<<p * validarPolinomio(poli)<<endl<<endl;
    }while(op!=0);

    system("PAUSE");
	return 0;
}

bool esEntero(const char *cadena, int length){
    int i=0;
    if(cadena[0]=='-') ++i;
    if(i>=length) return false;
    for (; i < length; ++i)
        if(cadena[i]<48 || cadena[i]>57) return false;
    return true;
}

int validarEntero(const char *cadena, int length){
    int i=0;
    if(cadena[0]=='-') ++i;
    int numero = 0;
    for(; i<length; ++i){
        numero *= 10;
        numero += cadena[i] - 48;
    }
    return (cadena[0]=='-'?-1:1) * numero;
}

bool esRacional(const char *cadena, int length){
    int d=0;
    while(cadena[d]!='/' && d<length) ++d;
    if(d==length) return esEntero(cadena, d);
    return esEntero(cadena, d) && esEntero(cadena+d+1, length-d-1);
}

Racional validarRacional(const char *cadena, int length){
    int d=0;
    while(cadena[d]!='/' && d<length) ++d;
    if(d==length) return Racional(validarEntero(cadena, length));
    return Racional(validarEntero(cadena, d), validarEntero(cadena+d+1, length-d-1));
}

bool esMonomio(const char *cadena, int length){
    if(length==0) return false;
    if(cadena[0]=='-') return cadena[1]!='-'? esMonomio(cadena+1, length-1) : false;
    int x=0;
    while(cadena[x]!='x' && x<length) ++x;
    if(x>=length-1) return x==0 ? true : esRacional(cadena, x);
    if(cadena[x+1]!='^') return false;
    return (x==0 ? true : esRacional(cadena, x)) && esEntero(cadena+x+2, length-x-2);
}

Monomio validarMonomio(const char *cadena, int length){
    if(cadena[0]=='-') return validarMonomio(cadena+1, length-1)*Monomio(-1, 0);
    int x = 0;
    while(cadena[x]!='x' && x<length) ++x;
    if(x==0) return Monomio(1, x+1==length ? 1 : validarEntero(cadena+x+2, length-x-2));
    if(x==length) return Monomio(validarRacional(cadena, length), 0);
    if(x==length-1) return Monomio(validarRacional(cadena, x), 1);
    return Monomio(validarRacional(cadena, x), validarEntero(cadena+x+2, length-x-2));
}

bool esPolinomio(const char *cadena){
    int length=0, i=0, plength=0;
    while(cadena[length]!='\0') ++length;
    for(; i<length; ++i){
        if(esMonomio(cadena+plength, i+1-plength)){
            if(cadena[i+1]=='+' || cadena[i+1]=='-'){
                if(cadena[i+1]=='+') ++i;
                plength = i+1;
            }else if(i+1==length){
                return true;
            }
        }
    }
    return false;
}

Polinomio validarPolinomio(const char *cadena){
    Polinomio poli;
    int length=0, i=0, plength=0;
    while(cadena[length]!='\0') ++length;
    for(; i<length; ++i){
        if(esMonomio(cadena+plength, i+1-plength) &&
            (i+1==length || cadena[i+1]=='+' || cadena[i+1]=='-')
        ){
            poli+=validarMonomio(cadena+plength, i+1-plength);
            if(cadena[i+1]=='+') ++i;
            plength = i+1;
        }
    }
    return poli;
}