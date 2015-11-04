/**
	Nombre:
	Autor: L. Roberto Montaño Valdez
	Fecha:
	Descripción:
*/

#include<cstdlib>
#include<iostream>
#include<cmath>

using namespace std;

///*******************Excepciones***************************

class NoHayMemoria : public exception {
    const char* what() const throw() { return "No hay memoria disponible"; }
};

class PilaVacia : public exception {
    const char* what() const throw() { return "La pila esta vacia"; }
};

class NoEsNatural : public exception {
    virtual const char* what() const throw(){ return "La cadena no representa un numero natural."; }
};

class NoEsEntero : public NoEsNatural{
    virtual const char* what() const throw(){ return "La cadena no representa un numero entero."; }
};

class NoEsReal : public NoEsEntero {
    virtual const char* what() const throw(){ return "La cadena no representa un numero real."; }
};

class NoEsDouble : public NoEsReal {
    virtual const char* what() const throw(){ return "La cadena no representa un numero."; }
};

class DivisionEntreCero : public exception {
    const char* what() const throw() { return "Division entre cero"; }
};

class RaizCompleja : public exception {
    const char* what() const throw() { return "Raiz compleja"; }
};

class ExpresionInvalida : public exception {
    const char* what() const throw() { return "La expresion no es valida"; }
};

///***********************Pila************************///

template<class T> class Pila;
template<class T> ostream& operator<<(ostream &, const Pila<T>&);

template <class T>
class Pila{
    friend ostream& operator<< <>(ostream &, const Pila<T>&);
public:

    Pila();
    Pila(const Pila<T> &);
    Pila<T>& operator=(const Pila<T> &);
    ~Pila();

    void vaciar();
    void insertarElemento(T);
    T removerElemento();
    T getElemento();
    int getSize();
    bool estaVacia();
    void imprimir();

private:
    class Elemento;
    int n;
    Elemento *tope;
};

template<class T>
class Pila<T>::Elemento{
public:
    Elemento(T, Elemento * = NULL);
    T info;
    Elemento *sig;
};

template <class T>
Pila<T>::Elemento::Elemento(T i, Elemento *n):info(i), sig(n) {}

template <class T>
Pila<T>::Pila():tope(NULL), n(0){}

template <class T>
Pila<T>::Pila(const Pila<T> &p):tope(NULL), n(0){
    *this=p;
}

template <class T>
Pila<T>& Pila<T>::operator=(const Pila<T> &p){
    if(this!=&p){
        Elemento *e = tope, *c = p.tope, *aux = tope;
        if(tope==NULL){
            tope = new (nothrow) Elemento(c->info);
            if(tope==NULL) throw NoHayMemoria();
            aux = tope;
            c = c->sig;
        }
        while(e!=NULL && c!=NULL){
            e->info = c->info;
            aux = e;
            e = e->sig;
            c = c->sig;
        }
        if(e!=NULL){
            aux->sig = NULL;
            while(e!=NULL){
                aux = e;
                e = e->sig;
                delete aux;
            }

        }
        else {
            while(c!=NULL){
                e = new (nothrow) Elemento(c->info);
                if(e == NULL) throw NoHayMemoria();
                aux->sig = e;
                aux = e;
                c = c->sig;
            }

        }
    }
    return *this;
}

template <class T>
Pila<T>::~Pila(){ vaciar(); }

template <class T>
void Pila<T>::vaciar(){
    while(tope!=NULL) removerElemento();
}

template <class T>
void Pila<T>::insertarElemento(T i){
    Elemento *e = new (nothrow) Elemento(i, tope);
    if(e==NULL) throw NoHayMemoria();
    tope = e;
    ++n;
}

template <class T>
T Pila<T>::removerElemento(){
    if(estaVacia()) throw PilaVacia();
    --n;
    Elemento *aux = tope;
    T r = aux->info;
    tope = tope->sig;
    delete aux;
    return r;
}

template <class T>
T Pila<T>::getElemento() {
    if(estaVacia()) throw PilaVacia();
    return tope->info;
}

template <class T>
int Pila<T>::getSize(){ return n; }

template <class T>
bool Pila<T>::estaVacia(){
    return tope == NULL;
}

template <class T>
void Pila<T>::imprimir(){
    cout<<"-> ";
    Elemento *t = tope;
    while(t!=NULL){
        cout<<t->info<<",";
        t = t->sig;
    }
    cout<<"\b]"<<endl;
}

template <class T>
ostream& operator<<(ostream &pout, const Pila<T> &p){
    pout<<"-> ";
    class Pila<T>::Elemento *t;
    t= p.tope;
    while(t!=NULL){
        pout<<t->info<<",";
        t = t->sig;
    }
    pout<<"\b]"<<endl;
    return pout;
}

///**************Cadena a double**********************///

long double parseNatural(char n[]) throw(NoEsNatural){
    int length = 0;
    while(n[length]!='\0') ++length;
    int exp = length-1;
    long double numero = 0;
    int cifra;
    for(int i=0; i<length; ++i, --exp){
        cifra = n[i] - '0';
        if(cifra<0 || cifra>9) throw NoEsNatural();
        numero+=cifra*pow(10, exp);
    }

    return numero;
}

long double parseInt(char n[]) throw(NoEsEntero){
    try{
        return parseNatural(n);
    }catch(NoEsNatural ex){
        throw NoEsEntero();
    }
}

long double parseReal(char n[]) throw(NoEsReal){
    int punto = 0;
    while(n[punto]!='.' && n[punto]!='\0') ++punto;
    try{
        if(n[punto]=='\0') return parseInt(n);
    }catch(NoEsEntero ex){
        throw NoEsReal();
    }
    char *entero = n, *decimal = n+punto+1;
    entero[punto] = '\0';

    long double real=0;

    int cifra, exp=-1;
    for(int i=0; decimal[i]!='\0'; ++i, --exp){
        cifra = decimal[i] - '0';
        if(cifra<0 || cifra>9) throw NoEsReal();
        real+=cifra*pow(10, exp);
    }

    try{
        real += parseInt(entero);
    }catch(NoEsEntero ex){
        n[punto] = '.';
        throw NoEsReal();
    }
    n[punto] = '.';
    return real;
}

long double parseDouble(char n[]) throw(NoEsDouble){
    int e = 0;
    while(n[e]!='e' && n[e]!='\0') ++e;
    try{
        if(n[e]=='\0') return parseReal(n);
    }catch(NoEsReal ex){
        throw NoEsDouble();
    }
    char *entero = n, *exponente = n+e+1;
    entero[e] = '\0';

    long double real;
    try{
        real = parseReal(entero);
        real *= pow(10, parseInt(exponente+(exponente[0]=='+'?1:0)));
    }catch(NoEsEntero ex){
        n[e] = 'e';
        throw NoEsDouble();
    }
    n[e] = 'e';
    return real;
}

bool esNatural(char n[]){
    if(n[0]=='\0') return false;
    for(int i=0; n[i]!='\0'; ++i)
        if(n[i]<'0' || n[i]>'9') return false;

    return true;
}

bool esEntero(char n[]){
    //if(n[0]=='-') return esNatural(n+1);
    return esNatural(n);
}

bool esReal(char n[]){
    int punto = 0;
    while(n[punto]!='.' && n[punto]!='\0') ++punto;
    if(n[punto]=='\0') return esEntero(n);
    n[punto] = '\0';
    bool entero = esEntero(n);
    n[punto] = '.';
    return entero && esNatural(n+punto+1);
}

bool esDouble(char n[]){
    int e = 0;
    while(n[e]!='e' && n[e]!='\0') ++e;
    if(n[e]=='\0') return esReal(n);
    n[e] = '\0';
    bool real = esReal(n);
    n[e] = 'e';
    return real && esEntero(n+e+(n[e+1]=='+'?2:1));
}


///********************Expresion*******************************///

class Expresion{
    friend ostream& operator<<(ostream &, const Expresion&);
public:
    Expresion(const char*);
    bool validar();
    void cambiarAPostfijo();
    long double evaluarPostfijo();
    operator bool() { return esValida; }
private:
    const char* ex;
    bool esValida;
};

Expresion::Expresion(const char* _ex): ex(_ex){
    esValida = validar();
}

bool Expresion::validar(){
    Pila<char> pila;
    char numero[100] = {}, aux;
    int c=0;
    try{
        for(int i=0; ex[i]!='\0'; ++i){
            switch(ex[i]){
                case '(': case '[': case '{':
                    if(c>0) return false;
                    pila.insertarElemento(ex[i]);
                    break;
                case ')': case ']': case '}':
                    numero[c] = '\0';
                    if(esDouble(numero)){
                        if(pila.getElemento()=='+') pila.removerElemento();
                        else pila.insertarElemento('n');
                    }
                    c = 0;
                    aux = ex[i] == ')' ? '(' : ( ex[i]==']' ? '[' : '{' );
                    if(pila.removerElemento()=='n' && pila.removerElemento()==aux){
                        if (pila.estaVacia()) pila.insertarElemento('n');
                        else if(pila.getElemento()=='+') pila.removerElemento();
                        else pila.insertarElemento('n');
                    }else return false;
                    break;
                case '+': case '-': case '*': case '/': case '^':
                    numero[c] = '\0';
                    if(esDouble(numero)){
                        if(pila.estaVacia()) pila.insertarElemento('n');
                        else if(pila.getElemento()=='+') pila.removerElemento();
                        else pila.insertarElemento('n');
                    }
                    pila.insertarElemento('+');
                    c = 0;
                    break;
                default:
                    numero[c++] = ex[i];
            }
        }
        numero[c] = '\0';
        if(esDouble(numero)){
            if(pila.estaVacia()) pila.insertarElemento('n');
            else if(pila.getElemento()=='+') pila.removerElemento();
            else pila.insertarElemento('n');
        }
        return pila.getSize()==1 && pila.removerElemento()=='n';
    }catch(...){
        return false;
    }
}

long double Expresion::evaluarPostfijo(){
    Pila<long double> pila;
    char numero[100];
    long double a, b;
    int c=0;
    for(int i=0; ex[i]!='\0'; ++i){
        switch(ex[i]){
            case '+':
                numero[c] = '\0';
                b = c>0 ? parseDouble(numero) : pila.removerElemento();
                c = 0;
                a = pila.removerElemento();
                pila.insertarElemento(a+b);
                break;
            case '-':
                numero[c] = '\0';
                b = c>0 ? parseDouble(numero) : pila.removerElemento();
                c = 0;
                a = pila.removerElemento();
                pila.insertarElemento(a-b);
                break;
            case '/':
                numero[c] = '\0';
                b = c>0 ? parseDouble(numero) : pila.removerElemento();
                c = 0;
                a = pila.removerElemento();
                if(b==0) throw DivisionEntreCero();
                pila.insertarElemento(a/b);
                break;
            case '*':
                numero[c] = '\0';
                b = c>0 ? parseDouble(numero) : pila.removerElemento();
                c = 0;
                a = pila.removerElemento();
                pila.insertarElemento(a*b);
                break;
            case '^':
                numero[c] = '\0';
                b = c>0 ? parseDouble(numero) : pila.removerElemento();
                c = 0;
                a = pila.removerElemento();
                if(a==0 && b<=0) throw DivisionEntreCero();
                if(a<0 && (long int)b!=b) throw RaizCompleja();
                pila.insertarElemento(pow(a, b));
                break;
            case ',':
                if(c==0) break;
                numero[c] = '\0';
                pila.insertarElemento(parseDouble(numero));
                c = 0;
                break;
            default:
                numero[c++] = ex[i];
        }
    }
    return pila.removerElemento();
}

void Expresion::cambiarAPostfijo(){
    if(!esValida) throw(ExpresionInvalida());
    Pila<char> pila;
    char numero[100], *chila = new char[1024], aux;
    int n = 0, c=0;
    for(int i=0; ex[i]!='\0'; ++i){
        switch(ex[i]){
            case '+':  case '-':
                for(int j=0; j<n; ++j){
                    chila[c++] = numero[j];
                }
                if((pila.estaVacia() || pila.getElemento()=='(' || pila.getElemento()=='[' || pila.getElemento()=='{') && n!=0) chila[c++] = ',';
                while(!pila.estaVacia() && pila.getElemento()!='('  && pila.getElemento()!='[' && pila.getElemento()!='{') chila[c++] = pila.removerElemento();
                n=0;
                pila.insertarElemento(ex[i]);
                break;
            case '*': case '/':
                for(int j=0; j<n; ++j){
                    chila[c++] = numero[j];
                }
                if((pila.estaVacia() || (pila.getElemento()!='^' && pila.getElemento()!='*' && pila.getElemento()!='/')) && n!=0) chila[c++] = ',';
                while(!pila.estaVacia() && (pila.getElemento()=='^' || pila.getElemento()=='*' || pila.getElemento()=='/')) chila[c++] = pila.removerElemento();
                n=0;
                pila.insertarElemento(ex[i]);
                break;
            case '^':
                pila.insertarElemento('^');
                for(int j=0; j<n; ++j){
                    chila[c++] = numero[j];
                }
                if(n!=0) chila[c++] = ',';
                n = 0;
                break;
            case '(': case '[': case '{':
                pila.insertarElemento(ex[i]);
                break;
            case ')': case ']': case '}':
                for(int j=0; j<n; ++j){
                    chila[c++] = numero[j];
                }
                aux = ex[i]==')'? '(' : (ex[i]==']' ? '[' : '{');
                if(n!=0 && pila.getElemento()==aux) chila[c++] = ',';
                n = 0;
                while(pila.getElemento()!=aux) chila[c++] = pila.removerElemento();
                pila.removerElemento();
                break;
            default: numero[n++] = ex[i];
        }
    }
    for(int i=0; i<n; ++i) chila[c++] = numero[i];
    while(!pila.estaVacia()) if(pila.getElemento()!='(') chila[c++] = pila.removerElemento();
    chila[c]='\0';
    ex = chila;
}

ostream& operator<<(ostream &eout, const Expresion &e){
    eout<<e.ex;
    return eout;
}

int main(int argc, char *argv []){

    char *expr;
    if(argc>1) expr = argv[1];
    else{
        expr = new char[1024];
        cin>>expr;
    }
    Expresion expresion(expr);
    if(expresion){
        expresion.cambiarAPostfijo();
        cout<<expresion<<endl;
        cout<<expresion.evaluarPostfijo();
    }else cout<<"oshe no";
    cout<<endl;
    if(argc==1) delete [] expr;
	//system("PAUSE");
	return 0;
}
