/*
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>
#include<iomanip>
#include<fstream>
#include"numerico.h"

using namespace std;

////////////Operaciones de pivoteo///////PRIVATE/////////
void Matriz::Eliminacion(float a, int r1, float b, int r2, int r)
{
    for(int i=0; i<m; ++i)
        arr[r][i] = arr[r1][i]*a + arr[r2][i]*b;

}

void Matriz::Permutacion(int a, int b)
{
    float *pntaux = arr[a];
    arr[a] = arr[b];
    arr[b] = pntaux;
}
////////////Constructures, Destructor & =////////////
Matriz::Matriz(int _n, int _m):n(_n), m(_m)
{
    arr = new float* [n];
    for(int i=0; i<n; ++i){
        arr[i] = new float [m];
        for(int j=0; j<m; ++j) arr[i][j] = 0;
    }
}

Matriz::~Matriz()
{
    for(int i=0; i<n; ++i) delete [] arr[i];
    delete [] arr;
}

Matriz::Matriz(const Matriz &A):n(A.n), m(A.m)
{
    arr = new float* [n];
    for(int i=0; i<n; ++i){
        arr[i] = new float [m];
        for(int j=0; j<m; ++j) arr[i][j] = A.arr[i][j];
    }

}

void Matriz::operator=(const Matriz &A)
{
    if(n!=A.n){
        for(int i=0; i<n; ++i) delete [] arr[i];
        delete [] arr;
        n=A.n;
        m=A.m;
        arr = new float* [n];
        for(int i=0; i<n; ++i) arr[i] = new float [m];
    }else if(m!=A.m){
        for(int i=0; i<n; ++i){
            delete [] arr[i];
            arr[i] = new float [A.m];
        }
        m=A.m;
    }
    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) arr[i][j] = A.arr[i][j];

}


Matriz Matriz::Cero(int n, int m)
{
    Matriz a(n,m);
    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) a.arr[i][j] = 0;
    return a;
}

Matriz Matriz::Identidad(int n)
{
    Matriz a(n,n);
    for(int i=0; i<n; ++i){
        for(int j=0; j<n; ++j) a.arr[i][j] = 0;
        a.arr[i][i] = 1;
    }
    return a;
}
//////////////////Metodos de la Clase///////////////////////////

Matriz Matriz::operator*(Matriz a){
    if(m!=a.n) throw;
    Matriz r(n, a.m);
    for(int i=0; i<n; ++i){
        for(int j=0; j<a.m; ++j){
            for(int k=0; k<m; ++k) r[i][j]+=arr[i][k]*a[k][j];
        }
    }
    return r;
}

Matriz Matriz::operator+(Matriz a){
    if(n!=a.n || m!=a.m) throw;
    Matriz r(n, m);
    for(int i=0; i<n; ++i){
        for(int j=0; j<m; ++j) r[i][j] = arr[i][j] + a[i][j];
    }
    return r;
}

Matriz Matriz::operator-(Matriz a){
    if(n!=a.n || m!=a.m) throw;
    Matriz r(n, m);
    for(int i=0; i<n; ++i){
        for(int j=0; j<m; ++j) r[i][j] = arr[i][j] - a[i][j];
    }
    return r;
}

Matriz Matriz::Trans() const
{
    Matriz R(m, n);
    for(int i=0; i<m; ++i)
        for(int j=0; j<n; ++j) R.arr[i][j] = arr[j][i];
    return R;
}

Matriz Matriz::Inv() const
{
    try{
        if(!this->Det()){
            throw "Error: Matriz no invertible.";
        }

        Matriz R=*this, I=Matriz::Identidad(n);

        for(int i=0; i<n; ++i){
            for(int j=0; j<i; ++j){
                I.Eliminacion(1, i, -R.arr[i][j], j, i);
                R.Eliminacion(1, i, -R.arr[i][j], j, i);
            }
            if(!R.arr[i][i]){
                int l=1;
                for(; !R.arr[i+l][i];++l);
                R.Permutacion(i, i+l);
                I.Permutacion(i, i+l);
                --i;
            }else{
                I.Eliminacion(1/R.arr[i][i], i, 0, 0, i);
                R.Eliminacion(1/R.arr[i][i], i, 0, 0, i);
            }
        }

        for(int i=n-2; i>=0; --i){
            for(int j=n-1; j>i; --j){
                I.Eliminacion(1, i, -R.arr[i][j], j, i);
                R.Eliminacion(1, i, -R.arr[i][j], j, i);
            }
        }
        return I;
    }catch(char const *err){
        cout<<err<<endl;
        Matriz R;
        return R;
    }
}

float Matriz::Det() const
{
    try{
        if(n!=m) throw "Error: Determinante de matrices no cuadradas no definido.";
        if(n==2)
            return arr[0][0]*arr[1][1]-arr[0][1]*arr[1][0];
        if(n>2){
            float det=0;
            Matriz D;
            for(int i=0; i<n; ++i){
                D=Matriz::Cero(n-1,n-1);
                for(int j=0; j<n-1; ++j)
                    for(int k=0; k<n-1; ++k) D.arr[j][k]=arr[j+1][k>=i?k+1:k];
                det+=arr[0][i]*D.Det();
            }
            return det;
        }else return arr[0][0];
    }catch(char const *err){
        cout<<err<<endl;
        return 0;
    }
}

Matriz Matriz::desaumentada() const {
    Matriz r(n, n);
    for(int i=0; i<n;++i)
        for(int j=0; j<n; ++j) r[i][j] = this->arr[i][j];
    return r;
}

Matriz Matriz::escalada() const {
    Matriz r= *this;
    for(int i=0; i<n; ++i){
        float mayor=0;
        for(int j=0; j<n; ++j) if(r[i][j]>mayor) mayor=r[i][j];
        if(mayor==0){
            cout<<"Error: Renglon de ceros.\n";
            return *this;
        }
        for(int j=0; j<m; ++j) r[i][j]/=mayor;
    }
    return r;
}

//////////////////Matriz BFF/////////////////////////////////

ostream& operator<<(ostream& matrixout, const Matriz &A)
{
    if(A)
        for(int i=0; i<A.n; ++i){
            matrixout<<'|';
            for(int j=0; j<A.m; ++j) matrixout<<setw(6)<<A.arr[i][j];
            matrixout<<setw(6)<<"|"<<endl;
        }
    return matrixout;
}

ifstream& operator>>(ifstream& matrixin, Matriz &A)
{
    try{
        if(!matrixin.good()) throw;
        int n,m;
        matrixin>>n;
        matrixin>>m;
        A = Matriz::Cero(n, m);
        for(int i=0; i<n; ++i){
            for(int j=0; j<m; ++j){
                matrixin>>A.arr[i][j];
            }
        }
    }catch(...){
        throw "Archivo invalido.";
    }
    return matrixin;
}
