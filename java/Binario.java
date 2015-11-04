/*
	Conversiones de decimal y binario.
	Roberto Monta~o
	22/08/14
*/

public class Binario{
	public static void main(String args[]){
		java.util.Scanner escan = new java.util.Scanner(System.in);
		System.out.println("Programa que convierte un numero de decimal a binario o viceversa");
		System.out.println("Elija una opcion.");
		System.out.println("1) Decimal a Binario.");
		System.out.println("2) Binario a Decimal.");
		int opcion;
		do opcion = escan.nextInt();
		while(opcion!= 1 && opcion!=2);
		System.out.print("Numero: ");
		String entrada = escan.next(), salida = "";
		int punto = entrada.indexOf(".");
		try{
			switch(opcion){
			case 1:
				int entero = punto>-1? Integer.parseInt(entrada.substring(0,punto)) : Integer.parseInt(entrada);
				double decim = punto>-1? Double.parseDouble(entrada.substring(punto)) : 0;
				while(entero>0){
					salida = entero%2+salida;
					entero/=2;
				}
				if(punto>-1) salida=salida+".";
				//decim/= Math.pow(10, entrada.length()-punto-1);
				while(decim>0 && salida.length()<30){
					salida= salida+(decim*2>=1? "1" : "0");
					decim*=2;
					decim-=decim>=1? 1 : 0;
				}
				break;
			case 2:
				double poten = Math.pow(2, (punto>-1?punto:entrada.length())-1);
				double salNum=0;
				for(int i=0;i<entrada.length();++i){
					if(entrada.charAt(i)!='.'){
						if(entrada.charAt(i)!='0' && entrada.charAt(i)!='1') 
							throw new RuntimeException();
						salNum+=Integer.parseInt(Character.toString(entrada.charAt(i)))*poten;
						poten/=2;
					}
				}
				salida = salNum+"";
			}
		}catch(Exception er){
			System.out.println("Error: Numero invalido");
			return;
		}
		System.out.println(entrada+" = "+salida);
		System.out.println("\tHecho por Nan");
	}
}
