/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<iomanip>
#include<fstream>
#include<cmath>

using namespace std;

class Matriz{
    friend class FactorizacionLU;
    friend ostream& operator<<(ostream&, const Matriz&);
    friend ifstream& operator>>(ifstream&, Matriz&);
public:
    Matriz(int=0, int=0);
    ~Matriz();
    Matriz(const Matriz&);
    void operator=(const Matriz&);
    static Matriz Cero(int, int);
    static Matriz Identidad(int);
    operator bool() const {return n && m ? true : false;}
    Matriz operator*(Matriz);
    float* operator[](int i){ return arr[i];}
    Matriz Trans() const;
    Matriz Inv() const;
    float Det() const;
private:
    float **arr;
    int n, m;
    void Eliminacion(float, int, float, int, int);
    void Permutacion(int, int);
};
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
    if(this->m!=a.n) return NULL;
    Matriz r(this->n, a.m);
    for(int i=0; i<n; ++i){
        for(int j=0; j<a.m; ++j){
            r[i][j]=0;
            for(int k=0; k<m; ++k){
                r[i][j]+=arr[i][k]*a[k][j];
            }
        }
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


//////////////////Matriz BFF/////////////////////////////////

ostream& operator<<(ostream& matrixout, const Matriz &A)
{
    if(A)
        for(int i=0; i<A.n; ++i){
            matrixout<<'|';
            for(int j=0; j<A.m; ++j) matrixout<<setprecision(3)<<setw(6)<<A.arr[i][j];
            matrixout<<"  |"<<endl;
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
        cout<<"Archivo invalido."<<endl;
        cin.get();
        exit(0);
    }
    return matrixin;
}

class FactorizacionLU{
public:
    FactorizacionLU(Matriz);
    Matriz SolucionarL(float);
private:
    Matriz a, b;
};

FactorizacionLU::FactorizacionLU(Matriz _m):a(_m.n,_m.n), b(_m.n, 1){
    for(int i=0, j=0; i<_m.n; ++i, j=0){
        for(; j<_m.n; ++j) a[i][j]=_m[i][j];
        b[i][0]=_m[i][j];
    }
}

Matriz FactorizacionLU::SolucionarL(float err_rel){
    Matriz u=a, l=Matriz::Identidad(a.n);
    for(int i=0; i<a.n; ++i){
        for(int j=i+1; j<a.n; ++j){
            l[j][i]=u[j][i]/u[i][i];
            for(int k=0; k<a.n; ++k){
                u[j][k]-=u[i][k]*l[j][i];
            }
        }
    }

    cout<<l<<endl<<u<<endl<<l*u;

    return NULL;
}

int main()
{
    Matriz A;

    char archivo[20];
    cout<<"Nombre del archivo [Con extensi\xA2n]: ";
    cin>>archivo;
    ifstream salida(archivo);

    salida>>A;

    cout<<A<<endl;

    FactorizacionLU lu(A);
    lu.SolucionarL(0.01);

    //system("PAUSE");
    return 0;
}
