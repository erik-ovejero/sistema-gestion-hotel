
package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;



public class CConexion {

Connection conectar = null;

String usuario="root";
String contrasenia="";
String bd="sistema_hotel";
String ip="localhost";
String puerto="3306";

String cadena ="jdbc:mysql://"+ip+":"+puerto+"/"+bd;

public Connection estableceConexion(){
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
conectar = DriverManager.getConnection(cadena,usuario,contrasenia);

//JOptionPane.showMessageDialog(null,"Se conectó a la BD correctamente.");
    } catch (Exception e) {
JOptionPane.showMessageDialog(null,"No se conectó a la BD correctamente, revise su conexión o usuario y contraseña.");
    }
return conectar;
} 

public void cerrarConexion () {
    try {
        if(conectar!=null && !conectar.isClosed()) {
conectar.close();
// JOptionPane.showMessageDialog(null, "Coneccion cerrada");
}
    } catch (Exception e) {
JOptionPane.showMessageDialog(null, "No se pudo cerrar la conexión");
    }
}
    
}
