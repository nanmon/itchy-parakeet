/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportedeusuarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Roberto Montaño
 */
public class ReporteDeUsuarios extends javax.swing.JFrame {
    
    ResultSet resultSet;
    ResultSetMetaData metaData;

    /**
     * Creates new form ReporteDeUsuarios
     */
    public ReporteDeUsuarios() {
        resultSet = null;
        initComponents();
        conectar();
        imprimir();
    }
    
    
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://148.225.83.3/e1ingsoft1_2014_2";
    
    public void conectar() {        
        System.out.println("Iniciando programa...");
        Connection connection = null; // manages connection
        Statement statement = null; // query statement
        
        try {
            System.out.println("loading the driver class");
            Class.forName( DRIVER );            
            System.out.println("establish connection to db");
            connection = DriverManager.getConnection(
                    DATABASE_URL, "e1ingsoft1", "SAlt6347" );            
            System.out.println("create Statement for querying database");
            statement = connection.createStatement();
            System.out.println("query database");
            resultSet = statement.executeQuery("SELECT * FROM table24" );
            
            //PENDIENTE PARA EL VIERNES!!!
            System.out.println("process query results");
            metaData = resultSet.getMetaData();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ReporteDeUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }catch ( Exception exception ){
                exception.printStackTrace();
            } // end catch
        }
        System.out.println("...Finalizado!");
    }
    
    public void imprimir(){
        try {
            int numberOfColumns = metaData.getColumnCount();
            System.out.println( "Datos de la Tabla adrianvo:\n" );
            for ( int i = 1; i <= numberOfColumns; i++ ){
                System.out.printf( "%-8s\t", metaData.getColumnName( i ));
            }
            ArrayList arr = new ArrayList();
            while( resultSet.next() ){
                System.out.println();
                String strarr[] = new String[numberOfColumns];
                for ( int i = 1; i <= numberOfColumns; i++ )
                    strarr[i-1] = resultSet.getString(i);
                arr.add(strarr);
                    //System.out.printf( "%-8s\t", resultSet.getObject( i ));
            }
            String [][] s = new String[arr.size()][4];
            arr.toArray(s);
            jTable1.setModel(new DefaultTableModel(
                        s, new String[] {"Id", "Nombre", "Apellido", "Direccion"}
            ));
        } catch (SQLException ex) {
            Logger.getLogger(ReporteDeUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane5 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Id", "Nombre", "Apellido", "Direccion"
            }
        ));
        jScrollPane5.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(18);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("GTK+".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReporteDeUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReporteDeUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReporteDeUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReporteDeUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReporteDeUsuarios().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
