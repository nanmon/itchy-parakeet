/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<ctime>

using namespace std;

int main()
{
    int n;

    srand(time(NULL));

    cout<<"Valor de n: ";
    cin>>n;
    cout<<"Números de 1 a "<<n<<" ordenados: ";
    int nums[n];
    for(int i=0; i<n; ++i){
        nums[i]=i+1;
        cout<<nums[i]<<", ";
    }
    cout<<endl;

    for(int i=0; i<n*5; ++i){
        int r1 = rand()%(int(n/2+1));
        int r2 = rand()%(int(n/2)+1) + (n-int(n/2+1));

        int aux = nums[r1];
        nums[r1] = nums[r2];
        nums[r2] = aux;
    }
    cout<<"Números desordenados: ";
    for(int i=0; i<n; ++i) cout<<nums[i]<<", ";

    cout<<endl;

    //system("PAUSE");
    return 0;
}
