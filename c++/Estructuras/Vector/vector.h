/**
    Autor: Nan Montaño
**/

#ifndef VECTOR_H_INCLUDED
#define VECTOR_H_INCLUDED

#include <iostream>

using std::istream;
using std::ostream;

class Vector{


    ///*leer
    friend istream& operator>>(istream&, Vector&);
    ///*imprimir
    friend ostream& operator<<(ostream&, const Vector&);
    ///*escalar*vector
    friend Vector operator*(float, const Vector &);

public:

    ///*Contructor
    explicit Vector(int);
    ///*constructor de copias
    Vector(const Vector&);
    ///*asignacion
    Vector& operator=(const Vector&);
    ///*destructor
    ~Vector();
    ///*ma facil
    float operator[](int i) const;
    ///*suma
    Vector operator+(const Vector&) const;
    ///resta
    Vector operator-(const Vector&) const;
    ///multiplicacion
    float operator*(const Vector&) const;
    ///vector*escalar
    Vector operator*(float) const;
    ///norma
    float magnitud() const;

private:
    int length;
    float *comp;
};

#endif // VECTOR_H_INCLUDED
