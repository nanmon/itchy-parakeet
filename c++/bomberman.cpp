/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<ctime>
#include<cmath>
#include<curses.h>
#include<iomanip>

#define BNUM 1

using namespace std;

class Bomber{
public:
    int x;
    int y;
    int pos;
    char control[5];
    Bomber(int=0);
    void operator+=(int[]);
};

Bomber::Bomber(int n):x(0),y(0), pos(0){
    control[0]='d';
    control[1]='s';
    control[2]='a';
    control[3]='w';
    control[4]='b';
}

void Bomber::operator+=(int m[])
{
    x+=x+m[0]<=8 && x+m[0]>=0? m[0] : 0;
    y+=y+m[1]<=8 && y+m[1]>=0? m[1] : 0;
}

/////////////////////
class World{
    int Arr[9][9];
    Bomber *bmans[BNUM];
public:
    World();
    int* operator[](int n){return Arr[n];}
    void operator>>(char);

} Mapita;

World::World()
{
    srand(time(NULL));
    for(int i=0; i<9; ++i)
        for(int j=0; j<9; ++j)
            Arr[i][j]=0;
    for(int i=1; i<9; i+=2)
        for(int j=1; j<9; j+=2)
            Arr[i][j]=1;
    bmans[0] = new Bomber(0);
    /*for(int i=0; i<9; ++i)
        for(int j=0; j<9; j+=1+i%2)
            if(!(rand()%3)) Arr[i][j]=2;*/
   Arr[bmans[0]->y][bmans[0]->x]=10;

}

void World::operator>>(char m){
    int i,j=5;
    for(i=0; i<BNUM && j>=5; ++i)
        for(j=0; j<5 && bmans[i]->control[j]!=m; ++j);
    --i;
    if(j<4){
        int mov[2]={round(cos(j*M_PI/2)), round(sin(j*M_PI/2))};
        if(i<BNUM && Arr[bmans[i]->y+mov[1]][bmans[i]->x+mov[0]]==0){
            Arr[bmans[i]->y][bmans[i]->x]=bmans[i]->pos;
            (*bmans[i])+= mov;
            Arr[bmans[i]->y][bmans[i]->x]=i+10;
            bmans[i]->pos=0;
        }
    }else if(j==4){
        bmans[i]->pos=-3;
    }else if(m==-1)
        for(i=0; i<9; ++i)
            for(j=0; j<9; ++j)
                if(Arr[i][j]<0) Arr[i][j]++;
}
///////////////////////

//////
ostream& operator<<(ostream &wout, World &mapi)
{
    for(int i=0; i<9; ++i){
        for(int j=0; j<9; ++j)
            wout<<setw(3)<<mapi[i][j];
        wout<<endl;
        }
    return wout;
}

int main()
{
    char m;
    do{
        time_t t=clock();
        do{
            cout<<Mapita;
            keypad(initscr(), true);
            timeout((1+t-clock())*1000);
            cbreak();
            noecho();
            m=getch();
            endwin();
            Mapita>>m;
            system("clear");
        }while(clock()-t==1);
        Mapita>>(char)-1;
    }while(true);

    //system("PAUSE");
    return 0;
}
