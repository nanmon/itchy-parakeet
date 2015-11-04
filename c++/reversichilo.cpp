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

bool StillMoves(int [][8], int[], int);

int main()
{
    int Tablero[8][8]={};
    Tablero[3][3] = 1;
    Tablero[3][4] = 2;
    Tablero[4][3] = 2;
    Tablero[4][4] = 1;
    int x=5, y=3, k, r, c, m ,n, player = 1, scores[2] = {2, 2}, i, j;
    char mover;
    bool posible=false;
    do{
        system("clear");
        cout<<"Turno de jugador "<<player<<endl;
        for(i=0; i<y; ++i){
            for(j=0; j<8; ++j) cout<<Tablero[i][j]<<" ";
            cout<<endl;
        }
        for(j=0; j<x; ++j) cout<<Tablero[i][j]<<" ";
        cout<<"_ ";
        for(j=x+1; j<8; ++j) cout<<Tablero[i][j]<<" ";
        cout<<endl;
        for(i=y+1; i<8; ++i){
            for(j=0; j<8; ++j) cout<<Tablero[i][j]<<" ";
            cout<<endl;
        }
        cout<<"\nScore:\nP1: "<<scores[0]<<" P2: "<<scores[1]<<endl;
        cin.get(mover);
        x = mover=='a' ? (x>0 ? x-1 : 0) : (mover=='d' ? (x<7 ? x+1 : 7) : x);
        y = mover=='w' ? (y>0 ? y-1 : 0) : (mover=='s' ? (y<7 ? y+1 : 7) : y);
        if(mover=='m' && !Tablero[y][x]){
            for(k=0; k<8; ++k){
                r=round(sin(k*M_PI/4));
                c=round(cos(k*M_PI/4));
                if(Tablero[y+r][x+c]==3-player)
                    for(m=2; (y+m*r+1)%9 && (x+m*c+1)%9; ++m)
                        if(Tablero[y+m*r][x+m*c]==player){
                            scores[player-1]+=m-1;
                            scores[2-player]-=m-1;
                            for(n=1; n<=m; ++n) Tablero[y+(m-n)*r][x+(m-n)*c]=player;
                            posible=true;
                            break;
                        }
            }
            if(posible){
                ++scores[player-1];
                player = 3-player;
                posible=false;
            }
        }
    }while(StillMoves(Tablero, scores, player));
    cout<<"yey";
    cin>>mover;
    //system("PAUSE");
    return 0;
}

bool StillMoves(int T[][8], int S[], int p)
{
    if(S[0]+S[1]>=64) return false;
    int k, r, c, m, i, j;
    for(i=0; i<8; ++i)
        for(j=0; j<8; ++j)
            if(!T[i][j])
                for(k=0; k<8; ++k){
                    r=round(sin(k*M_PI/4));
                    c=round(cos(k*M_PI/4));
                    if(T[i+r][j+c]==3-p)
                        for(m=2; (i+m*r+1)%9 && (j+m*c+1)%9; ++m)
                            if(T[i+m*r][j+m*c]==p) return true;
                }
    return false;
}
