/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<fstream>
#include<cmath>

using namespace std;

bool CrearArr(double **&, ifstream &, int &, int &);
void Operar(double **&, char[], int);
double Tdoubles(char [], int);
int Tints(char [], int);
void Imprimir(double **&, int, int);

int main(int argc, char* argv[])
{
    ifstream entrada;
    double **matriz;
    int n, m;

    if(argc>1) entrada.open(argv[1]);
    else{
        char archivo[30];
        cout<<"Nombre del archivo: ";
        cin>>archivo;
        entrada.open(archivo);
    }

    if(!entrada.good()){
        cout<<"Archivo feo.";
        return 0;
    }
    if(!CrearArr(matriz, entrada, n, m)){
        cout<<"La memoria no se pudo read.";
        return 0;
    }
    Imprimir(matriz, n, m);
    char operacion[32];
    do{
        cout<<"Operacion [Ej: -2r1+1/5r2]:";
        cin>>operacion;
        Operar(matriz, operacion, m);
        Imprimir(matriz, n, m);
    }while(true);

    //system("PAUSE");
    return 0;
}

bool CrearArr(double **&M, ifstream &Min, int &n, int &m)
{
    Min>>n;
    Min>>m;
    try{
        M = new double* [n];
        for(int i=0; i<n; ++i){
            M[i] = new double [m];
            for(int j=0; j<m; ++j){
                Min>>M[i][j];
            }
        }

        return true;
    }catch(...){
        return false;
    }
}

void Operar(double **&M, char op[], int dimm)
{
    int n, m, k, i=0, f=0;
    double kn=1, km=1;
    char en[4];

    if(op[i]=='-'){
        kn=-1;
        ++i;
    }
    for(; op[f]!='r'; ++f);
    kn *= Tdoubles(op+i, f-i);

    i=f+1;
    for(; op[f]!='-' && op[f]!='+'; ++f);
    n = Tints(op+i, f-i);

    i=f;
    if(op[i]=='-') km=-1;
    ++i;
    for(; op[f]!='r'; ++f);
    km *= Tdoubles(op+i, f-i);

    i=f+1;
    for(; op[f]!='\0'; ++f);
    m = Tints(op+i, f-i);

    cin>>en;
    for(f=0; en[f]!='\0'; ++f);
    k = Tints(en+1, f-1);

    for(int i=0; i<dimm; ++i)
        M[k][i]= M[n][i]*kn + M[m][i]*km;
}

int Tints(char op[], int length)
{
    int cifra, num;

    for(int i=0; i<length; ++i){
        cifra = op[i] - '0';
        num += cifra*pow(10, length-1 -i);
    }
    return num;
}

double Tdoubles(char op[], int length)
{
    int i_div=0;
    for(;op[i_div]!='/' && i_div<length; ++i_div);
    if(i_div==length) return Tints(op, length);
    return Tints(op, i_div)/Tints(op+i_div+1, length -i_div-1);
}

void Imprimir(double **&M, int n, int m){
    for(int i=0; i<n; ++i){
        for(int j=0; j<m; ++j) cout<<M[i][j]<<' ';
        cout<<endl;
    }
}
