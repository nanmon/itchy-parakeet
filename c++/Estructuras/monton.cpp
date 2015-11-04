/*
Autor: Roberto Montaño
Fecha: 26/mar/2015
*/

#include <cstdlib>
#include <iostream>
#include <cmath>
#include <iomanip>

using namespace std;

class NoHayMemoria : public exception{
public: const char* what() const throw() { return "No hay memoria disponible."; }
};

class MontonVacio : public exception{
public: const char* what() const throw() { return "El monton esta vacio."; }
};

///****************************MONTON***************************///
template<class T,bool m> class Monton;
template<class T, bool m>
ostream& operator<<(ostream&, const Monton<T,m>&);

template <class T, bool minimo = true>
class Monton{
	friend ostream& operator<< <>(ostream&, const Monton<T,minimo>&);
public:
	Monton();
	Monton(const Monton<T,minimo>&);
	~Monton();
	Monton<T,minimo>& operator=(const Monton<T,minimo>&);

	void insert(T, int);
	T remove();
	T get();
	void empty();
private:
	void pushUp(int);
	void pushDown(int);
	void swap(int, int);
	void redimensionar(int);

	int size;
	int last;
	struct Elemento{
		Elemento(T i, int p):info(i), prioridad(p){}
		T info;
		int prioridad;
	} **arr;
	bool min(int a,int b){ return arr[a]->prioridad<arr[b]->prioridad; }
	bool max(int a,int b){ return arr[a]->prioridad>arr[b]->prioridad; }
	constexpr static bool (Monton<T, minimo>::*criterio) (int,int) = minimo ? &Monton<T,minimo>::min : &Monton<T,minimo>::max;
};

//////////////////////////////////////////Constructores, operator=, destructor

template <class T, bool minimo>
Monton<T,minimo>::Monton():arr(NULL),size(1),last(-1){
	 //criterio  = minimo ? &Monton<T,minimo>::min : &Monton<T,minimo>::max;
	 arr = new (nothrow) Elemento*[size];
	 if(!arr) throw NoHayMemoria();
}

template <class T, bool minimo>
Monton<T,minimo>::Monton(const Monton<T,minimo> &m):arr(NULL), size(m.size), last(-1){
	arr = new (nothrow) Elemento*[size];
	if(!arr) throw NoHayMemoria();
	*this = m;
}

template <class T, bool minimo>
Monton<T,minimo>::~Monton(){
	empty();
}

template <class T, bool minimo>
Monton<T,minimo>& Monton<T,minimo>::operator=(const Monton<T,minimo> &m){
	if(this!=&m){
		if(size != m.size) redimensionar(m.size);
		int minlast = last > m.last ? last : m.last;
		for(int i=0; i<=minlast; ++i)
			arr[i]->info = m.arr[i]->info;
		if(last > m.last)
			for(int i = minlast+1; i<=last; ++i) delete arr[i];
		else if(last < m.last)
			for(int i = minlast+1; i<=m.last; ++i){
				arr[i] = new Elemento(m.arr[i]->info, m.arr[i]->prioridad);
				if(!arr[i]) throw NoHayMemoria();
			}
	}
	return *this;
}

//////////////////////////////////////////Funciones miembro

template <class T, bool m>
void Monton<T,m>::insert(T in, int prioridad){
	if(++last == size)
		redimensionar(size * 2 +1);
	arr[last] = new (nothrow) Elemento(in, prioridad);
	if(!arr[last]) throw NoHayMemoria();
	pushUp(last);
}

template <class T, bool m>
T Monton<T,m>::remove(){
	if(last==-1) throw MontonVacio();
	swap(0, last);
	T r = arr[last--]->info;
	if(last==(size-3)/4)
		redimensionar((size-1)/2);
	pushDown(0);
	return r;
}

template <class T, bool m>
T Monton<T,m>::get(){
	if(last==-1) throw MontonVacio();
	return arr[0]->info;
}

template <class T, bool m>
void Monton<T,m>::empty(){
	for(int i=0; i<=last;++i)
		delete arr[i];
	delete[] arr;
	arr = new Elemento*[1];
	if(!arr) throw NoHayMemoria();
	size = 1;
	last = -1;
}

template <class T, bool m>
void Monton<T,m>::pushUp(int hijo){
	int padre = (hijo-1)/2;
	if(hijo==0 || !(this->*criterio)(hijo, padre)) return;
	swap(hijo, padre);
	pushUp(padre);
}

template <class T, bool m>
void Monton<T,m>::pushDown(int padre){
	int hijo = 2 * padre + 1;//izq
	if(hijo+1<=last && (this->*criterio)(hijo+1, hijo)) ++hijo; //der
	if(hijo>last || !(this->*criterio)(hijo, padre)) return;
	swap(hijo, padre);
	pushDown(hijo);
}

template<class T, bool m>
ostream& operator<<(ostream &mout, const Monton<T,m> &mon){
	if(mon.last==-1){
		mout<<"Monton vacio";
		return mout;
	}
	for(int i=0; i<=mon.last; ++i){
		mout<<'('<<mon.arr[i]->info<<','<<mon.arr[i]->prioridad<<")  ";
		float nivel = log2(i+2);
		if(nivel == (int)nivel) cout<<endl;
	}
	return mout<<"\b\n";
}

template<class T, bool m>
void Monton<T,m>::swap(int a, int b){
	Elemento *aux = arr[a];
	arr[a] = arr[b];
	arr[b] = aux;
}

template<class T, bool m>
void Monton<T,m>::redimensionar(int r){
	Elemento **nuevo = new Elemento*[r];
	if(!nuevo) throw NoHayMemoria();
	for(int i=0; i<=last; ++i) nuevo[i] = arr[i];
	delete[] arr;
	arr = nuevo;
	size = r;
}

template <bool m = true>
void menu();

template <bool m = true>
void encolar(Monton<char,m>&, const char*);

int main(int argc, char const *argv[])
{

	menu<false>();

	system("PAUSE");
	return 0;
}

template <bool min>
void menu(){
	Monton<char, min> m;
	int op;
	char in[100];

	cout<<"Monton "<<(min?"minimo":"maximo")<<endl;
	cout<<"Formato para encolar: etiqueta[char],prioridad[int]\n";

	do{
		cout<<"1)Encolar\n2)Desencolar\n3)Ver siguiente\n4)Imprimir\n5)Vaciar\n0)Salir\n";
		cin>>op;
		switch(op){
		case 1:
			cout<<"encolar: ";
			cin>>in;
			encolar(m, in);
			break;
		case 2:
			cout<<"desencolado: "<<m.remove()<<endl;
			break;
		case 3:
			cout<<"siguiente: "<<m.get()<<endl;
			break;
		case 4:
			cout<<m;
			break;
		case 5:
			m.empty();
		}
		cout<<endl;
	}while(op!=0);
}

template <bool min>
void encolar(Monton<char, min> &m, const char *in){
	char name = in[0];
	if(in[1] != ',' || in[2]=='\0') return;
	int prio = 0;
	for(int i=2; in[i]!='\0'; ++i){
		if(in[i]<48 || in[i]>57) return;
		prio*=10;
		prio += in[i] - 48;
	}
	m.insert(name, prio);
}