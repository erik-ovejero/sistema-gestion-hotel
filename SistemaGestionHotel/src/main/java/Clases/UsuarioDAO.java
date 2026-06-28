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
                int idUsuario = rs.getInt("id_usuario");
                String usuarioBD = rs.getString("nombre_usuario");
                String rol = rs.getString("rol");
                String estado = rs.getString("estado");

                if (rol.equalsIgnoreCase("Administrador")) {
                    usuario = new CAdministrador(idUsuario, usuarioBD, rol, estado);
                } else if (rol.equalsIgnoreCase("Recepcionista")) {
                    usuario = new CRecepcionista(idUsuario, usuarioBD, rol, estado);
                } else {
                    usuario = new CUsuario(idUsuario, usuarioBD, rol, estado);
                }
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
