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

    cout<<"\nPrimos desde 2 hasta "<<n<<": ";

    for(int i=1; nums[i]*nums[i]<=n;++i){
        if(nums[i]!=0)
            for(int j=2; j<=n/nums[i]; ++j) nums[j*nums[i]-1] = 0;
    }
    for(int i=1; i<n; ++i)
        if(nums[i]!=0) cout<<nums[i]<<", ";

    //system("PAUSE");
    return 0;
}
