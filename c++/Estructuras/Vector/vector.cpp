/**
	Autor: L. Roberto Montaño Valdez
**/

#include<cstdlib>
#include<iostream>
#include<cmath>
#include<new>
#include"vector.h"

using std::cout;
using std::cin;
using std::endl;
using std::nothrow;

///*********CONSTRUCTORES***********//
Vector::Vector(int n):length(n){
    comp = new (nothrow) float[n];
    if(comp == NULL){
        throw 0;
    }
}

Vector::Vector(const Vector& v):length(-1), comp(NULL){
    *this = v;
}

Vector& Vector::operator=(const Vector& v){
    if(this!=&v){
        if(length!=v.length){
            length = v.length;
            if(!comp) delete [] comp;
            comp = new (nothrow) float[length];
            if(!comp) throw 0;
        }
        for(int i=0; i<length; ++i) comp[i] = v[i];
    }
    return *this;
}

Vector::~Vector(){
    delete[] comp;
}

///************OPERACIONES**********///
float Vector::operator[](int i) const{
    return comp[i];
}

Vector Vector::operator+(const Vector &b) const{
    if(this->length!=b.length) throw 1;
    Vector r(length);
    for(int i=0; i<length; ++i) r.comp[i] = comp[i] + b[i];
    return r;
}

Vector Vector::operator*(float b) const{
    Vector r(length);
    for(int i=0; i<length; ++i) r.comp[i] = b*comp[i];
    return r;
}

Vector Vector::operator-(const Vector &b) const{
    if(this->length!=b.length) throw 1;
    Vector r(length);
    for(int i=0; i<length; ++i) r.comp[i] = comp[i] - b[i];
    return r;
}

float Vector::operator*(const Vector &b) const{
    if(this->length!=b.length) throw 1;
    float r=0;
    for(int i=0; i<length; ++i) r+=comp[i]*b[i];
    return r;
}

float Vector::magnitud() const{
    float sum=0;
    for(int i=0; i<length; ++i) sum+= comp[i]*comp[i];
    return sqrt(sum);
}

///*///////////LEER/////////
istream& operator>>(istream& vin, Vector& v){
    for(int i=0; i<v.length; ++i){
        cout<<"Componente #"<<i+1<<": ";
        vin>>v.comp[i];
    }
    return vin;
}

///*////////////IMPRIMIR/////////////////
ostream& operator<<(ostream& vout, const Vector& v){
    vout<<"(";
    for(int i=0; i<v.length; ++i){
        vout<<v[i]<<", ";
    }
    vout<<"\b\b)";
    return vout;
}

///*////////////MULTIPLICACION CONMUTATIVA//////////
Vector operator*(float b, const Vector &v) {
    return v*b;
}
