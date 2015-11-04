/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<cstdio>
#include<cmath>

using namespace std;



class Tablero{
    friend ostream& operator<<(ostream&, Tablero &);
    friend istream& operator>>(istream&, Tablero &);
public:
    Tablero();
    int SePuede(int, int=-1, int=-1);
    operator bool();
private:
    int arr[8][8];
    int x;
    int y;
    int marcador[2];
    int jugador;
    class moves{
        int *a;
        int *b;
        int na;
        int nb;
    } Movimientos;
} Reversi;

Tablero::Tablero():y(3), x(5), jugador(1)
{
    for(int i=0; i<8; ++i)
        for(int j=0; j<8; ++j) arr[i][j]=0;

    arr[3][3] = 1;
    arr[3][4] = 2;
    arr[4][3] = 2;
    arr[4][4] = 1;

    marcador[0] = 2;
    marcador[1] = 2;
}

int Tablero::SePuede(int st, int i, int j)
{
    if(i<0 && j<0){
        i=y;
        j=x;
    }
    int k, r, c, m;
    if(!arr[i][j])
        for(k=st; k<8; ++k){
            r=round(sin(k*M_PI/4));
            c=round(cos(k*M_PI/4));
            if(arr[i+r][j+c]==3-jugador && (i+r+1)%9 && (j+c+1)%9)
                for(m=2; (i+m*r+1)%9 && (j+m*c+1)%9; ++m)
                    if(arr[i+m*r][j+m*c]==jugador)
                        return k*10+m;

            }
    return 0;
}

Tablero::operator bool()
{
    if(marcador[0]+marcador[1]>=64) return false;
    for(int i=0; i<8; ++i)
        for(int j=0; j<8; ++j)
            if(SePuede(0, i, j)) return true;
    return false;
}

istream& operator >>(istream &rin, Tablero &T)
{
    int start=0, dir, magn, compy, compx;
    char mover;
    bool posible=false;
    rin.get(mover);
    T.x = mover=='a' ? (T.x>0 ? T.x-1 : 0) : (mover=='d' ? (T.x<7 ? T.x+1 : 7) : T.x);
    T.y = mover=='w' ? (T.y>0 ? T.y-1 : 0) : (mover=='s' ? (T.y<7 ? T.y+1 : 7) : T.y);
    if(mover=='m'){
        while(start=T.SePuede(start)){
            magn=start%10;
            dir=start/10;
            compy=round(sin(dir*M_PI/4));
            compx=round(cos(dir*M_PI/4));
            for(int n=1; n<magn; ++n){
                T.arr[T.y+(magn-n)*compy][T.x+(magn-n)*compx]=T.jugador;
            }
            T.marcador[T.jugador-1]+=magn-1;
            T.marcador[2-T.jugador]-=magn-1;
            start=dir;
            posible=true;
        }
        if(posible){
            T.arr[T.y][T.x]=T.jugador;
            ++T.marcador[T.jugador-1];
            T.jugador = 3-T.jugador;
        }
    }
    return rin;
}

ostream& operator<<(ostream &rout, Tablero &T)
{
    int i, j;
    system("clear");
    rout<<"Turno de jugador "<<T.jugador<<endl;
    for(i=0; i<T.y; ++i){
        for(j=0; j<8; ++j) rout<<T.arr[i][j]<<" ";
        rout<<endl;
    }
    for(j=0; j<T.x; ++j) rout<<T.arr[i][j]<<" ";
    rout<<"_ ";
    for(j=T.x+1; j<8; ++j) rout<<T.arr[i][j]<<" ";
    rout<<endl;
    for(i=T.y+1; i<8; ++i){
        for(j=0; j<8; ++j) rout<<T.arr[i][j]<<" ";
        rout<<endl;
    }
    rout<<"\nScore:\nP1: "<<T.marcador[0]<<" P2: "<<T.marcador[1]<<endl;
    return rout;
}

int main()
{
    do{
        cout<<Reversi;
        cin>>Reversi;
    }while(Reversi);
    cout<<"yey";
    //system("PAUSE");
    return 0;
}
