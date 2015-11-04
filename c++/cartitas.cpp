/*
  Nombre: Cartita
  Autor: Montaño Valdez L. Roberto
  Fecha: 22/02/14 09:38
  Descripción: Programa que imprime cartas de baraja al azar
*/

#include <iostream>
#include <stdio.h>
#include <windows.h>
//#include <conio.h>

using namespace std;

enum {
    ESQ_INF_DER = 217,
    ESQ_SUP_IZQ,
    ESQ_SUP_DER = 191, 
    ESQ_INF_IZQ,
    HORIZONTAL = 196,
    VERTICAL = 179,
    NUMCERO = 48
};

void Cuadrar(int, int, int, int);
void MueveCursor(unsigned short,unsigned short);
void Cartear(int, int);

int xAux, yAux;

int main() {
	srand(time(NULL));
	//textbackground(GREEN);
	//clrscr();
    //textbackground(WHITE);
	for(int i=0; i<200; i++){
	    //textcolor(BLACK);
	    xAux=rand()%67+1;
	    yAux=rand()%11+1;
		int numero = rand()%13+1;
		int palo = rand()%4 + 3;
		Cartear(numero, palo);
		//tiempo de espera entre cada carta (20vo de segundo)
		int timer = clock();
		int delay = CLOCKS_PER_SEC/20;
		while(clock() < timer+delay);
	}
	MueveCursor(1, 24);
	
   system("PAUSE");
   return 0;
}

void MueveCursor(unsigned short x,unsigned short y) 
{
	HANDLE handle = GetStdHandle(STD_OUTPUT_HANDLE);
	COORD coord = {x-1,y-1}; 
	SetConsoleCursorPosition(handle,coord);
}

void Cartear(int numero, int palo){
	 
	 char cartaNum[3] = "A\0";
	 
	 switch(numero){
	 	case 1: cartaNum[0]='A'; break;
	 	case 10:
			 cartaNum[0]=char(NUMCERO +1);
			 cartaNum[1]=char(NUMCERO);
			 break;
		case 11: cartaNum[0]='J'; break;
		case 12: cartaNum[0]='Q'; break;
		case 13: cartaNum[0]='K'; break;
		default: cartaNum[0]=char(NUMCERO +numero);
	 };
	 int x=xAux, y=yAux;
	 Cuadrar(x, y, 11, 11);
	 //int color = palo<=4 ? RED : BLACK;
  	 //textcolor(color);
	 MueveCursor(++x, ++y);
	 cout<<cartaNum;
	 MueveCursor(x, ++y);
	 cout<<char(palo);
	 //girar?
	 MueveCursor(x=xAux+11, y=yAux+11);
	 if(numero==10) cout<<'\b';
	 cout<<cartaNum;
	 MueveCursor(x, --y);
	 cout<<char(palo);
	 
	 x=xAux+4;
	 y=yAux+3;
	 //coord(x,y) justo a la izquierda del simbolo en la esquina superior izquierda
	 MueveCursor(x,y);
  	 int numSimb = numero>10 ? 1 : numero;
  	 /*x=numero se simbolos; y=numero de simbolos en centro:
      x|1 2 3 4 5 6 7 8 9 10
      y|1 2 3 0 1 0 1 2 1 2
    */
	 int enCentro = numSimb==6 ? 0 : numSimb <=5 ? numSimb%4 : 2-numSimb%2;
	 int enLados = (numSimb-enCentro)/2;
	 //caso especial al acomodar los simbolos con el 7 y el 10
	 float fixer = numSimb == 7 ? 0.5 : numSimb == 10 ? 2 : 1;
	 int fixer2 = numSimb == 10 ? -3 : 0;
	 /*
       busque una funcion (matematica) de la que conosco todo el dominio y rango,
       que depende del numero de simbolos en la columna, y el indice al imprimir cada simbolo.
     */
	 int m = (enLados*enLados -7*enLados +14);
	 int b = - 3*enLados*enLados/2 +19*enLados/2 -16;
	 
	 for(int i=1;i<=enLados;i++){
	     //posicion es una funcion f(enLados, i) que devuelve la altura de cada figura en las columnas:
 		   int posicion = m*i+b ;
 		   MueveCursor(x, y+posicion);
 		   cout<<char(palo);
	 }
	 x+=2;
	 
	 for(int i=1; i<=enCentro; i++){
	 		 
	 	int posicion = fixer*2*i +2 -enCentro + fixer2;
 	 	MueveCursor(x, y+posicion);
	    cout<<char(palo);
	 }
	 x+=2;
	 
	 for(int i=1;i<=enLados;i++){
	    
 		   int posicion = m*i+b;
 		   MueveCursor(x, y+posicion);
 		   cout<<char(palo);
	 }
}

void Cuadrar(int x, int y, int ancho, int alto)
{
    MueveCursor(x, y);
    cout<<char(ESQ_SUP_IZQ);
    for(int i=0; i<ancho; i++) cout<<char(HORIZONTAL);
    cout<<char(ESQ_SUP_DER)<<endl;
    for(int i=1; i<=alto; i++){
        MueveCursor(x, y+i);
        cout<<char(VERTICAL);
        for(int j=0; j<ancho; j++)cout<<' ';  
        cout<<char(VERTICAL)<<endl;
    }
    MueveCursor(x, y+alto+1);
    cout<<char(ESQ_INF_IZQ);
    for(int i=0; i<ancho; i++) cout<<char(HORIZONTAL);
    cout<<char(ESQ_INF_DER)<<endl<<endl;
}
