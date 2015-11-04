/*
Nombre:Promedio de Calificaciones
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<fstream>
#include<iomanip>
#include<string>

using namespace std;

#define MAXCALIF 10
#define MAXALUMN 40
#define MAXGRUP 8
#define MAXNAME 50
#define MAXEXP 15
#define MAXG_NAME 10

struct Alumno{
    float *calif, promedio;
    char nombre[MAXNAME/2], apellido[MAXNAME/2], exp[MAXEXP];
};

struct Grupo{
    char materia[MAXNAME], nombre[MAXG_NAME], clave[MAXEXP];
    float promedio;
    Alumno **alumnos;
    int nCalif, nAlumn;
};

struct Profesor{
    char nombre[MAXNAME];
    Grupo *grupos;
    int nGrupos;
};

typedef void (*PtrOrdenar)(Alumno *[], int, int);

bool CrearGrupos(Grupo *&, int);
int CapturaArchivo(Grupo &, ifstream&);
void CapturaGrupo(Grupo &);
void CapturaAlumnos(Alumno *[], int, int);
void Imprimir(const Grupo [], int);
void Promedios(Grupo [], int);
void Liberar(Grupo *&, int);
void OrdenPorApellido(Alumno* [], int, int);
void OrdenPorPromedio(Alumno* [], int, int);
bool EsMayor(Alumno*, Alumno*);

int main()
{
    Profesor profe;
    int opcion;
    PtrOrdenar ordenar;

    //Si la lista de alumnos es mas ancha que la ventana, imprime un aviso
    if(MAXEXP + (8-MAXEXP%8) + MAXNAME + (8-MAXNAME%8) + 8>70)
        cout<<"AVISO: Ajusta el ancho de la ventana a "
        <<MAXEXP + (8-MAXEXP%8) + MAXNAME + (8-MAXNAME%8) + 8
        <<" caracteres o mas.\n";

    cout<<"Nombre del maestro: ";
    cin.getline(profe.nombre, MAXNAME);

    do{
        cout<<"Numero de grupos [ < "<<MAXGRUP<<" ]: ";
        cin>>profe.nGrupos;
    }while(profe.nGrupos>MAXGRUP || profe.nGrupos<0);

    if(!CrearGrupos(profe.grupos, profe.nGrupos)){
        cout<<"No hay suficiente memoria disponible";
        //system("PAUSE");
        return 0;
    }

    Promedios(profe.grupos, profe.nGrupos);
    do{
        cout<<"Ordenar por promedio o por apellido?\n1)Promedio\t2)Apellido\n";
        cin>>opcion;
    }while(opcion<1 || opcion>2);
    switch(opcion){
        case 1: ordenar = OrdenPorPromedio; break;
        case 2: ordenar = OrdenPorApellido;
    }
    for(int i=0; i<profe.nGrupos; ++i)
        ordenar(profe.grupos[i].alumnos, profe.grupos[i].nAlumn, 0);

    system("clear");
    cout<<setiosflags(ios::left)<<"Profesor: "<<profe.nombre<<endl;
    Imprimir(profe.grupos, profe.nGrupos);
    Liberar(profe.grupos, profe.nGrupos);
    //system("PAUSE");
    return 0;
}

bool CrearGrupos(Grupo *&G, int n)
{
    int opcion, i=0;
    try{
        G = new Grupo[n];
        do{
            cout<<"Tiene un archivo con datos de grupos?\n1)Si\t2)No\n";
            cin>>opcion;
        }while(opcion<1 || opcion>2);
        if(opcion==1){
            char archivo[20];
            int error=0;
            ifstream entrada;
            cout<<"\nFormato:\nClave Nombre Materia #Alumnos #Calificaciones\n";
            cout<<"Expediente Apellidos, Nombre, calif1 calif2 ...\n";
            cout<<"Expediente Apellidos, Nombre, calif1 calif2 ...\n.\n.\n.\n\n";

            do{
                cout<<"Nombre del archivo: ";
                cin>>archivo;
                entrada.open(archivo);
                if(!entrada.good() && archivo!="0"){
                    cout<<"Archivo no válido. Introduzca un archivo válido o 0 para continuar por teclado.\n";
                    error=1;
                }else{
                    if(archivo!="0")
                        for(; i<n && !entrada.eof() ; ++i){
                            error=CapturaArchivo(G[i], entrada);
                            if(error==1) return false;
                            else if(error==2){
                                cout<<"Error en la lectura del archivo.\n";
                                i=0;
                                break;
                            }
                        }
                }
            }while(error && archivo!="0");
        }
        for(; i<n; ++i){
            cout<<"\t\tGrupo #"<<i+1<<endl;
            CapturaGrupo(G[i]);
            G[i].alumnos = new Alumno* [G[i].nAlumn];
            for(int j=0; j<G[i].nAlumn; ++j){
                G[i].alumnos[j] = new Alumno;
                G[i].alumnos[j]->calif = new float [G[i].nCalif];
            }
            CapturaAlumnos(G[i].alumnos, G[i].nAlumn, G[i].nCalif);
        }
        return true;
    }catch(...){
        return false;
    }
}

int CapturaArchivo(Grupo &G, ifstream &Gin)
{
    Gin>>G.clave;
    Gin>>G.nombre;
    Gin.ignore();
    Gin.getline(G.materia, MAXNAME, ',');
    Gin>>G.nAlumn;
    Gin>>G.nCalif;
    try{
        G.alumnos = new Alumno* [G.nAlumn];
        for(int i=0; i<G.nAlumn; ++i){
            G.alumnos[i] = new Alumno;
            G.alumnos[i]->calif = new float [G.nCalif];

            Gin>>G.alumnos[i]->exp;
            Gin.ignore();
            Gin.getline(G.alumnos[i]->apellido, MAXNAME/2, ',');
            Gin.ignore();
            Gin.getline(G.alumnos[i]->nombre, MAXNAME/2, ',');
            for(int j=0; j<G.nCalif; ++j) Gin>>G.alumnos[i]->calif[j];
        }
        return 0;
    }catch(...){
        return 1;
    }
}

void CapturaGrupo(Grupo &G)
{
    cout<<"Clave: ";
    cin>>G.clave;
    cout<<"Nombre: ";
    cin>>G.nombre;
    cout<<"Materia: ";
    cin.ignore();
    cin.getline(G.materia, MAXNAME);
    do{
        cout<<"Numero de alumnos [ < "<<MAXALUMN<<" ]: ";
        cin>>G.nAlumn;
    }while(G.nAlumn>MAXALUMN || G.nAlumn<0);
    do{
        cout<<"Numero de calificaciones [ < "<<MAXCALIF<<" ]: ";
        cin>>G.nCalif;
    }while(G.nCalif>MAXCALIF || G.nCalif<0);
}

void CapturaAlumnos(Alumno *A[], int n, int calif)
{
    for(int i=0; i<n; ++i){
        cout<<"\tAlumno #"<<i+1<<endl;
        cout<<"Expediente: ";
        cin>>A[i]->exp;
        cout<<"Apellidos: ";
        cin.ignore();
        cin.getline(A[i]->apellido, MAXNAME/2);
        cout<<"Nombre: ";
        cin.getline(A[i]->nombre, MAXNAME/2);
        for(int j=0; j<calif; ++j){
            cout<<"Calificacion "<<j+1<<": ";
            cin>>A[i]->calif[j];
        }
    }

}

void Promedios(Grupo G[], int n)
{
    for(int i=0; i<n; ++i){
        G[i].promedio=0;
        for(int j=0; j<G[i].nAlumn; ++j){
            G[i].alumnos[j]->promedio=0;
            for(int k=0; k<G[i].nCalif; ++k)
                G[i].alumnos[j]->promedio+=G[i].alumnos[j]->calif[k];
            G[i].alumnos[j]->promedio/=G[i].nCalif;
            G[i].promedio+=G[i].alumnos[j]->promedio;
        }
        G[i].promedio/=G[i].nAlumn;
    }
}

void OrdenPorApellido(Alumno *Almns[], int n, int inicio)
{
    Alumno *aux;
    int izq=inicio, der=n-1, pivote=inicio;
    while(izq != der){
        for(; EsMayor(Almns[der], Almns[pivote]) && pivote<der;--der);
        aux=Almns[pivote];
        Almns[pivote] = Almns[der];
        Almns[der] = aux;
        pivote = der;

        for(; EsMayor(Almns[pivote], Almns[izq]) && pivote>izq;++izq);
        aux=Almns[pivote];
        Almns[pivote] = Almns[izq];
        Almns[izq] = aux;
        pivote = izq;
    }
    if(pivote-inicio>1) OrdenPorApellido(Almns, pivote, inicio);
    if(n-pivote>1) OrdenPorApellido(Almns, n, pivote+1);
}

bool EsMayor(Alumno *A, Alumno *B)
{
    for(int i=0; i<MAXNAME/2; ++i){
        if(A->apellido[i] > B->apellido[i]) return true;
        if(A->apellido[i] < B->apellido[i]) return false;
        if(A->apellido[i]=='\0' && B->apellido[i]=='\0') break;
    }
    for(int i=0; i<MAXNAME/2; ++i){
        if(A->nombre[i] > B->nombre[i]) return true;
        if(A->nombre[i] < B->nombre[i]) return false;
        if(A->nombre[i]=='\0' && B->nombre[i]=='\0') return true;
    }
}

void OrdenPorPromedio(Alumno *Almns[], int n, int inicio)
{
    Alumno *aux;
    int izq=inicio, der=n-1, pivote=inicio;
    int test;       /////////////////////////////////
    while(izq != der){
        for(; Almns[der]->promedio <= Almns[pivote]->promedio && pivote<der;--der);
        aux=Almns[pivote];
        Almns[pivote] = Almns[der];
        Almns[der] = aux;
        pivote = der;

        for(; Almns[pivote]->promedio <= Almns[izq]->promedio && pivote>izq;++izq);
        aux=Almns[pivote];
        Almns[pivote] = Almns[izq];
        Almns[izq] = aux;
        pivote = izq;
    }
    if(pivote-inicio>1) OrdenPorPromedio(Almns, pivote, inicio);
    if(n-pivote>1) OrdenPorPromedio(Almns, n, pivote+1);
}

void Imprimir(const Grupo G[], int n)
{
    for(int i=0; i<n; ++i){
        cout<<"Clave: "<<G[i].clave;
        cout<<"\tGrupo: "<<G[i].nombre;
        cout<<"\tMateria: "<<G[i].materia<<"\n\n";
        cout<<setw(MAXEXP)<<"Expediente";
        cout<<'\t'<<setw(MAXNAME)<<"Nombre";
        cout<<"\tPromedio\n";
        for(int j=0; j<G[i].nAlumn; ++j){
            cout<<setw(MAXEXP)<<G[i].alumnos[j]->exp;
            cout<<'\t'<<setw(MAXNAME)<<(string)G[i].alumnos[j]->apellido +" "+ (string)G[i].alumnos[j]->nombre;
            cout<<'\t'<<setw(8)<<G[i].alumnos[j]->promedio;
            cout<<endl;
        }
        cout<<"\nPromedio general del grupo: "<<G[i].promedio<<endl;
        cout<<setw(80)<<setfill('-')<<'-'<<setfill(' ')<<endl;
    }
}

void Liberar(Grupo *&G, int n)
{
    for(int i=0; i<n; ++i){
        for(int j=0; j<G[i].nAlumn; ++j){
            delete [] G[i].alumnos[j]->calif;
            delete G[i].alumnos[j];
        }
        delete [] G[i].alumnos;
    }
    delete [] G;
    G=NULL;
}
