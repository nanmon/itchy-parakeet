#include <iostream>
#include <stdio.h>
#include <windows.h>
#include <conio2.h>
#include <ctime>
#include <cstdlib>
#include <iomanip>


using namespace std;

#define COLUMNAS 10
#define FILAS 10
#define SIN_BOMBA -2
#define BOMBA -1
#define SIN_JUGAR -3
#define MARCADA -4

#define ESTADO_PERDEDOR 0
#define ESTADO_GANADOR  1
#define ESTADO_EN_JUEGO 2

enum {
    ESQ_INF_DER = 217,
    ESQ_SUP_IZQ,
    ESQ_SUP_DER = 191,
    ESQ_INF_IZQ,
    HORIZONTAL = 196,
    VERTICAL = 179
};

struct Casilla{
    int numero;
    bool jugada=false;
    bool marcada=false;
};


void IniciarJuego(Casilla[][COLUMNAS]);
void IniciaArr(Casilla[][COLUMNAS]);
void AgregaBombas(int,Casilla[][COLUMNAS]);
void EstablecerNumeros(Casilla[][COLUMNAS]);
void Print(int,int,Casilla[][COLUMNAS]);
void JugadaEnGrupo(int,int, int &, Casilla[][COLUMNAS]);
int Contar2(int,int,Casilla[][FILAS]);
void PrintAll(Casilla[][COLUMNAS]);
int ContarJugadas(Casilla[][COLUMNAS]);
bool GetMenu();
void Marco(int , int , int, int);
void ComoMoverse();
void Reglas();


int main(int argc, char** argv) {

    Casilla buscaminas[FILAS][COLUMNAS];

    bool QuiereJugar;

    do{
        textbackground(BLACK);
        textcolor(WHITE);
        QuiereJugar = GetMenu();
        if (QuiereJugar) IniciarJuego(buscaminas);
        cin.ignore();
    }while(QuiereJugar);
}

/////////////////////////////////////////////////////////////////////////

void IniciarJuego(Casilla B[][FILAS]){
    int njugadas=0;
    int estado;
    int nbombas = -1;
    while( nbombas < 0 || nbombas > 70 ){
        gotoxy(30,20);
        cout << "Numero de bombas: ";
        cin >> nbombas;
    }

    int opcion,x=0,y=0;
    estado = ESTADO_EN_JUEGO;
    IniciaArr(B);

    srand ( time(NULL) );
    AgregaBombas(nbombas,B);
    EstablecerNumeros(B);

    system("cls");
    Marco(24,4,FILAS*3,FILAS);
    while( estado == ESTADO_EN_JUEGO){
        Print(x,y,B);
        njugadas = ContarJugadas(B);
        if( njugadas == ( FILAS*COLUMNAS - nbombas) ){
            estado = ESTADO_GANADOR;
        }
        opcion = getch();


        if (opcion == 'w' && x - 1 >= 0) --x;
        if (opcion == 's' && x + 1 <= COLUMNAS-1) ++x;
        if (opcion == 'a' && y - 1 >= 0) --y;
        if (opcion == 'd' && y + 1 <= FILAS-1) ++y;

        if (opcion == 'i'){
                B[x][y].jugada = true;

                if( B[x][y].numero == BOMBA){
                    estado = ESTADO_PERDEDOR;
                }else{
                    JugadaEnGrupo(x,y, estado,B);
                }
        }

        if(opcion == 'o'){
            if( B[x][y].marcada == true){
                B[x][y].jugada = false;
                if(B[x][y].numero != BOMBA) B[x][y].numero = Contar2(x,y,B);
                B[x][y].marcada = false;
            }else{

                B[x][y].marcada=MARCADA;
            }


        }
    }
    textbackground(BLACK);
        system("cls");

    if( estado == ESTADO_PERDEDOR){
        textcolor(RED);
        gotoxy(35,1);
        cout << "Perdiste" << endl;
        textcolor(WHITE);
        gotoxy(35,3);
        cout << "Solucion: " << endl;
        Marco(24,4,FILAS*3,FILAS);
        PrintAll(B);
    }else{
        gotoxy(33,2);
        textcolor(YELLOW);
        cout << "\xAD \xAD GANASTE !!" << endl;
        textcolor(WHITE);
        Marco(24,4,FILAS*3,FILAS);
        PrintAll(B);

    }
}

//////////////////////////////////////////////////////////////

void IniciaArr(Casilla B[][COLUMNAS]){
    for(int i = 0; i < FILAS; i++){
        for(int j = 0; j < COLUMNAS; j++){
            B[i][j].numero = SIN_BOMBA;
            B[i][j].jugada = false;
        }
    }
}

//////////////////////////////////////////////////////////////////

void AgregaBombas(int nbombas,Casilla B[][COLUMNAS]){
    int x,y;
    for (int i = 0; i < nbombas;++i){
        x = rand()%FILAS;
        y = rand()%COLUMNAS;
        if (B[x][y].numero != BOMBA)
        B[x][y].numero = BOMBA;
    }
}

//////////////////////////////////////////////////////////////////

void EstablecerNumeros(Casilla B[][FILAS]){
    int i,j, numero;
    for( i = 0; i < FILAS; i++){
        for(j = 0; j < COLUMNAS; j++){
            if( B[i][j].numero == SIN_BOMBA){
                numero = Contar2(i,j, B);
                B[i][j].numero = numero;
            }
        }
    }
}

//////////////////////////////////////////////////////////////////

void Print(int x,int y,Casilla B[][COLUMNAS]){
    ComoMoverse();
    textcolor(BLACK);
    textbackground(LIGHTGRAY);

    int i,j;
    for( i = 0; i < FILAS; ++i){
        gotoxy(25,5+i);
        for(j = 0; j < COLUMNAS; ++j){
            if(i==x && j==y){
                 textbackground(DARKGRAY);
            }

            if( B[i][j].jugada ){

                if( B[i][j].numero == BOMBA){
                    cout << " * ";
                }else
                    if(B[i][j].marcada == false) cout << " " << B[i][j].numero << " ";
            }else
                if( B[i][j].marcada == true){
                     textcolor(BLUE);
                     cout<<" "<<char(169)<<" ";
                     textcolor(WHITE);
                }
                else cout << " - ";

            textbackground(LIGHTGRAY );
            textcolor(BLACK);
        }
        cout << endl;
    }
}

//////////////////////////////////////////////////////////////////

void JugadaEnGrupo(int x, int y, int &estado,Casilla B[][COLUMNAS]){
    if(B[x][y].numero == 0){
        for(int i = x-1; i < x+2; i++){
            for(int j = y-1; j < y+2; j++){
                if(i>=0 && i< FILAS && j>=0 && j<COLUMNAS){
                    if( B[i][j].numero == 0 && !B[i][j].jugada){
                        B[i][j].jugada = true;
                        JugadaEnGrupo(i,j, estado,B);
                    }else{
                        B[i][j].jugada = true;

                    }

                }
            }
        }
    }

}

//////////////////////////////////////////////////////////////////

int Contar2(int x, int y, Casilla B[][COLUMNAS]){
    int nbombas = 0;
    for(int i = x-1; i < x+2; i++){
        for(int j = y-1; j < y+2; j++){
            if( i >= 0 && i < FILAS && j >= 0 && j < COLUMNAS ){
                if( B[i][j].numero == BOMBA){
                    nbombas++;
                }
            }
        }
    }
    return nbombas;
}

//////////////////////////////////////////////////////////////////

void PrintAll(Casilla B[][COLUMNAS]){

    for(int i = 0; i < FILAS; i++){
        gotoxy(25,5+i);
        for(int j = 0; j < COLUMNAS; j++){
            //textcolor(BLACK);
           // textbackground(LIGHTGRAY );
            if( B[i][j].numero == BOMBA){
                textcolor(RED);
                cout << " * ";
                textcolor(BLACK);
            }else{
                cout << " " << B[i][j].numero << " ";
            }
            B[i][j].marcada=false;
        }
        cout << endl;
    }
}

//////////////////////////////////////////////////////////////////

void Reglas()
{
    system("cls");
    cout<<setw(50)<<"Reglas del juego: "<<endl<<endl;
    cout<<"El juego consiste en destapar todas las casillas de la pantalla que no oculten \nuna mina.\n\n";
    cout<<"Algunas casillas tienen un n\xA3mero, este n\xA3mero indica las minas que suman todas las casillas que lo rodean.";
    cout<<" As\xA1, si una casilla tiene el n\xA3mero 3, significa \nque de las ocho casillas que hay alrededor";
    cout<<" (si no es en una esquina o borde) \nhay 3 con minas y 5 sin minas.\n\n";
    cout<<"Si se descubre una casilla sin n\xA3mero indica que ninguna de las casillas vecinas";
    cout<<"tiene mina y estas se descubren autom\xA0ticamente.\n\n";
    cout<<"Si se descubre una casilla con una mina se pierde la partida.\n\n";
    cout<<"Se puede poner una marca en las casillas que el jugador piensa que hay minas \npara ayudar a descubrir la que est\xa0n cerca.\n\n";
    system("pause");//detenemos el programa para que el usuario pueda leer las instyrucciones
    system("cls");//limpiamos pantalla para regresar al menu
}

//////////////////////////////////////////////////////////////////

void ComoMoverse(){
    textcolor(WHITE);
    textbackground(BLACK);
    gotoxy(2,5);
    cout<<"Teclas para moverse:";

    Marco(9,8,3,2);
    gotoxy(11,9);
    cout<<'W';
    Marco(9,12,3,2);
    gotoxy(11,13);
    cout<<'S';
    Marco(4,12,3,2);
    gotoxy(6,13);
    cout<<'A';
    Marco(14,12,3,2);
    gotoxy(16,13);
    cout<<'D';

    textcolor(WHITE);
    textbackground(BLACK);
    gotoxy(63,5);
    cout<<"Tecla para";
    gotoxy(57,7);
    cout<<"destapar:";
    Marco(59,10,3,2);
    gotoxy(61,11);
    cout<<'I';
    textcolor(WHITE);
    textbackground(BLACK);
    gotoxy(70,7);
    cout<<"marcar:";
    Marco(71,10,3,2);
    gotoxy(73,11);
    cout<<'O';
}

//////////////////////////////////////////////////////////////////

void Marco(int x, int y, int ancho, int alto)
{
    textcolor(BLACK);
    textbackground(LIGHTGRAY);
    gotoxy(x, y);
    cout<<char(ESQ_SUP_IZQ);
    for(int i=0; i<ancho; i++) cout<<char(HORIZONTAL);
    cout<<char(ESQ_SUP_DER)<<endl;
    for(int i=1; i<=alto; i++){
        gotoxy(x, y+i);
        cout<<char(VERTICAL);
        for(int j=0; j<ancho; j++)cout<<' ';
        cout<<char(VERTICAL)<<endl;
    }
    gotoxy(x, y+alto+1);
    cout<<char(ESQ_INF_IZQ);
    for(int i=0; i<ancho; i++) cout<<char(HORIZONTAL);
    cout<<char(ESQ_INF_DER)<<endl<<endl;

}

//////////////////////////////////////////////////////////////////

int ContarJugadas(Casilla B[][COLUMNAS]){
    int numero = 0;
    for(int i = 0; i < FILAS; i++){
        for(int j = 0; j < COLUMNAS; j++){
            if ( B[i][j].jugada){
                numero++;
            }
        }
    }
    return numero;
}

//////////////////////////////////////////////////////////////////

bool GetMenu(){
    int n;
    do{
        gotoxy(30,16);
        cout<<"2. Reglas."<<endl;
        gotoxy(30,17);
        cout << "1. Jugar Buscaminas." << endl;
        gotoxy(30,18);
        cout << "0. Salir.";
        char opt[30];
        bool cadena_valida=true;

       do{
            cadena_valida = true;

            gotoxy(30,19);
            cout << "Opci\xa2n: ";
            cin.getline(opt, 30,'\n');

            if (  (opt[0] == char(48) && opt[1]=='\0') || (opt[0] == char(49) && opt[1]=='\0') || (opt[0] == char(50) && opt[1]=='\0')){
            } else cadena_valida = false;
        }while(!cadena_valida);
        n = opt[0] - 48;
        if (n == 1) return true;
        if (n == 0) return false;
        if(n==2) Reglas();
    }while(n==2);

}
