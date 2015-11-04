/**
	Autor: L. Roberto Montaño Valdez
*/

#include<cstdlib>
#include<iostream>
#include"Vector/vector.h"

using namespace std;

int main(){
    try{
        Vector v(3), a(3);
        cin>>v>>a;
        cout<<v<<endl<<a<<endl;
        cout<<a+v<<endl;
        cout<<a-v<<endl;
        cout<<a*v<<endl;
        cout<<a*5<<endl;
        cout<<5*a<<endl;
        cout<<a.magnitud()<<endl;
    }catch(int e){
        if(e == 0) cout<<"No hay memoria desponible"<<endl;
        if(e == 1) cout<<"No se pudo realizar la operacion: los vectores son de distinta dimension"<<endl;
    }
	//system("PAUSE");
	return 0;
}
