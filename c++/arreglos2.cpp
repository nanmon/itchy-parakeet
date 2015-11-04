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
    int n;

    srand(time(NULL));

    cout<<"Valor de n: ";
    cin>>n;
    int nums[n];
    cout<<"Números de 1 a "<<n<<" ordenados: ";
    for(int i=0; i<n; ++i){
        nums[i]=i+1;
        cout<<nums[i]<<", ";
    }

    cout<<"\nPares eliminados: ";

    for(int i=0; i<n; ++i){
        cout<<(nums[i]%2 == 0 ? 0 : nums[i])<<", ";
    }
    //system("PAUSE");
    return 0;
}
