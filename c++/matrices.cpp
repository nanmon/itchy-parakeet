/*
Nombre: Operaciones con Matrices
Autor: Montaño Valdez L. Roberto
Descripcion: Programa que realiza operaciones con matrices.
Fecha: 02/May/2014
*/

#include<iostream>
#include<cstdlib>
#include<iomanip>
#include<cstdio>

#define MAX 9

void Captura(float [][MAX], int, int, char);
void Imprimir(float [][MAX], int, int, char);
void Suma(float [][MAX], float [][MAX], int, int);
void Diferencia(float [][MAX], float [][MAX], int, int);
void Producto(float [][MAX], float [][MAX], int, int, int);
void ProductoE(float [][MAX], int, int);
void Transpuesta(float [][MAX], int, int);
void Inversa(float [][MAX], int);
float Determinante(float [][MAX], int);

using namespace std;

int main()
{
    float A[MAX][MAX], B[MAX][MAX];
    srand(time(NULL));
    int opcion, n, m, k;
    do{
        cout<<"Programa que realiza operaciones con matrices. Elija una operación:\n";
        do{
            cout<<"1)Suma\n2)Diferencia\n3)Producto\n4)Producto Escalar\n5)Transpuesta\n6)Inversa\n7)Determinante\n0)Salir\n\n";
            cin>>opcion;
        }while(opcion<0 || opcion>7);

        switch(opcion){
            case 0:
                system("PAUSE");
                return 0;
            case 1:
            case 2:
                cout<<"Necesario 2 matrices, ambas de nxm.\n";
                break;
            case 3:
                cout<<"Necesario 2 matrices, una nxm y otra mxk";
                break;
            case 4:
            case 5:
                cout<<"Necesario una matriz de nxm";
                break;
            case 6:
            case 7:
                cout<<"Necesario una matriz de nxn";
                break;
        }
        do{
            cout<<"\nValor de n [2<n<9]: ";
            cin>>n;
        }while(n<2 || n>MAX);
        if(opcion>=1 && opcion<=5)
            do{
                cout<<"Valor de m [2<m<9]: ";
                cin>>m;
            }while(m<2 || m>MAX);
        else m=n;
        Captura(A, n, m, 'A');

        if(opcion==3){
            do{
                cout<<"\nValor de k[2<n<9]: ";
                cin>>k;
            }while(k<2 || k>9);
            Captura(B, m, k, 'B');
        }else if(opcion<3) Captura(B, n, m, 'B');

        switch(opcion){
            case 1: Suma(A, B, n, m); break;
            case 2: Diferencia(A, B, n, m); break;
            case 3: Producto(A, B, n, m, k); break;
            case 4: ProductoE(A, n, m); break;
            case 5: Transpuesta(A, n, m); break;
            case 6: Inversa(A, n); break;
            case 7: cout<<"D="<<Determinante(A, n);
        }
        system("PAUSE");
        system("CLS");
    }while(opcion!=0);
}

void Suma(float A[][MAX], float B[][MAX], int n, int m)
{
    float R[MAX][MAX];

    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) R[i][j]=A[i][j]+B[i][j];

    Imprimir(R, n, m, 'R');
}

void Diferencia(float A[][MAX], float B[][MAX], int n, int m)
{
    float R[MAX][MAX];

    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) R[i][j]=A[i][j]-B[i][j];

    Imprimir(R, n, m, 'R');
}

void Producto(float A[][MAX], float B[][MAX], int n, int m, int k)
{
    float aux=0, R[MAX][MAX];

    for(int i=0; i<n; ++i)
        for(int j=0; j<k; ++j){
            for(int l=0; l<m; ++l) aux+=A[i][l]*B[l][j];
            R[i][j]=aux;
            aux=0;
        }

    Imprimir(R, n, k, 'R');
}

void ProductoE(float A[][MAX], int n, int m)
{
    float e, R[MAX][MAX];
    cout<<"Escalar: ";
    cin>>e;

    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) R[i][j]=A[i][j]*e;

    Imprimir(R, n, m, 'R');
}

void Transpuesta(float A[][MAX], int n, int m)
{
    float R[MAX][MAX];

    for(int i=0; i<m; ++i)
        for(int j=0; j<n; ++j) R[i][j]=A[j][i];

    Imprimir(R, m, n, 'R');
}

void Inversa(float A[][MAX], int n)
{
    float aux=0, R[MAX][MAX], I[MAX][MAX]={};

    if(Determinante(A, n)){
        for(int i=0; i<n; ++i){
            for(int j=0; j<n; ++j){
                R[i][j] = A[i][j];
            }
            I[i][i]=1;
        }

        for(int i=0; i<n; ++i){
            for(int j=0; j<i; ++j){
                aux=R[i][j];
                for(int k=0; k<n; ++k){
                    R[i][k]-=R[j][k]*aux;
                    I[i][k]-=I[j][k]*aux;
                }
            }
            aux=R[i][i];
            if(!aux){
                int l=1;
                for(; !R[i+l][i];++l);
                for(int j=0; j<n; ++j){
                    aux=R[i][j];
                    R[i][j]=R[i+l][j];
                    R[i+l][j]=aux;
                    aux=I[i][j];
                    I[i][j]=I[i+l][j];
                    I[i+l][j]=aux;
                }
                --i;
            }else for(int j=0; j<n; ++j){
                R[i][j]/=aux;
                I[i][j]/=aux;
            }
        }

        for(int i=n-2; i>=0; --i){
            for(int j=n-1; j>i; --j){
                aux=R[i][j];
                R[i][j]-=R[j][j]*aux;
                for(int k=0; k<n;++k)
                    I[i][k]-=I[j][k]*aux;
            }
        }
        Imprimir(I, n, n, 'I');
    }else cout<<"\nError: La matriz no es invertible.";
}

float Determinante(float A[][MAX], int n)
{
    float det=0;
    if(n==2)
        det=A[0][0]*A[1][1]-A[0][1]*A[1][0];
    else if (n>2){
        float D[MAX][MAX];
        for(int i=0; i<n; ++i){
            for(int j=0; j<n-1; ++j)
                for(int k=0; k<n-1; ++k) D[j][k]=A[j][(i+k)%n];
            det+=A[n-1][(n-1+i)%n]*Determinante(D, n-1);
        }
    }else det=A[0][0];
    return det;
}

void Captura(float A[][MAX], int n, int m, char letra)
{
    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) A[i][j]=rand()%5;
    Imprimir(A, n, m, letra);
}

void Imprimir(float A[][MAX], int n, int m, char letra)
{
    cout<<'\n'<<letra<<"=|";
    for(int i=0; i<n; ++i){
        for(int j=0; j<m; ++j) cout<<setw(6)<<setprecision(2)<<fixed<<A[i][j]<<",";
        cout<<"  |\n  |";
    }
    cout<<"\b \n";
}
