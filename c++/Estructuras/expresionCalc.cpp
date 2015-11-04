/**
	Nombre:
	Autor: L. Roberto Montaño Valdez
	Fecha:
	Descripción:
*/

#include<cstdlib>
#include<iostream>
#include"cadenaDouble.cpp"
#include"plantillaPila.cpp"

using namespace std;

class DivisionEntreCero : public exception {
    const char* what() const throw() { return "Division entre cero"; }
};

class RaizCompleja : public exception {
    const char* what() const throw() { return "Raiz compleja"; }
};

bool expresionValida(char ex[]){
    Pila<char> pila;
    char numero[100] = {}, aux;
    int c=0;
    try{
        for(int i=0; ex[i]!='\0'; ++i){
            switch(ex[i]){
                case '(': case '[': case '{':
                    if(c>0) return false;
                    pila.insertarElemento(ex[i]);
                    break;
                case ')': case ']': case '}':
                    numero[c] = '\0';
                    if(esDouble(numero)){
                        if(pila.getElemento()=='+') pila.removerElemento();
                        else pila.insertarElemento('n');
                    }
                    c = 0;
                    aux = ex[i] == ')' ? '(' : ( ex[i]==']' ? '[' : '{' );
                    if(pila.removerElemento()=='n' && pila.removerElemento()==aux){
                        if (pila.estaVacia()) pila.insertarElemento('n');
                        else if(pila.getElemento()=='+') pila.removerElemento();
                        else pila.insertarElemento('n');
                    }else return false;
                    break;
                case '+': case '-': case '*': case '/': case '^':
                    numero[c] = '\0';
                    if(esDouble(numero)){
                        if(pila.estaVacia()) pila.insertarElemento('n');
                        else if(pila.getElemento()=='+') pila.removerElemento();
                        else pila.insertarElemento('n');
                    }
                    pila.insertarElemento('+');
                    c = 0;
                    break;
                default:
                    numero[c++] = ex[i];
            }
        }
        numero[c] = '\0';
        if(esDouble(numero)){
            if(pila.estaVacia()) pila.insertarElemento('n');
            else if(pila.getElemento()=='+') pila.removerElemento();
            else pila.insertarElemento('n');
        }
        return pila.getSize()==1 && pila.removerElemento()=='n';
    }catch(...){
        return false;
    }
}

long double evaluarPostfijo(char ex[]){
    Pila<long double> pila;
    char numero[100];
    long double a, b;
    int c=0;
    for(int i=0; ex[i]!='\0'; ++i){
        switch(ex[i]){
            case '+':
                numero[c] = '\0';
                b = c>0 ? parseDouble(numero) : pila.removerElemento();
                c = 0;
                a = pila.removerElemento();
                pila.insertarElemento(a+b);
                break;
            case '-':
                numero[c] = '\0';
                b = c>0 ? parseDouble(numero) : pila.removerElemento();
                c = 0;
                a = pila.removerElemento();
                pila.insertarElemento(a-b);
                break;
            case '/':
                numero[c] = '\0';
                b = c>0 ? parseDouble(numero) : pila.removerElemento();
                c = 0;
                a = pila.removerElemento();
                if(b==0) throw DivisionEntreCero();
                pila.insertarElemento(a/b);
                break;
            case '*':
                numero[c] = '\0';
                b = c>0 ? parseDouble(numero) : pila.removerElemento();
                c = 0;
                a = pila.removerElemento();
                pila.insertarElemento(a*b);
                break;
            case '^':
                numero[c] = '\0';
                b = c>0 ? parseDouble(numero) : pila.removerElemento();
                c = 0;
                a = pila.removerElemento();
                if(a==0 && b<=0) throw DivisionEntreCero();
                if(a<0 && (long int)b!=b) throw RaizCompleja();
                pila.insertarElemento(pow(a, b));
                break;
            case ',':
                if(c==0) break;
                numero[c] = '\0';
                pila.insertarElemento(parseDouble(numero));
                c = 0;
                break;
            default:
                numero[c++] = ex[i];
        }
    }
    return pila.removerElemento();
}

char* cambiarAPostfijo(char ex[]){
    Pila<char> pila;
    char numero[100], *chila = new char[1024], aux;
    int n = 0, c=0;
    for(int i=0; ex[i]!='\0'; ++i){
        switch(ex[i]){
            case '+':  case '-':
                for(int j=0; j<n; ++j){
                    chila[c++] = numero[j];
                }
                if((pila.estaVacia() || pila.getElemento()=='(' || pila.getElemento()=='[' || pila.getElemento()=='{') && n!=0) chila[c++] = ',';
                while(!pila.estaVacia() && pila.getElemento()!='('  && pila.getElemento()!='[' && pila.getElemento()!='{') chila[c++] = pila.removerElemento();
                n=0;
                pila.insertarElemento(ex[i]);
                break;
            case '*': case '/':
                for(int j=0; j<n; ++j){
                    chila[c++] = numero[j];
                }
                if((pila.estaVacia() || (pila.getElemento()!='^' && pila.getElemento()!='*' && pila.getElemento()!='/')) && n!=0) chila[c++] = ',';
                while(!pila.estaVacia() && (pila.getElemento()=='^' || pila.getElemento()=='*' || pila.getElemento()=='/')) chila[c++] = pila.removerElemento();
                n=0;
                pila.insertarElemento(ex[i]);
                break;
            case '^':
                pila.insertarElemento('^');
                for(int j=0; j<n; ++j){
                    chila[c++] = numero[j];
                }
                if(n!=0) chila[c++] = ',';
                n = 0;
                break;
            case '(': case '[': case '{':
                pila.insertarElemento(ex[i]);
                break;
            case ')': case ']': case '}':
                for(int j=0; j<n; ++j){
                    chila[c++] = numero[j];
                }
                aux = ex[i]==')'? '(' : (ex[i]==']' ? '[' : '{');
                if(n!=0 && pila.getElemento()==aux) chila[c++] = ',';
                n = 0;
                while(pila.getElemento()!=aux) chila[c++] = pila.removerElemento();
                pila.removerElemento();
                break;
            default: numero[n++] = ex[i];
        }
    }
    for(int i=0; i<n; ++i) chila[c++] = numero[i];
    while(!pila.estaVacia()) if(pila.getElemento()!='(') chila[c++] = pila.removerElemento();
    chila[c]='\0';
    return chila;
}

int main(int argc, char **argv){


    char *expr;
    if(argc>1) expr = argv[1];
    else{
        expr = new char[1024];
        cin>>expr;
    }
    if(expresionValida(expr)){
        expr = cambiarAPostfijo(expr);
        cout<<expr<<endl;
        cout<<evaluarPostfijo(expr);
    }else cout<<"oshe no";
    cout<<endl;
    if(argc==1) delete [] expr;
	//system("PAUSE");
	return 0;
}
