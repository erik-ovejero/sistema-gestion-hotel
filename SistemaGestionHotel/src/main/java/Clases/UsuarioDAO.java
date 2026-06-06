package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class UsuarioDAO {

    public CUsuario iniciarSesion(String nombreUsuario, String contrasena) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        CUsuario usuario = null;

        String sql = "SELECT id_usuario, nombre_usuario, rol, estado "
                + "FROM usuarios "
                + "WHERE nombre_usuario = ? "
                + "AND contrasena = ? "
                + "AND estado = 'Activo'";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new CUsuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("rol"),
                        rs.getString("estado")
                );
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al iniciar sesión: " + e.getMessage());
        }

        return usuario;
    }
}