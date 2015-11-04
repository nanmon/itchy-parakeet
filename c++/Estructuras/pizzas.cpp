/**
	Nombre:
	Autor: L. Roberto Montaño Valdez
	Fecha:
	Descripción:
*/

#include<cstdlib>
#include<iostream>

using namespace std;

class Cola{
public:
    Cola():ultimo(-1), maxEspera(0), maxCola(0){}


    void meter(int bato){
        if(ultimo==MAX-1) throw "shit";
        maxEspera+=bato;
        gente[++ultimo] = bato;
        if(ultimo==maxCola) ++maxCola;
    }

    int sacar(){
        if(ultimo==-1) throw "fuck";
        --maxCola;
        int r = gente[0];
        --ultimo;
        for(int i=0; i<=ultimo; ++i){
            gente[i] = gente[i+1];
        }
        return r;
    }

    int getUltimo(){
        return gente[ultimo];
    }

    int getSize(){
        return ultimo+1;
    }

    void imprimir(){
        cout<<"<<";
        for(int i=0; i<=ultimo; ++i) cout<<gente[i]<<",";
        cout<<"\b<<";
    }

    int getMaxCola(){
        return maxCola;
    }

    int getMaxEspera(){
        return maxEspera-gente[ultimo];
    }

private:
    static const int MAX = 100;
    int gente[100];
    int ultimo;
    int maxCola;
    int maxEspera;
};

class Cajas{
public:
    Cajas(int _n):n(_n), ponerEn(0){
        colas = new Cola[n];
    }

    Cajas(const Cajas &c){
        *this = c;
    }

    Cajas& operator=(const Cajas &c){
        if(this!=&c){
            if(n!=c.n){
                n = c.n;
                if(colas!=NULL) delete [] colas;
                colas = new Cola[n];
            }
            for(int i=0; i<n; ++i){
                colas = c.colas;
            }
        }
    }

    ~Cajas(){
        delete [] colas;
    }

    void nuevoCliente(int tiempo=45){
        colas[ponerEn++].meter(tiempo);
        if(ponerEn>=n) ponerEn=0;
    }

    void imprimir(){
        int maxEspera=0, maxCola=0;
        for(int i=0, aux; i<n; ++i){
            aux = colas[i].getMaxEspera();
            if(maxEspera<aux) maxEspera = aux;
            aux = colas[i].getMaxCola();
            if(maxCola<aux) maxCola = aux;
        }
        cout<<"max espera: "<<maxEspera<<" , max cola: "<<maxCola;
    }

private:
    Cola *colas;
    int n;
    int ponerEn;
};

int main(int argc, char *argv){

    int n, c;
    do{
        cout<<"Numero de cajas: ";
        cin>>n;
    }while(n<=0);

    Cajas lupita(n);

    do{
        cout<<"Numero de clientes: ";
        cin>>c;
    }while(c<=0);

    for(int i=0, t; i<c; ++i){
        /*do{
            cout<<"Duracion del cliente"<<i+1<<": ";
            cin>>t;
        }while(t<=0);*/

        lupita.nuevoCliente();
    }

    lupita.imprimir();

	//system("PAUSE");
	return 0;
}
