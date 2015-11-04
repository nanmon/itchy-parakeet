#ifndef NUMERICO_H_INCLUDED
#define NUMERICO_H_INCLUDED

#include<iostream>
#include<stdlib.h>
#include<fstream>

using namespace std;

void Biseccion(long double (*)(long double));
void ReglaFalsa(long double (*)(long double));
void PuntoFijo();
void NewtonRaphson(long double (*)(long double), long double (*)(long double));
void Secante(long double (*)(long double));

class Matriz;

ostream& operator<<(ostream&, const Matriz&);
ifstream& operator>>(ifstream&, Matriz&);

class Regresion;

class Matriz{
    friend class Gauss;
    friend class Gauss2;
    friend class GaussJordan;
    friend class Jacobi;
    friend class GaussSeidel;
    friend class FactorizacionLU;
    friend class Regresion;
    friend ostream& operator<<(ostream&, const Matriz&);
    friend ifstream& operator>>(ifstream&, Matriz&);
    friend ostream& operator<<(ostream &, Regresion);
public:
    Matriz(int=0, int=0);
    ~Matriz();
    Matriz(const Matriz&);
    void operator=(const Matriz&);
    static Matriz Cero(int, int);
    static Matriz Identidad(int);
    operator bool() const {return n && m ? true : false;}
    float* operator[](int i){ return arr[i];}
    Matriz operator*(Matriz);
    Matriz operator+(Matriz);
    Matriz operator-(Matriz);
    Matriz Trans() const;
    Matriz Inv() const;
    float Det() const;
    Matriz desaumentada() const;
    Matriz escalada() const;
private:
    float **arr;
    int n, m;
    void Eliminacion(float, int, float, int, int);
    void Permutacion(int, int);
};

class Gauss{
public:
    Gauss(Matriz);
    Matriz solucionar();


    void static main(){
        char archivo[50];
        Matriz g;
        cout<<"Eliminacion gaussiana simple"<<endl<<endl;
        cout<<"Formato del archivo:\n\nn m\na11 a12 .. a1m\na21 a22 .. a2m\n:\nan1 an2 .. anm\n";
        cout<<"\nArchivo: ";
        cin>>archivo;
        ifstream leer(archivo);
        leer>>g;
        Gauss gauss(g);
        cout<<"Solucion:\n"<<gauss.solucionar();

        cin.get();
    }
private:
    Matriz m, b, x;
};

class GaussJordan{
public:
    GaussJordan(Matriz);
    Matriz solucionar();
   void static main(){

        char archivo[50];
        Matriz g;
        cout<<"Gauss-Jordan"<<endl<<endl;
        cout<<"Formato del archivo:\n\nn m\na11 a12 .. a1m\na21 a22 .. a2m\n:\nan1 an2 .. anm\n";
        cout<<"\nArchivo: ";
        cin>>archivo;
        ifstream leer(archivo);
        leer>>g;
        GaussJordan gauss(g);
        cout<<"Solucion:\n"<<gauss.solucionar();

        cin.get();
    }
private:
    Matriz m, b, x;
};

class Gauss2{
public:
    Gauss2(Matriz);
    Matriz solucionar();
    void static main(){
        char archivo[50];
        Matriz g;
        cout<<"Eliminacion gaussiana con pivoteo parcial"<<endl<<endl;
        cout<<"Formato del archivo:\n\nn m\na11 a12 .. a1m\na21 a22 .. a2m\n:\nan1 an2 .. anm\n";
        cout<<"\nArchivo: ";
        cin>>archivo;
        ifstream leer(archivo);
        leer>>g;
        Gauss2 gauss(g);
        cout<<"Solucion:\n"<<gauss.solucionar();

        cin.get();
    }
private:
    Matriz m, b, x;
};

class GaussSeidel{
public:
    GaussSeidel(Matriz);
    Matriz Solucionar(float);
     void static  main()
    {
        Matriz A;

        char archivo[20];
        cout<<"Gauss-Seidel"<<endl<<endl;
        cout<<"Formato del archivo:\n\nn m\na11 a12 .. a1m\na21 a22 .. a2m\n:\nan1 an2 .. anm\n";
        cout<<"[La matriz debe ser diagonal dominante]"<<endl;
        cout<<"Nombre del archivo [Con extensi\xA2n]: ";
        cin>>archivo;
        ifstream salida(archivo);

        salida>>A;

        GaussSeidel j(A);
        cout<<j.Solucionar(0.01);

        cin.get();
    }
private:
    Matriz mat;
    Matriz soluciones;
};

class Jacobi{
public:
    Jacobi(Matriz);
    Matriz Solucionar(float);
    void static main()
    {
        Matriz A;

        char archivo[20];
        cout<<"Jacobi"<<endl<<endl;
        cout<<"Formato del archivo:\n\nn m\na11 a12 .. a1m\na21 a22 .. a2m\n:\nan1 an2 .. anm\n";
        cout<<"[La matriz debe ser diagonal dominante]"<<endl;
        cout<<"Nombre del archivo [Con extensi\xA2n]: ";
        cin>>archivo;
        ifstream salida(archivo);

        salida>>A;

        Jacobi j(A);
        cout<<j.Solucionar(0.01);

        cin.get();
    }
private:
    Matriz mat;
    Matriz soluciones;
};

class FactorizacionLU{
public:
    FactorizacionLU(Matriz);
    Matriz SolucionarL();
    static void main()
    {
        Matriz A;

        char archivo[20];
        cout<<"Factorizacion LU"<<endl<<endl;
        cout<<"Formato del archivo:\n\nn m\na11 a12 .. a1m\na21 a22 .. a2m\n:\nan1 an2 .. anm\n";
        cout<<"Nombre del archivo [Con extensi\xA2n]: ";
        cin>>archivo;
        ifstream salida(archivo);

        salida>>A;

        cout<<A<<endl;

        FactorizacionLU lu(A);
        cout<<"Solucion:\n"<<lu.SolucionarL();

        cin.get();
    }

private:
    Matriz a;
};

void operator>>(ifstream &, float **&);

class DifDiv{
    friend ostream& operator<<(ostream &fout, DifDiv d);
public:
    DifDiv(float*, int, float (*)(float));
    DifDiv(float**, int);
    float operator()(float);
    void add(float);
    void liberar();
    static void main(){

        float **x;
        char archivo[50];
        cout<<"Diferencias divididas de Newton"<<endl<<endl;
        cout<<"Formato del archivo:\n\nn\nx0 y0\nx1 y1\n...\nxn yn\n\n";
        cout<<"Nombre del archivo: ";
        cin>>archivo;
        ifstream leer(archivo);
        leer>>x;
        DifDiv f(x+1, x[0][0]);
        cout<<"f(x) = "<<f<<endl;
        float x2;
        cout<<"x: ";
        cin>>x2;
        cout<<"f("<<x2<<") = "<<f(x2)<<endl;
        f.liberar();
        cin.get();

    }
private:
    float (*f)(float);
    int n;
    float **dd;
};

ostream& operator<<(ostream &fout, DifDiv d);

class Lagrange{
    friend ostream& operator<<(ostream &fout, Lagrange f);
public:
    Lagrange(float*, int, float (*)(float));
    Lagrange(float**, int);
    float operator()(float);
    void add(float);
    void liberar();
    static void main(){

        float **x;
        char archivo[50];
        cout<<"Polinomio de Lagrange"<<endl<<endl;
        cout<<"Formato del archivo:\n\nn\nx0 y0\nx1 y1\n...\nxn yn\n\n";
        cout<<"Nombre del archivo: ";
        cin>>archivo;
        ifstream leer(archivo);
        leer>>x;
        Lagrange f(x+1, x[0][0]);
        float x2;
        cout<<"f(x) = "<<f<<endl;
        cout<<"x: ";
        cin>>x2;
        cout<<"f("<<x2<<") = "<<f(x2)<<endl;
        f.liberar();
        cin.get();

    }
private:
    float (*f)(float);
    int n;
    float *x, *coef;
};

ostream& operator<<(ostream &fout, Lagrange f);

class Regresion{
    friend ostream& operator<<(ostream &, Regresion);
public:
    Regresion(float**, int, int);
    float operator()(float);
    static void main(){
        float **x;
        char archivo[50];
        cout<<"Regresion"<<endl<<endl;
        cout<<"Formato del archivo:\n\nn\nx0 y0\nx1 y1\n...\nxn yn\n\n";
        cout<<"Nombre del archivo: ";
        cin>>archivo;
        ifstream leer(archivo);
        leer>>x;
        int grado;
        cout<<"Grado del polinomio: ";
        cin>>grado;
        Regresion f(x+1, x[0][0], grado);
        cout<<"Polinomio:\n"<<f;
        float x2;
        cout<<"x: ";
        cin>>x2;
        cout<<"f("<<x2<<") = "<<f(x2);
        cin.get();
    }
private:
    Matriz polinomio;
};

ostream& operator<<(ostream &, Regresion);

class Trapecios{
    friend ostream& operator<<(ostream&, Trapecios);
public:
    Trapecios(long double (*f)(long double), float intervalo[2], int n);
    static void main(long double (*f)(long double)){
        float inter[2] = {1, 2};
        int part;
        cout<<"Se calculara la integral de 3x^3+3x+1 de 1 a 2 con el numero de particiones especificado\n";
        cout<<"Numero de particiones: ";
        cin>>part;
        Trapecios I(f, inter, part);
        cout<<I;

        cin.get();
    }
private:
    float valor;
};

ostream& operator<<(ostream&, Trapecios);

class Simpson{
public:
    Simpson(long double (*f)(long double), long double x[2], int n);
    long double getVal();

    static void main(long double (*f)(long double)){
        long double inter[2] = {1, 2};
        int part;
        cout<<"Se calculara la integral de 3x^3+3x+1 de 1 a 2 con el numero de particiones especificado\n";
        do{
            cout<<"Numero de particiones [n>=3]: ";
            cin>>part;
        }while(part<3);
        Simpson I(f, inter, part);
        cout<<I.getVal();

        cin.get();
    }
private:
    long double integral;
};


#endif // NUMERICO_H_INCLUDED
