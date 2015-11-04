/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<new>
#include<iomanip>

using namespace std;

#define MAXCALIF 3
#define MAXALUMN 40

struct Alumno{
    float calif[MAXCALIF], promedio;
    char nombre[50];
    int id;
};

bool CrearArr(Alumno *&, int);
void CapturarAlumn(Alumno &, int);
void Imprimir(Alumno &);

int main()
{

    int n;
    Alumno * A = NULL;
    do{
        cout<<"Numero de alumnos: ";
        cin>>n;
    }while(n<1 || n>MAXALUMN);

    CrearArr(A, n);
    for(int i=0; i<n; ++i)
        CapturarAlumn(A[i], i+1);

    cout<<setiosflags(ios::left)<<"|# | "<<setw(49)<<"NOMBRE"<<"|PROMEDIO|"<<endl;


    for(int i=0; i<n; ++i)
        Imprimir(A[i]);

    delete [] A;
    //system("PAUSE");
    return 0;
}

bool CrearArr(Alumno *& A, int n)
{
    try{
        A = new Alumno[n];
        return true;
    }catch(...){
        return false;
    }
}

void CapturarAlumn(Alumno &A, int indice)
{
    A.id = indice;

    cin.ignore();
    cout<<"Nombre del alumno #"<<indice<<": ";
    cin.getline(A.nombre, 50);

    for(int i=0; i<MAXCALIF; ++i){
        cout<<"Calificacion "<<i+1<<": ";
        cin>>A.calif[i];
    }
    A.promedio=0;
    for(int i=0; i<MAXCALIF; ++i) A.promedio+=A.calif[i];
    A.promedio/=MAXCALIF;
}

void Imprimir(Alumno &A)
{
    cout<<'|'<<setw(2)<<A.id<<"| "<<setw(49)<<A.nombre<<'|'<<setw(8)<<int(A.promedio)<<'|'<<endl;
}
