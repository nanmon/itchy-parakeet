/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha: 28/03/14 19:40
*/

#include<iostream>
#include<cstdlib>
#include<ctime>
#include<new>

using namespace std;

bool Crear(int *&, int);
void Relleno(int [], int);
void Metodo(int [], int);
void Burbuja(int [], int);
void Seleccion(int [], int);
void Insercion(int [], int);
void Rapido(int [], int, int);
void Grafica(int[], int, int=0);
//void MueveCursor(unsigned short ,unsigned short );

int main()
{
    int *Numeros=NULL;
    int n;
    do{
        cout<<"n: ";
        cin>>n;
    }while(n<1);

    if(!Crear(Numeros, n)){
        cout<<"No hay espacio en la memoria.\n";
        system("PAUSE");
        return 0;
    }

    Relleno(Numeros, n);
	system("clear");
    Grafica(Numeros, n);
    Metodo(Numeros, n);
	Grafica(Numeros, n);
    delete [] Numeros;

    system("PAUSE");
    return 0;
}

bool Crear(int *&Arreglo, int n)
{
    try{
        Arreglo = new int[n];
        return true;
    }catch(...){
        return false;
    }
}

void Relleno(int Arreglo[], int n)
{
    char esAlAzar;
    int inicio, fin;
    do{
        cout<<"\xA8Generar al azar? s/n: ";
        cin>>esAlAzar;
    }while(esAlAzar!='s' && esAlAzar!='n');
    if(esAlAzar=='s'){
        srand(time(NULL));
        for(int i=0; i<n;++i) Arreglo[i]=1 + rand()%20;
    }else{
        cout<<"Valores menores o iguales a 20\n";
        for(int i=0; i<n;++i){
                cout<<"Valor del elemento "<<i+1<<": ";
                cin>>Arreglo[i];
                if(Arreglo[i]>20 || Arreglo[i]<1){
					cout<<"Error: Fuera de Rango.\n";
					--i;
				}
        }
    }

}

void Metodo(int Arreglo[], int n)
{
    int opc;
    do{
        cout<<"\nElija un m\x82todo de ordenamiento:\n1)Burbuja\t2)Selecci\xA2n\t3)Inserci\xA2n\t4)R\xA0pido\n";
        cin>>opc;
    }while(opc<1 && opc>0);

    switch(opc){
        case 1: Burbuja(Arreglo, n); break;
        case 2: Seleccion(Arreglo, n); break;
        case 3: Insercion(Arreglo, n); break;
        case 4: Rapido(Arreglo, n, 0);
    }
}

void Burbuja(int Arreglo[], int n)
{
    bool listo=false;
    int aux;
    for(int i=n-1;!listo;--i){
        listo=true;
        for(int j=0; j<i; ++j){
            if(Arreglo[j]>Arreglo[j+1]){
                aux=Arreglo[j];
                Arreglo[j]=Arreglo[j+1];
                Arreglo[j+1]=aux;
                listo=false;
            }
            Grafica(Arreglo, n);
        }
    }
}

void Seleccion(int Arreglo[], int n)
{
    int posMenor, aux;
    for(int i=0;i<n-1;++i){
        posMenor=i;
        for(int j=i+1; j<n; ++j){
            if(Arreglo[j]<Arreglo[posMenor]) posMenor=j;
        }
        aux = Arreglo[i];
        Arreglo[i]= Arreglo[posMenor];
        Arreglo[posMenor]=aux;
        Grafica(Arreglo, n);
    }
}

void Insercion(int Arreglo[], int n)
{
    int aux, aux2, j;
    bool esMenor;
    for(int i=1;i<n;++i){
        j=0;
        esMenor=false;
        for(; j<i && !esMenor ; ++j){
            if(Arreglo[i]<Arreglo[j]){
                aux=Arreglo[j];
                Arreglo[j]=Arreglo[i];
                esMenor=true;
            }
        }
        Grafica(Arreglo, n);

        for(; j<=i && esMenor ; ++j){
            aux2 = Arreglo[j];
            Arreglo[j] = aux;
            aux = aux2;
            Grafica(Arreglo, n);
        }
    }
}

void Rapido(int Arreglo[], int n, int inicio)
{
    int izq=inicio, der=n-1, pivote=inicio, aux;
    while(izq != der){
        for(; Arreglo[der]>=Arreglo[pivote] && pivote<der;--der)
            Grafica(Arreglo, n, inicio);
        aux=Arreglo[pivote];
        Arreglo[pivote] = Arreglo[der];
        Arreglo[der] = aux;
        pivote = der;


        for(; Arreglo[izq]<=Arreglo[pivote] && pivote>izq;++izq)
            Grafica(Arreglo, n, inicio);
        aux=Arreglo[pivote];
        Arreglo[pivote] = Arreglo[izq];
        Arreglo[izq] = aux;
        pivote = izq;


    }
    if(pivote-inicio>1) Rapido(Arreglo, pivote, inicio);
    if(n-pivote>1) Rapido(Arreglo, n, pivote+1);
}

void Grafica(int Arreglo[], int n, int inicio)
{
	static int tama_o = n;
	system("clear");
	for(int i=20; i>=1; --i){
		for(int j=0; j<inicio; ++j)	cout<<(Arreglo[j]>=i ? 'M' : ' ');
		for(int j=inicio; j<n; ++j) cout<<(Arreglo[j]>=i ? '|' : ' ');
		for(int j=n; j<tama_o; ++j)	cout<<(Arreglo[j]>=i ? 'M' : ' ');
		cout<<endl;
	}
}

/*void MueveCursor(unsigned short x,unsigned short y)
{
	HANDLE handle = GetStdHandle(STD_OUTPUT_HANDLE);
	COORD coord = {x-1,y-1};
	SetConsoleCursorPosition(handle,coord);
}*/
