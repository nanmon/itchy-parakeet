/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<iomanip>

using namespace std;

class Fraccion{

public:
    Fraccion(long int _a, long int _b):a(_a), b(_b) {Reduccion();}
    Fraccion(int _a):a(_a), b(1) {}
    Fraccion(float);
    Fraccion operator+(Fraccion f){Fraccion s(a*f.b+f.a*b, b*f.b); return s;}
    Fraccion operator-(Fraccion f){Fraccion s(a*f.b-f.a*b, b*f.b); return s;}
    Fraccion operator*(Fraccion f){Fraccion s(a*f.a, b*f.b); return s;}
    Fraccion operator/(Fraccion f){Fraccion s(a*f.b, b*f.a); return s;}
    operator int(){}
private:
    long int a, b;
    void Reduccion();
};

void Fraccion::Reduccion()
{
    for(int i=2; i*i<=(a > b ? a : b); ++i)
        if(!a%i && !b%i){
            a/=i;
            b/=i;
            Reduccion();
            break;
        }
}

Fraccion::Fraccion(float a)
{
    int cifras[20], i, x, punto, fl;
    float decim;
    punto = log10(a);
    fl = (int)a;
    decim = a - fl;
    for(int i=0; i<20; ++i){

    }

}

////////////////////////////////////
class Matriz{
    friend ostream& operator<<(ostream&, const Matriz&);
    friend istream& operator>>(istream&, Matriz&);
    friend Matriz operator*(float, const Matriz&);
public:
    Matriz(int=0, int=0);
    ~Matriz();
    Matriz(const Matriz&);
    void operator=(const Matriz&);
    void operator=(int);
    void operator()(int, int);
    operator bool() const {return n && m ? true : false;}
    Matriz operator-() const;
    Matriz operator+(const Matriz&) const;
    Matriz operator-(const Matriz&) const;
    Matriz operator*(const Matriz&) const;
    Matriz operator*(float) const;
    Matriz Trans() const;
    Matriz Inv() const;
    float Det() const;
private:
    Fraccion **arr;
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
    arr = new Fraccion* [n];
    for(int i=0; i<n; ++i){
        arr[i] = new Fraccion [m];
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
    arr = new Fraccion* [n];
    for(int i=0; i<n; ++i){
        arr[i] = new Fraccion [m];
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
        arr = new Fraccion* [n];
        for(int i=0; i<n; ++i) arr[i] = new Fraccion [m];
    }else if(m!=A.m){
        for(int i=0; i<n; ++i){
            delete [] arr[i];
            arr[i] = new Fraccion [A.m];
        }
        m=A.m;
    }
    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) arr[i][j] = A.arr[i][j];

}

void Matriz::operator=(int cero)
{
    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) arr[i][j] = cero;
}

void Matriz::operator()(int _n, int _m)
{
    Matriz A(_n,_m);
    *this = A;
}
//////////////////Metodos de la Clase///////////////////////////

Matriz Matriz::operator-() const
{
    Matriz R(n, m);
    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) R.arr[i][j] = -arr[i][j];
    return R;
}

Matriz Matriz::operator+(const Matriz &A) const
{
    try{
        if(n!=A.n || m!=A.m){
            throw "Error: Matrices de distinta dimension";
        }
        cout<<"holi";
        Matriz R(n, m);
        for(int i=0; i<n; ++i)
            for(int j=0; j<m; ++j) R.arr[i][j] = arr[i][j] + A.arr[i][j];
        return R;
    }catch(char const *err){
        cout<<err<<endl;
        Matriz R;
        return R;
    }
}

Matriz Matriz::operator-(const Matriz &A) const
{
    return (*this)+(-A);
}

Matriz Matriz::operator*(const Matriz &A) const
{
    try{
        if(m!=A.n){
            throw "Error: No se puede realizar la multiplicación";
        }
        Matriz R(n, A.m);
        for(int i=0; i<n; ++i)
            for(int j=0; j<A.m; ++j)
                for(int k=0; k<m; ++k) R.arr[i][j]+= arr[i][k] * A.arr[k][j];
        return R;
    }catch(char const *err){
        cout<<err<<endl;
        Matriz R;
        return R;
    }
}

Matriz Matriz::operator*(float E) const
{
    Matriz R(n, m);
    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) R.arr[i][j] = arr[i][j]*E;
    return R;
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

        Matriz R=*this, I(n, n);
        for(int i=0; i<n; ++i){
            I.arr[i][i]=1;
        }

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
        if(n!=m) throw "Error: La matriz no es cuadrada.";
        if(n==2)
            return arr[0][0]*arr[1][1]-arr[0][1]*arr[1][0];
        if(n>2){
            float det=0;
            Matriz D(n-1,n-1);
            for(int i=0; i<n; ++i){
                D=0;
                for(int j=0; j<n-1; ++j)
                    for(int k=0; k<n-1; ++k) D.arr[j][k]=arr[j][(i+k)%n];
                det+=arr[n-1][(n-1+i)%n]*(float)D;
            }
            return det;
        }else return arr[0][0];
    }catch(char const *err){
        cout<<err<<endl;
        return 0;
    }
}


//////////////////Matriz BFF/////////////////////////////////
Matriz operator*(float E, const Matriz &A)
{
    return A*E;
}

ostream& operator<<(ostream& matrixout, const Matriz &A)
{
    if(A)
        for(int i=0; i<A.n; ++i){
            matrixout<<'|';
            for(int j=0; j<A.m; ++j) matrixout<<setw(4)<<A.arr[i][j];
            matrixout<<"    |"<<endl;
        }
    return matrixout;
}

istream& operator>>(istream& matrixin, Matriz &A)
{
    if(!A){
        int _n, _m;
        cout<<"Numero de renglones: ";
        matrixin>>_n;
        cout<<"Numero de columnas: ";
        matrixin>>_m;
        A(_n, _m);
    }
    for(int i=0; i<A.n; ++i)
        for(int j=0; j<A.m; ++j){
        cout<<"["<<i+1<<"]["<<j+1<<"]: ";
        matrixin>>A.arr[i][j];
        }
}

int main()
{
    char archivo[30];
    Matriz R, B;

    cin>>R;
    B=R.Inv();
    cout<<R.Det();
    cout<<B;
    //system("PAUSE");
    return 0;
}
