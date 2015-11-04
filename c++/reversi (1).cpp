/*
Nombre:
Autor: Montaño Valdez L. Roberto
Descripcion:
Fecha:
*/

#include<iostream>
#include<cstdlib>
#include<cstdio>

using namespace std;

void Imprimir(int[][8], int, int);
void Cursor(int[][8], int*, int*, int*);

int main()
{
    int Tablero[8][8]={};
    Tablero[3][3] = 1;
    Tablero[3][4] = 2;
    Tablero[4][3] = 2;
    Tablero[4][4] = 1;
    int x=5, y=3;
    int player = 1;
    int opcion;
    
	cout<<"**********************************************\n"<<endl;
    cout<<"*************       REVERSI      *************\n"<<endl;
	cout<<"*************        Menu        *************\n"<<endl;
    cout<<"*************   Instrucciones: 1 *************\n"<<endl;
    cout<<"*************        Play: 2     *************\n"<<endl;
    cout<<"**********************************************\n"<<endl;
	
	cout<<"              Eliga Una Opcion                          "<<endl;
	cin>>opcion;
	
	switch(opcion)	{
		
		case 1:
			system("cls");
			cout<<"************   INSTRUCCIONES   ************\n"<<endl;
			cout<<"Se juega con wasd, con enter se coloca una ficha"<<endl;
			break;
			
			
			
			
			
			case 2:
			
			
			system("cls");
			do{
        		Imprimir(Tablero, x, y);
        		Cursor(Tablero, &player, &x, &y);
    		}while(true);
    		//system("PAUSE");
    		return 0;
}
}
void Imprimir(int T[][8], int x, int y)
	
	 
	{
	
		system("cls");
		int i, j;
		for(i=0; i<y; ++i){
			for(j=0; j<8; ++j) cout<<T[i][j]<<" ";
			cout<<endl;
		}
		for(j=0; j<x; ++j) cout<<T[i][j]<<" ";
		cout<<"_ ";
		for(j=x+1; j<8; ++j) cout<<T[i][j]<<" ";
		cout<<endl;
		for(i=y+1; i<8; ++i){
			for(j=0; j<8; ++j) cout<<T[i][j]<<" ";
			cout<<endl;
		}
	}

void Cursor(int T[][8], int *player, int *x, int *y)
{
	char mover;
	bool posible=false;
	cin.get(mover);
	*x = mover=='a' ? (*x>0 ? *x-1 : 0) : (mover=='d' ? (*x<7 ? *x+1 : 7) : *x);
	*y = mover=='w' ? (*y>0 ? *y-1 : 0) : (mover=='s' ? (*y<7 ? *y+1 : 7) : *y);
	if(mover=='m' && !T[*y][*x]){
		posible = false;
		for(int k=-1; k<=1; ++k)
			for(int l=-1; l<=1; ++l)
				if(T[*y+k][*x+l]==3-*player && (l || k)){
					for(int m=2; *y+m*k<8 && *y+m*k>=0 && *x+m*l<8 && *x+m*l>=0; ++m)
						if(T[*y+m*k][*x+m*l]==*player){
							for(int n=1; n<=m; ++n) T[*y+(m-n)*k][*x+(m-n)*l]=*player;
							posible=true;
							m=8;
						}else if(!T[*y+m*k][*x+m*l]) m=8;
				}
		if(posible) *player = 3-*player;
		
	}
			   
		
		
	}
	



