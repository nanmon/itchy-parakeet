/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>

using namespace std;

int main()
{
    double n = (double)12345678901/(100000000000-1);
    n = n<0 ? -n : n;
    int cifras[30], i=0;
    cout<<n<<endl;
    double diez=10;

    for(;int(n*diez)%10>=0; diez*=10,++i){
        cifras[i]=int(n*diez)%10;
        cout<<cifras[i]<<',';
    }
    /*int length=1; psb_l=1, j=1, perd=0;
    bool seek=false;
    for(; j<i; ++j){
        if(seek){

        }else{
            if(cifras[j]==cifras[j-1])
                ++psb_l;
            else {
                length=psb_l;
                seek=true;
            }
        }
    }*/
    cout<<endl<<i<<endl;


    //system("PAUSE");
    return 0;
}
