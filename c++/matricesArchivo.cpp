/*
Nombre: Operaciones con Matrices con archivos con matrices.
Autor: Montaño Valdez L. Roberto
Descripcion: Programa que realiza operaciones con matrices.
Fecha: 02/May/2014
*/

#include<iostream>
#include<math.h>
#include<iomanip>
#include<cstdlib>
#include<fstream>

#define MAX 9

bool Captura(float [][MAX], int*, int*, char[]);
void Imprimir(float [][MAX], int, int, char);
void Suma(float [][MAX], float [][MAX], int, int);
void Diferencia(float [][MAX], float [][MAX], int, int);
void Producto(float [][MAX], float [][MAX], int, int, int);
void ProductoE(float [][MAX], int, int);
void Transpuesta(float [][MAX], int, int);
void Inversa(float [][MAX], int);
float Determinante(float [][MAX], int);
float CadenaAFloat(char [], int);

using namespace std;

int main()
{
    float A[MAX][MAX], B[MAX][MAX];
    srand(time(NULL));
    int opcion, n=0, m=0, k=0;
    do{
        cout<<"Programa que realiza operaciones con matrices. Elija una operaci\xA2n:\n";
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
                while(!Captura(A, &n, &m, "Anm")){
					n=0;
					m=0;
				}
                while(!Captura(B, &n, &m, "Bnm"));
                break;
            case 3:
                cout<<"Necesario 2 matrices, una nxm y otra mxk";
                while(!Captura(A, &n, &m, "Anm")){
					n=0;
					m=0;
				}
                while(!Captura(B, &m, &k, "Bmk")) k=0;
                break;
            case 4:
            case 5:
                cout<<"Necesario una matriz de nxm";
                while(!Captura(A, &n, &m, "Anm")){
					n=0;
					m=0;
				}
                break;
            case 6:
            case 7:
                cout<<"Necesario una matriz de nxn";
                while(!Captura(A, &n, &n, "Ann")) n=0;
                break;
        }

        switch(opcion){
			case 1: Suma(A, B, n, m); break;
			case 2: Diferencia(A, B, n, m); break;
			case 3: Producto(A, B, n, m, k); break;
			case 4: ProductoE(A, n, m); break;
			case 5: Transpuesta(A, n, m); break;
			case 6: Inversa(A, n); break;
			case 7:	Determinante(A, n); break;
		}

        cout<<endl;
		system("PAUSE");
        system("CLS");
        n=0;
        m=0;
        k=0;
    }while(true);
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

bool Captura(float A[][MAX], int* n, int* m, char letra[])
{
    int opc, aux, k;
	do{
		cout<<"\n"<<'\xA8'<<"Desea capturar la matriz "<<letra[0]<<" por tecleado o por archivo?\n1)Teclado\t2)Archivo\n";
		cin>>opc;
	}while(opc!=1 && opc!=2);
	if(opc==1){
		if(!*n)
			do{
				cout<<letra[1]<<": ";
				cin>>*n;
			}while(*n<2 || *n>MAX);
		if(!*m)
			do{
				cout<<letra[2]<<": ";
				cin>>*m;
			}while(*m<2 || *m>MAX);

		do{
			cout<<'\xA8'<<"Desea capturar cada valor o generar al azar?\n1)Capturar\t2)Al azar\n";
			cin>>opc;
		}while(opc!=1 && opc!=2);
		if(opc==1){
			for(int i=0; i<*n; ++i)
        		for(int j=0; j<*m; ++j) {
					cout<<letra[0]<<"["<<i+1<<"]["<<j+1<<"]=";
					cin>>A[i][j];
				}
		}else for(int i=0; i<*n; ++i)
        	for(int j=0; j<*m; ++j) A[i][j]=rand()%5;

	}else{
		char archivo[20], line[100];
		cout<<"Nombre del archivo [Con extensi\xA2n]: ";
		cin>>archivo;
		ifstream salida(archivo);
		if(!salida.good()){
			cout<<"Error: El archivo no es v\xA0lido";
			return false;
		}
	    salida>>line;
	    for(k=0; line[k]!='\0'; ++k);
		for(int i=0; i<k; ++i)
			if((int)line[i]<45 || (int)line[i]>57 || line[i]== '/'){
    			cout<<"Error: el valor de "<<letra[1]<<" no se pudo capturar.";
				return false;
		 	}
		aux=CadenaAFloat(line, k);
		if(aux>MAX || aux<2){
			cout<<"Error: El numero de renglones es mayor que "<<MAX<<" o menor que 2";
			return false;
		}
		if(!*n) *n=aux;
		else if(*n!=aux){
			cout<<"Error: Dimesiones de matriz incorrectos.";
			return false;
		}
		salida>>line;
		for(k=0; line[k]!='\0'; ++k);
		for(int i=0; i<k; ++i)
			if((int)line[i]<45 || (int)line[i]>57 || line[i]== '/'){
    			cout<<"Error: el valor de "<<letra[2]<<" no se pudo capturar.";
				return false;
		 	}
		aux=CadenaAFloat(line, k);
		if(aux>MAX || aux<2){
			cout<<"Error: El numero de columnas es mayor que "<<MAX<<" o menor que 2";
			return false;
		}
		if(!*m) *m=aux;
		else if(*m!=aux){
			cout<<"Error: Dimesiones de matriz incorrectos.";
			return false;
		}
		int i=0, j=0, puntos[2]={};
		while(salida>>line){
			for(k=0; line[k]!='\0'; ++k);
			for(int l=0; l<k; ++l)
    			if(((int)line[l]<45 || (int)line[l]>57 || line[l]== '/') && line[l]!='e' || puntos[0]>1 || puntos[1]>2){
        			cout<<"Error: el valor en ["<<i+1<<"]["<<j+1<<"] no se pudo capturar. ";
					return false;
   			 	}
   			 	if(line[i]=='e') ++puntos[0];
   			 	if(line[i]=='.') ++puntos[1];
   			 	if(!puntos[0] & puntos[1]==2) ++puntos[0];
			A[i][j]=CadenaAFloat(line, k);
			++j;
			if(j==*m){
				j=0;
				++i;
			}
			puntos[0]=0;
			puntos[1]=0;
		}
		if(i!=*n || j){
			cout<<"Error: Incoherencia con respecto a dimensiones.";
			return false;
		}
	}

    Imprimir(A, *n, *m, letra[0]);
    return true;
}

void Imprimir(float A[][MAX], int n, int m, char letra)
{
    cout<<'\n'<<letra<<"=|";
    for(int i=0; i<n; ++i){
        for(int j=0; j<m; ++j) cout<<setw(6)<<setprecision(2)<<A[i][j]<<",";
        cout<<"  |\n  |";
    }
    cout<<"\b \n";
}

float CadenaAFloat(char cadena[], int k)
{
    float numero=0, exponente=0;
    int punto_i=0, exp_i=0, pe_i=0, cifra;
    bool posible=true;

    for(; cadena[exp_i]!='e' && exp_i<k; ++exp_i);
    for(; cadena[punto_i]!='.' && punto_i<exp_i; ++punto_i);
    for(pe_i=exp_i; cadena[pe_i]!='.' && pe_i<k; ++pe_i);

    if(cadena[0]!='.'){
        cifra=cadena[0]=='-' ? 0 : cadena[0]-48;
        numero+=cifra*pow(10, punto_i-1);
    }

    for(int i=1, n=punto_i-2; i<punto_i;++i,--n){
        cifra=cadena[i]-48;
        numero+=cifra*pow(10, n);
    }

    for(int i=punto_i+1, n=1; i<exp_i;++i,++n){
        cifra=cadena[i]-48;
        numero+=cifra/pow(10, n);
    }

    if(exp_i<k){
        if(cadena[exp_i+1]!='.'){
            cifra=cadena[exp_i+1]=='-' ? 0 : cadena[exp_i+1]-48;
            exponente+=cifra*pow(10, pe_i - exp_i-2);
        }
        for(int i=exp_i+2, n=pe_i-exp_i-3; i<pe_i;++i,--n){
            cifra=cadena[i]-48;
            exponente+=cifra*pow(10, n);
        }

        for(int i=pe_i+1, n=1; i<k;++i,++n){
            cifra=cadena[i]-48;
            exponente+=cifra/pow(10, n);
        }
    }

    if(cadena[0]=='-') numero*=-1;
    if(cadena[exp_i+1]=='-') exponente*=-1;
    numero*= pow(10, exponente);

    return numero;
}



