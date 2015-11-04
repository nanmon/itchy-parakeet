/*
  Name:
  Author: IRC
  Date: 11/03/14 14:22
  Description:
*/

#include <iostream>
#include <cstdlib>
using namespace std;

#define MAX 20
typedef char tipo;
bool Pertenece(tipo [], int, tipo);

int main()
{
    tipo A[MAX],B[MAX],U[2*MAX];
    int cardA,cardB,cardU;

    do{
        cout<<"Cardinalidad de A: ";
        cin>>cardA;
    }while(cardA<0 || cardA>MAX);

    cout<<"Introduce los "<<cardA<<" elementos del conjunto A\n";
    for(int i=0;i<cardA;){
        cout<<"Elemento "<<i+1<<": ";
        cin>>A[i];
        if(!Pertenece(A,i,A[i])) ++i;
        else cout<<"Elemento repetido, introd\xA3 \bcelo de nuevo...\n";
    }

    cout<<endl;
    do{
        cout<<"Cardinalidad de B: ";
        cin>>cardB;
    }while(cardB<0 || cardB>MAX);

    cout<<"Introduce los "<<cardB<<" elementos del conjunto B\n";
    for(int i=0;i<cardB;){
        cout<<"Elemento "<<i+1<<": ";
        cin>>B[i];
        if(!Pertenece(B,i,B[i])) ++i;
        else cout<<"Elemento repetido, introd\xA3 \bcelo de nuevo...\n";
    }

    //Uniendo los conjuntos A y B
	for(int i=0;i<cardA;++i) U[i]=A[i];
    cardU=cardA;
    for(int i=0;i<cardB;++i)
    	if(!Pertenece(A,cardA,B[i])) U[cardU++]=B[i];


    cout<<"\n\n{ ";
    for(int i=0;i<cardA;++i) cout<<A[i]<<",";
    cout<<"\b } U ";

    cout<<"{ ";
    for(int i=0;i<cardB;++i) cout<<B[i]<<",";
    cout<<"\b } = ";

    cout<<"{ ";
    for(int i=0;i<cardU;++i) cout<<U[i]<<",";
    cout<<"\b }";


    cout<<endl;
    system("pause");
    return 0;
}


bool Pertenece(tipo Conj[], int card, tipo elem)
{
    for(int j=0;j<card;++j)
        if(elem==Conj[j]) return true;
    return false;
}
