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

class Matriz{
    friend ostream& operator<<(ostream&, const Matriz&);
    friend istream& operator>>(istream&, Matriz&);
    friend Matriz operator*(float, const Matriz&);
public:
    explicit Matriz(int=1, int=1);
    ~Matriz();
    Matriz(const Matriz&);
    void operator=(const Matriz&);
    void Ceros();
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
    float **arr;
    int n, m;
    void Eliminacion(float, int, float, int, int);
    void Permutacion(int, int);
    static const int y=9;
};

//int Matriz::y = 5;

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
    arr = new (nothrow) float* [n];
    if(arr == NULL){
        throw false;
    }
    for(int i=0; i<n; ++i){
        arr[i] = new (nothrow) float [m];
        if(arr[i] == NULL) throw false;
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
    if(arr == NULL) throw false;
    for(int i=0; i<n; ++i){
        arr[i] = new float [m];
        if(arr[i] == NULL) throw false;
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
        if(arr == NULL) throw false;
        for(int i=0; i<n; ++i){
            arr[i] = new float [m];
            if(arr[i] == NULL) throw false;
        }
    }else if(m!=A.m){
        for(int i=0; i<n; ++i){
            delete [] arr[i];
            arr[i] = new float [A.m];
            if(arr[i] == NULL) throw false;
        }
        m=A.m;
    }
    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) arr[i][j] = A.arr[i][j];

}

void Matriz::Ceros()
{
    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) arr[i][j] = 0;
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
    if(n!=A.n || m!=A.m){
        throw "Error: Suma de matrices de distinta dimension no esta definido";
    }
    Matriz R(n, m);
    for(int i=0; i<n; ++i)
        for(int j=0; j<m; ++j) R.arr[i][j] = arr[i][j] + A.arr[i][j];
    return R;
}

Matriz Matriz::operator-(const Matriz &A) const
{
    return (*this)+(-A);
}

Matriz Matriz::operator*(const Matriz &A) const
{
    if(m!=A.n){
        throw "Error: No se puede realizar la multiplicación";
    }
    Matriz R(n, A.m);
    for(int i=0; i<n; ++i)
        for(int j=0; j<A.m; ++j)
            for(int k=0; k<m; ++k) R.arr[i][j]+= arr[i][k] * A.arr[k][j];
    return R;
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

    if(n!=m || !this->Det()){
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
}

float Matriz::Det() const
{
    if(n!=m) throw "Error: Determinante de matrices no cuadradas no definido.";
    if(n==2)
        return arr[0][0]*arr[1][1]-arr[0][1]*arr[1][0];
    if(n>2){
        float det=0;
        Matriz D(n-1,n-1);
        for(int i=0; i<n; ++i){
            D.Ceros();
            for(int j=0; j<n-1; ++j)
                for(int k=0; k<n-1; ++k) D.arr[j][k]=arr[j+1][(i+1+k)%n];
            det+=arr[0][i]*D.Det();
        }
        return det;
    }else return arr[0][0];
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
            for(int j=0; j<A.m; ++j) matrixout<<setw(6)<<A.arr[i][j];
            matrixout<<setw(6)<<"|"<<endl;
        }
    return matrixout;
}

istream& operator>>(istream& matrixin, Matriz &A)
{

    for(int i=0; i<A.n; ++i)
        for(int j=0; j<A.m; ++j){
        cout<<"["<<i+1<<"]["<<j+1<<"]: ";
        matrixin>>A.arr[i][j];
        }
}

int main()
{

    int n, m;
    cout<<"Tamaño de la matriz A (nxm)"<<endl;
    do{
        cout<<"n: ";
        cin>>n;
    }while(n<=0);

    do{
        cout<<"m: ";
        cin>>m;
    }while(m<=0);

    Matriz A(n, m);

    cout<<"Tamaño de la matriz B (nxm)"<<endl;

    do{
        cout<<"n: ";
        cin>>n;
    }while(n<=0);

    do{
        cout<<"m: ";
        cin>>m;
    }while(m<=0);

    Matriz B(n, m);

    //Pruebas:
    cout<<"Capturar matriz A:"<<endl;
    cin>>A;
    cout<<"Matriz capturada A: "<<endl;
    cout<<"Capturar matriz B:"<<endl;
    cin>>B;
    cout<<"Matriz capturada B: "<<endl;
    try{
        try{
            Matriz C = A+B;
            cout<<"A+B ="<<endl;
            cout<<C;
            C = A-B;
            cout<<"A-B ="<<endl;
            cout<<C;
        }catch(const char* e){
            cout<<e<<endl;
        }

        try{
            Matriz C = A*B;
            cout<<"A*B ="<<endl;
            cout<<C;
        }catch(const char* e){
            cout<<e<<endl;
        }

        float esc;
        cout<<"Capturar escalar: ";
        cin>>esc;
        Matriz C = A*esc;
        cout<<"A*"<<esc<<" ="<<endl;
        cout<<C;
        C = esc*B;
        cout<<esc<<"*B ="<<endl;
        cout<<C;

        C = A.Trans();
        cout<<"A transpuesta ="<<endl;
        cout<<C;

        try{
            Matriz C = A.Inv();
            cout<<"A inversa ="<<endl;
            cout<<C;
        }catch(const char* e){
            cout<<e<<endl;
        }
    }catch(bool e){
        cout<<"No hay memoria disponible"<<endl;
    }catch(...){
        cout<<"Error desconocido";
    }

    //system("PAUSE");
    return 0;
}
