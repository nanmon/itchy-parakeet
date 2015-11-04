/*
  Nombre: Jorge Arturo Carvajal Siller
  Programa: Clase Cadena
  Fecha: 2/11/2015
  Descripcion: Implementa una clase cadena para que el usuario la pueda usar.
*/

#include<iostream>
#include<cstdlib>
#include <cstring>
#include <stdio.h>
using namespace std;
////////////////////////////////Excepciones
class Excepcion{
public:
    Excepcion(char *m){strcpy(mensaje, m);}
    char *getMensaje(){return mensaje;}

private:
    char mensaje[100];
};

class NoHayMemoria: public Excepcion{
public:
    NoHayMemoria():Excepcion("No hay memoria disponible"){}
};

class IndiceNoValido: public Excepcion{
public:
    IndiceNoValido():Excepcion("El indice no fue valido."){}
};
//////////////////////////////Clase Cadena


class Cadena{
   friend ostream& operator << (ostream &, const Cadena &);
   friend istream & operator >> (istream &, Cadena &);
public:

   Cadena(char* C =  NULL);
   Cadena(const Cadena &);
   ~Cadena(){delete [] cadena;}
   void operator +=(Cadena);
   Cadena(int);
   bool operator !() const {return !(cadena[0] == '\0');}
   bool operator == (Cadena) const;
   char & operator [](int );
   char operator[](int) const;
   bool operator!=(Cadena C) const{return !(*this == C);}
   Cadena& operator = (const Cadena &);
   int longitudCadena(){return tam;}
   Cadena subCadena(int = 0, int = 0);
   void convertirMinusculas();
   void convertirMayusculas();
private:
   char * cadena;
   int tam;

};
///////////////////////////////////////////////////Constructor normal
Cadena::Cadena(char* C):tam(0){
   if(C == NULL){
      cadena = new (nothrow) char[1];
      if(cadena == NULL) throw NoHayMemoria();
      cadena[0] = '\0';
   }
   else{
        for(int i = 0; C[i] != '\0';i++,tam++);
        cadena = new (nothrow) char[tam+1];
        if(cadena == NULL) throw NoHayMemoria();
        cadena[tam] = '\0';
        for (int i = 0; i < tam; i++) cadena[i] = C[i];
   }
}

//////////////////////////////////////////////////Constructor de copias
Cadena::Cadena(const Cadena &C){
   tam = -1;
   cadena = NULL;
   *this = C;
}
//////////////////////////////////////////////////////Sobrecarga del operador=
Cadena& Cadena::operator=(const Cadena &C){
   if (!(this==&C)){
      if (tam != C.tam){
         delete [] cadena;
         cadena = new (nothrow) char[C.tam+1];
         if(cadena == NULL) throw NoHayMemoria();
         cadena[C.tam] = '\0';
         tam = C.tam;
      }
      for(int i = 0; i < tam; i++) cadena[i] = C.cadena[i];
   }
   return *this;
}
///////////////////////////////////Sobrecarga del operador de comparacion
bool Cadena::operator==(Cadena C) const{
   if (this==&C) return true;
   if (tam != C.tam) return false;
   bool esIgual = true;
   for(int i = 0; i < tam; i++) if (cadena[i] != C.cadena[i]){
      esIgual = false;
      break;
   }
   return esIgual;
}
////////////////////////////////////////////////lvalue y rvalue
char & Cadena::operator [](int x){
    if (x < 0 || x >=tam) throw IndiceNoValido();
    return cadena[x];
}
char Cadena::operator[](int x) const{
    if (x < 0 || x >=tam) throw IndiceNoValido();
    return cadena[x];
}
//////////////////////////////////////////Convertir a mayusculas/minusculas
void Cadena::convertirMayusculas(){
   for(int i = 0; i < tam; i++){
      if ((int)cadena[i] >= 97 && (int)cadena[i] <= 122) cadena[i] = char((int)cadena[i] - 32);
   }
}
void Cadena::convertirMinusculas(){
   for(int i = 0; i < tam; i++){
      if ((int)cadena[i] >= 65 && (int)cadena[i] <= 90) cadena[i] = char((int)cadena[i] + 32);
   }
}
//////////////////////////////////////////////////Operadores cout y cin
ostream& operator << (ostream &out, const Cadena &C){
   for (int i = 0; i < C.tam ;i++) out << C.cadena[i];
   return out;
}

istream & operator >> (istream &in, Cadena &C){
      char* aux = new (nothrow) char[100];
      if(aux == NULL) throw NoHayMemoria();
      in.sync();
      in.getline(aux,100);
      C = aux;
      delete [] aux;
      return in;
}
///////////////////////////////////////////////////Subcadena
Cadena Cadena::subCadena(int indice, int length){

    if(indice  < 0 || indice >tam) {
        throw IndiceNoValido();
    }
   //caso de prueba: indice = 0, length = 6
   char * aux = new (nothrow) char[length+1];
   if(aux == NULL) throw NoHayMemoria();
   aux[length] = '\0';
   int i = 0;
   for(; i < length;i++) {
		if(indice + i >= tam) break;
      aux[i] = cadena[indice+i];
   }
   aux[i] = '\0';
   Cadena auxiliar(aux);
   delete [] aux;
   return auxiliar;
}
////////////////////////////////////////////////////Sobrecarga del operador +=
void Cadena::operator += (Cadena C){
   Cadena aux = *this;
   delete[]cadena;
   cadena = new (nothrow) char[aux.tam+C.tam + 1];
   if(cadena == NULL) throw NoHayMemoria();
   cadena[aux.tam+C.tam] = '\0';
   tam = aux.tam+C.tam;
   for(int i = 0; i < aux.tam; i++) cadena[i] = aux.cadena[i];
   for(int i = 0; i < C.tam; i++) cadena[aux.tam+i] = C.cadena[i];
}
///////////////////////////////////////////////////Main
int main()
{
    try{
        char aux; //para validar captura de opciones
        int opcion;
        Cadena cadena1;
        do{
            system("cls");
            cout << "Introduzca una cadena: ";
            cin >> cadena1;

            cout << "Que operacion desea hacer? " << endl;
            cout << "0)Salir" << endl;
            cout << "1)Cadena a minusculas" << endl;
            cout << "2)Cadena a mayusculas" << endl;
            cout << "3)Concatenar cadena" << endl;
            cout << "4) Comparar cadena" << endl;
            cout << "5)Cambiar caracater de cadena" << endl;
            cout << "6) Conseguir caracter de cadena" << endl;
            cout << "7)Conseguir subcadena" << endl;
            cout << "8) Checar cadena vacia" << endl;
            cout << "9) Longitud de la cadena" << endl;
            do{
                cin >> aux;
            }while(! ( (int)aux >= 48 && (int)aux <= 57) );
            opcion = aux - '0';
            if(opcion == 0) break;

            switch(opcion){
                case 1:{
                    //Aqui se prueba el constructor de copias
                    cout << "Cadena original: " << cadena1 << endl;
                    cadena1.convertirMinusculas();
                    Cadena cadena2 = cadena1;
                    cout << "Cadena en minusculas:" << cadena2 << endl;
                    break;
                }
                case 2:{
                    //Aqui se prueba el operador =
                    Cadena cadena2;
                    cout << "Cadena original: " << cadena1 << endl;
                    cadena1.convertirMayusculas();
                    cadena2 = cadena1;
                    cout << "Cadena en mayusculas:" << cadena2 << endl;
                    break;
                }
                case 3:{
                    Cadena cadena2;
                    cout << "Introduzca una cadena: ";
                    cin >> cadena2;
                    cout << "Cadena 1: " << cadena1 << endl;
                    cout << "Cadena 2: " << cadena2 << endl;
                    cadena1+= cadena2;
                    cout << "Cadena1 + cadena 2:" << cadena1 << endl;
                    break;
                }
                case 4:{
                    Cadena cadena2;
                    cout << "Introduzca la segunda cadena: ";
                    cin >> cadena2;
                    cout << "Cadena 1: " << cadena1 << endl;
                    cout << "Cadena 2: " << cadena2 << endl;
                    //Uso el != aqui porque llama a ==, osea, se estan probando los 2
                    if( !(cadena1 != cadena2) ) cout << "Las cadenas son iguales." << endl;
                    else cout << "Las cadenas no son iguales." << endl;
                    break;
                }
                case 5:{
                    char caracter;
                    int indice;
                    cout << "Deme el indice del caracter que quiere reemplazar [0," << cadena1.longitudCadena() - 1 << "]";
                    cin >> indice;
                    cout << "Deme el caracter que quiere poner en reemplazo. ";
                    cin >> caracter;
                    try{
                    cadena1[indice] = caracter;
                    cout << "Cadena nueva:" << cadena1 << endl;
                    }catch(IndiceNoValido &e){
                        cout << e.getMensaje() << endl;
                    }
                    break;
                }
                case 6:{
                    char caracter;
                    int indice;
                    cout << "Deme el indice del caracter que quieres conseguir[0," << cadena1.longitudCadena() - 1 << "]";
                    cin >> indice;
                    try{
                    caracter = cadena1[indice];
                    cout << "Cadena:" << cadena1 << endl;
                    cout << "Caracter conseguido: " << caracter << endl;
                    }catch(IndiceNoValido &e){
                        cout << e.getMensaje() << endl;
                    }
                    break;
                }
                case 7:{
                    Cadena cadena2;
                    int indice, longitud;
                    cout << "De que indice quieres que empieze la subcadena? [0," << cadena1.longitudCadena() - 1 << "]";
                    cin >> indice;
                    cout << "De que longitud sera la subcadena?";
                    cin >> longitud;
                    try{
                    cadena2 = cadena1.subCadena(indice,longitud);
                    cout << "Cadena original: " << cadena1 <<  endl;
                    cout << "Subcadena adquirida: " << cadena2 << endl;
                    }catch(IndiceNoValido &e){
                        cout << e.getMensaje() << endl;
                    }
                    break;

                }
                case 8:{
                    if(!cadena1) cout << "No esta vacia" << endl;
                    else cout << "Esta vacia" << endl;
                    break;
                }
                case 9:{
                    cout << "Longitud de la cadena: " << cadena1.longitudCadena() << endl;
                    break;
                }
            }
            system("Pause");
            system("cls");
            cout  << "Quieres hacer otra cosa? 1) Si 2) No ";

            do{
               cin >> opcion;
            }while(opcion < 1 || opcion > 2);
        }while(opcion == 1);
    }catch(NoHayMemoria &e){
       cout << e.getMensaje();
    }
    return 0;
}
