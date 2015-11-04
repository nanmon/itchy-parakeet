/*
    Autor: Montaño Valdez L. Roberto

*/

#include <iostream>
#include <cstdlib>
#include "numerico.h"
//#include "factDirecta.cpp"

using namespace std;

long double f(long double x){
    return 3*x*x*x*x*x*x + 3*x +1;
}

long double df(long double x){
    return 9*x*x +3;
}

int main()
{
    int opcion;
    do{
        cout << "Elija una opcion." << endl;
        cout<<"1) Solucion de ecuaciones no lineales"<<endl;
        cout<<"2) Solucion de sistemas de ecuaciones"<<endl;
        cout<<"3) Interpolacion"<<endl;
        cout<<"4) Regresion"<<endl;
        cout<<"5) Integracion numerica"<<endl;
        cout<<"0) Salir"<<endl;
        cin>>opcion;
        system("clear");
        try{
            switch(opcion){
                case 1:
                    do{
                        cout<<"\tSolucion de ecuaciones no lineales"<<endl;
                        cout<<"1) Biseccion"<<endl;
                        cout<<"2) Regla Falsa"<<endl;
                        cout<<"3) Punto Fijo"<<endl;
                        cout<<"4) Newton-Raphson"<<endl;
                        cout<<"5) Secante"<<endl;
                        cout<<"0) Volver al menu"<<endl;
                        cin>>opcion;
                        switch(opcion){
                            case 1: Biseccion(f); opcion=0; break;
                            case 2: ReglaFalsa(f); opcion=0; break;
                            case 3: PuntoFijo(); opcion=0; break;//ajksbdlaksjbfdjlaksbjdbfljkeba
                            case 4: NewtonRaphson(f, df); opcion=0; break;
                            case 5: Secante(f);  opcion=0;
                        }
                    }while(opcion!=0);
                    opcion=1;
                    break;
                case 2:
                    do{
                        cout<<"\tSolucion de sistemas de ecuaciones"<<endl;
                        cout<<"1) Eliminacion Gaussiana Simple"<<endl;
                        cout<<"2) Eliminacion Gaussiana con Pivoteo"<<endl;
                        cout<<"3) Gauss-Jordan"<<endl;
                        cout<<"4) Jacobi"<<endl;
                        cout<<"5) Gauss-Seidel"<<endl;
                        cout<<"6) Factorizacion LU"<<endl;
                        cout<<"0) Volver al menu"<<endl;
                        cin>>opcion;
                        switch(opcion){
                            case 1: Gauss::main(); opcion=0; break;
                            case 2: Gauss2::main(); opcion=0; break;
                            case 3: GaussJordan::main(); opcion=0; break;
                            case 4: Jacobi::main(); opcion=0; break;
                            case 5: GaussSeidel::main(); opcion=0; break;
                            case 6: FactorizacionLU::main();  opcion=0;
                        }
                    }while(opcion!=0);
                    opcion=1;
                    break;
                case 3:
                    do{
                        cout<<"\tInterpolacion"<<endl;
                        cout<<"1) Diferencias divididas de Newton"<<endl;
                        cout<<"2) Lagrange"<<endl;
                        cout<<"0) Volver al menu"<<endl;
                        cin>>opcion;
                        switch(opcion){
                            case 1: DifDiv::main(); opcion=0; break;
                            case 2: Lagrange::main(); opcion=0; break;
                        }
                    }while(opcion!=0);
                    opcion=1;
                    break;
                case 4:
                    Regresion::main();
                    break;
                case 5:
                    do{
                        cout<<"\tIntegracion numerica"<<endl;
                        cout<<"1) Trapecios"<<endl;
                        cout<<"2) Simpson"<<endl;
                        cout<<"0) Volver al menu"<<endl;
                        cin>>opcion;
                        switch(opcion){
                            case 1: Trapecios::main(f); opcion=0; break;
                            case 2: Simpson::main(f); opcion=0; break;
                        }
                    }while(opcion!=0);
                    opcion=1;

            }
        }catch(char const* err){
            cout<<err<<endl;
            cin.get();
        }
    }while(opcion!=0);
    cout<<"Bye bye";
    return 0;
}
