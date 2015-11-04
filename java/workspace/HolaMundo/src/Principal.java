import javax.swing.*;

public class Principal{
	public static void main(String args[]){
		//Capturar
		String holi= JOptionPane.showInputDialog(
			null, 
			"FREEZER ERES UN ESTUPIDO!!",
			"MALDITO FREEZER!",
			1); //JOptionPane.INFORMATION_MESSAGE
		//Confirmacion
		int smn = JOptionPane.showConfirmDialog(
				null, 
				"smn o no smn?", 
				null, 
				0, //JOptionPane.YES_NO_OPTION
				2); //JOptionPane.WARNING_MESSAGE
		//Opcion
		int smn2 = JOptionPane.showOptionDialog(
				null, 
				"Smn o no smn?", 
				"No.", 
				0, //JOptionPane.OK_OPTION
				3, //JOptionPane.QUESTION_MESSAGE
				null, 
				args, 
				holi);
		//Imprimir
		JOptionPane.showMessageDialog(null, holi+(smn==0?" smn." : " no smn"));
		
	}
}