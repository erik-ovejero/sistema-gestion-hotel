package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CUsuarioDAO {

    public void mostrarUsuarios(JTable tabla) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Usuario");
        modelo.addColumn("Contraseña");
        modelo.addColumn("Rol");
        modelo.addColumn("Estado");

        tabla.setModel(modelo);

        String sql = "SELECT id_usuario, nombre_usuario, contrasena, rol, estado FROM usuarios";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[5];

                fila[0] = rs.getInt("id_usuario");
                fila[1] = rs.getString("nombre_usuario");
                fila[2] = rs.getString("contrasena");
                fila[3] = rs.getString("rol");
                fila[4] = rs.getString("estado");

                modelo.addRow(fila);
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al mostrar usuarios: " + e.getMessage());
        }
    }

    public void guardarUsuario(CUsuario usuario) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "INSERT INTO usuarios(nombre_usuario, contrasena, rol, estado) "
                + "VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getRol());
            ps.setString(4, usuario.getEstado());

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Usuario guardado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al guardar usuario: " + e.getMessage());
        }
    }

    public void modificarUsuario(CUsuario usuario) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "UPDATE usuarios SET nombre_usuario=?, contrasena=?, rol=?, estado=? "
                + "WHERE id_usuario=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getRol());
            ps.setString(4, usuario.getEstado());
            ps.setInt(5, usuario.getIdUsuario());

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Usuario modificado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al modificar usuario: " + e.getMessage());
        }
    }

    public void eliminarUsuario(int idUsuario) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "DELETE FROM usuarios WHERE id_usuario=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idUsuario);
            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar usuario: " + e.getMessage());
        }
    }
}