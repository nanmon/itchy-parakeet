/**
	Nombre:
	Autor: L. Roberto Montaño Valdez
	Fecha:
	Descripción:
*/

#include<cstdlib>
#include<iostream>

using namespace std;

///********************************EXCEPCIONES*******************************///

class DivisionEntreCero : public exception{
public:
    const char* what() const throw() { return "Error: División entre Cero."; }
};

class IndiceFueraDeRango : public exception{
public:
    const char* what() const throw() { return "Error: Indice fuera de rango."; }
};

///************************************RACIONAL*******************************///

class Racional{
    friend ostream& operator<<(ostream &, const Racional&);
    friend istream& operator>>(istream &, Racional&);
public:
    Racional(long int, long int);
    Racional():numerador(0), denominador(1){}

    Racional operator+(const Racional&) const;
    Racional operator-(const Racional&) const;
    Racional operator*(const Racional&) const;
    Racional operator/(const Racional&) const;

    bool operator>(const Racional&) const;
    bool operator<(const Racional&) const;
    bool operator==(const Racional&) const;
    bool operator>=(const Racional&) const;
    bool operator<=(const Racional&) const;
    bool operator!=(const Racional&) const;
    operator bool() const;
private:
    void simplificar();
    void denPositivo();
    long int numerador, denominador;
};

///****************************Utileria**********************************///

void Racional::simplificar(){
    if(numerador==0){
        denominador = 1;
        return;
    }
    long int menor = numerador<denominador ? numerador : denominador;
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
    simplificar();
    denPositivo();
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
    long int x = numerador*a.denominador, y = a.numerador*denominador;
    return x<y;
}

bool Racional::operator==(const Racional &a) const{
    long int x = numerador*a.denominador, y = a.numerador*denominador;
    return x==y;
}

bool Racional::operator>=(const Racional &a) const{
    if(*this > a) return true;
    return *this == a;
}

bool Racional::operator<=(const Racional &a) const{
    if(*this < a) return true;
    return *this == a;
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
    r.simplificar();
    r.denPositivo();
}

///************************ARREGLO************************///

template <class T> class Arreglo;

template <class T>
ostream& operator<< (ostream&, const Arreglo<T>&);
template <class T>
istream& operator>> (istream&, Arreglo<T>&);

template <class T>
class Arreglo{
    friend ostream& operator<< <>(ostream&, const Arreglo<T>&);
    friend istream& operator>> <>(istream&, Arreglo<T>&);
public:
    Arreglo(int=10);
    Arreglo(const Arreglo<T> &);
    Arreglo& operator=(const Arreglo<T> &);
    ~Arreglo();

    T operator[](int);
    T operator[](int) const;
    void ordenarAsc();
    void ordenarDes();

    const int n;
private:
    void quicksort(int, int, bool);
    T *arr;

};

///*********************************Utileria*************************************///

template <class T>
void Arreglo<T>::quicksort(int inicio, int fin, bool esAsc){
    int izq = inicio, der = fin;
    T aux;

    while(izq<der){
        while((esAsc? arr[izq]<=arr[der] : arr[izq]>=arr[der]) && izq<der) --der;
        if(izq<der){
            aux = arr[izq];
            arr[izq] = arr[der];
            arr[der] = aux;
        }

        while((esAsc? arr[izq]<=arr[der] : arr[izq]>=arr[der]) && izq<der) ++izq;
        if(izq<der){
            aux = arr[izq];
            arr[izq] = arr[der];
            arr[der] = aux;
        }
    }
    if(inicio < izq-1)this->quicksort(inicio, izq-1, esAsc);
    if(der+1 < fin)this->quicksort(der+1, fin, esAsc);

}

///*********************************Constructores, destructor y operator=********************************///

template <class T>
Arreglo<T>::Arreglo(int _n):n(_n){
    arr = new T[n];
}

template <class T>
Arreglo<T>::Arreglo(const Arreglo<T> &a):n(a.n), arr(NULL){
    *this = a;
}

template <class T>
Arreglo<T>& Arreglo<T>::operator=(const Arreglo<T> &a){
    if(*this != &a){
        if(arr!=NULL) delete [] arr;
        arr = new T[n];
        for(int i=0; i<n; ++i){
            arr[i] = a.arr[i];
        }
    }

    return *this;
}

template <class T>
Arreglo<T>::~Arreglo(){
    delete [] arr;
}

///****************************** [] y ordenamiento ***************************///

template <class T>
T Arreglo<T>::operator[](int i){
    if(i<0 || i>=n) throw IndiceFueraDeRango();
    return arr[i];
}

template <class T>
T Arreglo<T>::operator[](int i) const{
    if(i<0 || i>=n) throw IndiceFueraDeRango();
    return arr[i];
}

template <class T>
void Arreglo<T>::ordenarAsc(){
    this->quicksort(0, n-1, true);
}

template <class T>
void Arreglo<T>::ordenarDes(){
    this->quicksort(0, n-1, false);
}

///**************************************Amiguitas********************************///

template <class T>
ostream& operator<< (ostream& aout, const Arreglo<T> &a){
    aout<<"[";
    for(int i=0; i<a.n; ++i) aout<<a.arr[i]<<", ";
    aout<<"\b\b]";
}

template <class T>
istream& operator>> (istream& ain, Arreglo<T> &a){
    for(int i=0; i<a.n; ++i){
        cout<<"Elemento "<<i+1<<":"<<endl;
        cin>>a.arr[i];
    }
}

///****************************************MAIN**************************///

int main(int argc, char **argv)
{
    try{
        int n;

        cout<<"Programa que captura un arreglo de numeros racionales, los ordena, los suma y los multiplica\n";
        do{
            cout<<"Numero de racionales: ";
            cin>>n;
        }while(n<=0);

        Arreglo<Racional> arr(n);
        cin>>arr;
        cout<<arr<<endl;

        do{
            cout<<"Ordenar:\n 1)Ascendiente\t2)Descendiente\n";
            cin>>n;
        }while(n<=0 || n>2);

        n==1 ? arr.ordenarAsc() : arr.ordenarDes();

        cout<<arr<<endl;

        Racional r(0, 1);
        for(int i=0; i<arr.n; ++i){
            r = r+arr[i];
        }

        cout<<"Suma total: "<<r<<endl;

        r = Racional(1, 1);
        for(int i=0; i<arr.n; ++i){
            r = r*arr[i];
        }

        cout<<"Producto total: "<<r;

    }catch(exception &e){
        cout<<e.what();
    }


	system("PAUSE");
	return 0;
}
