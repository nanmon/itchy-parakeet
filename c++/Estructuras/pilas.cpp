/**
	Nombre:
	Autor: L. Roberto Montaño Valdez
	Fecha:
	Descripción:
*/

#include<cstdlib>
#include<iostream>

using namespace std;

template<class T=int>
class Pila{
public:
    explicit Pila<T>(int n=10):MAX(n), tope(-1) {
        arr = new T[n];
    }
    Pila<T>(const Pila<T>&);
    Pila<T>& operator=(const Pila<T>&);
    ~Pila<T>() { delete[] arr; }
    void insert(T);
    T remove();
    T get();
    bool empty();

    int count();
    void print();
private:
    void redim();
    bool full();
    int MAX;
    int tope;
    T *arr;
};

template<class T>
Pila<T>::Pila(const Pila<T> &p):MAX(-1), arr(NULL){
    *this=p;
}

template<class T>
Pila<T>& Pila<T>::operator=(const Pila<T> &p){
    if(this!=&p){
        tope = p.tope;
        if(MAX <= p.tope){
            this->MAX = p.MAX;
            if(arr!=NULL) delete [] this->arr;
            this->arr = new (nothrow) T[this->MAX];
            if(!arr) throw false;
        }
        for(int i=0; i<=p.tope; ++i) this->arr[i] = p.arr[i];
    }
    return *this;
}

template<class T>
void Pila<T>::insert(T dato){
    if(full()) redim();
    arr[++tope] = dato;
}

template<class T>
T Pila<T>::remove(){
    if(empty()) throw "Error: Pila vacia";
    return arr[tope--];
}

template<class T>
T Pila<T>::get(){
    if(empty()) throw "Error: Pila vacia";
    return arr[tope];
}

template<class T>
int Pila<T>::count(){
    return tope+1;
}

template<class T>
void Pila<T>::print(){
    if(empty()){
        cout<<"[]";
        return;
    }
    cout<<"[";
    for(int i=0; i<=tope; ++i){
        cout<<arr[i]<<", ";
    }
    cout<<"\b\b]";
}

template<class T>
void Pila<T>::redim(){
    T *aux = arr;
    arr = new (nothrow) T[MAX*2];
    if(!arr) throw false; ///NoHayMemoria exception
    for(int i=0; i<MAX; ++i) arr[i] = aux[i];
    MAX*=2;
}

template<class T>
bool Pila<T>::full(){
    return tope==MAX-1;
}

template<class T>
bool Pila<T>::empty(){
    return tope==-1;
}

int main(int argc, char *argv[]){

    Pila<float> p2(3);
    p2.insert(5.2);
    p2.insert(3.5);

    Pila<float> p(p2);

try{
    cout<<"add 5: ";
    p.insert(5.2f);
    p.print();
    cout<<">> tamaño: "<<p.count()<<endl;

    cout<<"add 3: ";
    p.insert(3.5f);
    p.print();
    cout<<">> tamaño: "<<p.count()<<endl;

    cout<<"get: "<<p.remove()<<endl;

    p.print();
    cout<<endl;

    cout<<"get: "<<p.remove()<<endl;

    p.print();

    p.get();
}catch(const char *e){
    cout<<e;
}

	//system("PAUSE");
	return 0;
}
