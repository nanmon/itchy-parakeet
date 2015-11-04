/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
using namespace std;

#define MAX 20
typedef char tipo;

void Union(tipo [], tipo [], int, int);
void Interseccion(tipo [], tipo [], int, int);
void Complemento(tipo [], tipo [], int, int);
void Diferencia(tipo [], tipo [], int, int);
bool Contencion(tipo [], tipo [], int, int);
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

    if(Contencion(A, B, cardA, cardB) || Contencion(B, A, cardB, cardA)){
        bool EsAMayor = Contencion(A, B, cardA, cardB);
        tipo mayor[MAX] = EsAMayor ? A[] : B[];
        tpo menor[MAX] = EsAMayor ? B[] : A[];
        int cardMyr = EsAMayor ? cardA : card B
        int cardMnr = EsAMayor = cardB : card A;
        char myrNombre= EsAMayor ? 'A' : 'B';
        char mnrNombre= EsAMayor ? 'B' : 'A';

        cout<<"El conjunto "<<mayrNombre<<" contiene al conjunto "<<mnrNombre<<".\n";
        cout<<"Tomando "<<myrNombre<<" como universo:\n";
        Conmplemento(menor, mayor, cardmnr, cardmayor);
    }else{
        cout<<"Ningun conjunto contiene al otro\n"
        Union(A, B, card A, cardB);
        Interseccion(A, B, cardA, cardB);
        Diferencia(A, B, cardA, cardB);
    }

    //system("PAUSE");
    return 0;
}

bool Pertenece(tipo Conj[], int card, tipo elem)
{
    for(int j=0;j<card;++j)
        if(elem==Conj[j]) return true;
    return false;
}

void Union(tipo A[], tipo B[], int cardA, int cardB)
{
    tipo U[2*MAX];
    int cardU = cardA;
    for(int i=0; i<cardA; ++i) U[i]=A[i];
    for(int i=0; i<cardB; ++i)
        if(!Pertenece(A, cardA, B[i])) U[cardU++]=B[i];

     cout<<"\n\n{ ";
    for(int i=0;i<cardA;++i) cout<<A[i]<<",";
    cout<<"\b } U ";

    cout<<"{ ";
    for(int i=0;i<cardB;++i) cout<<B[i]<<",";
    cout<<"\b } = ";

    cout<<"{ ";
    for(int i=0;i<cardU;++i) cout<<U[i]<<",";
    cout<<"\b }";
}

void Interseccion(tipo A[], tipo B[], int cardA, int cardB)
{
    tipo I[2*MAX];
    int cardI=0;
    for(int=0;i<cardA;++i)
        if(Pertenece(B, cardB, A[i])) I[cardI++]=A[i];

     cout<<"\n\n{ ";
    for(int i=0;i<cardA;++i) cout<<A[i]<<",";
    cout<<"\b } · ";

    cout<<"{ ";
    for(int i=0;i<cardB;++i) cout<<B[i]<<",";
    cout<<"\b } = ";

    cout<<"{ ";
    for(int i=0;i<cardI;++i) cout<<I[i]<<",";
    cout<<"\b }";

}

void Complemento(tipo A[], tipo U[], int cardA, int cardU)
{
    tipo Ac[2*MAX];
    int cardAc=0;
    for(int=0;i<cardU;++i)
        if(!Pertenece(A, cardA, U[i])) Ac[cardAc++]=U[i];

     cout<<"\n\n{ ";
    for(int i=0;i<cardU;++i) cout<<U[i]<<",";
    cout<<"\b } c ";

    cout<<"{ ";
    for(int i=0;i<cardA;++i) cout<<A[i]<<",";
    cout<<"\b } complemento = ";

    cout<<"{ ";
    for(int i=0;i<cardAC;++i) cout<<Ac[i]<<",";
    cout<<"\b }";

}

void Diferencia(tipo A[], tipo B[], int cardA, int cardB)
{
    tipo D[2*MAX];
    int cardD=0;
    for(int=0;i<cardA;++i)
        if(!Pertenece(B, cardB, A[i])) D[cardD++]=A[i];

     cout<<"\n\n{ ";
    for(int i=0;i<cardA;++i) cout<<A[i]<<",";
    cout<<"\b } \\ ";

    cout<<"{ ";
    for(int i=0;i<cardB;++i) cout<<B[i]<<",";
    cout<<"\b } = ";

    cout<<"{ ";
    for(int i=0;i<cardD;++i) cout<<D[i]<<",";
    cout<<"\b }";
}

bool Contencion(tipo A[], tipo B[], int cardA, int cardB)
{
    for(int=0;i<cardA;++i)
        if(!Pertenece(B, cardB, A[i])) return false;
    return true;
}
