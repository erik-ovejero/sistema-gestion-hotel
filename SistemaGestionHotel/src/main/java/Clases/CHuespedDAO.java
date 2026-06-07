package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CHuespedDAO {

    public void mostrarHuespedes(JTable tabla) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("DNI");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Correo");

        tabla.setModel(modelo);

        String sql = "SELECT id_huesped, nombre, apellido, dni, telefono, correo FROM huespedes";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[6];

                fila[0] = rs.getInt("id_huesped");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("apellido");
                fila[3] = rs.getString("dni");
                fila[4] = rs.getString("telefono");
                fila[5] = rs.getString("correo");

                modelo.addRow(fila);
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Error al mostrar huéspedes: " + e.getMessage());
        }
    }

    public void guardarHuesped(CHuesped huesped) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "INSERT INTO huespedes(nombre, apellido, dni, telefono, correo) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, huesped.getNombre());
            ps.setString(2, huesped.getApellido());
            ps.setString(3, huesped.getDni());
            ps.setString(4, huesped.getTelefono());
            ps.setString(5, huesped.getCorreo());

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            javax.swing.JOptionPane.showMessageDialog(null, "Huésped guardado correctamente.");

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Error al guardar huésped: " + e.getMessage());
        }
    }

    public void modificarHuesped(CHuesped huesped) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "UPDATE huespedes SET nombre=?, apellido=?, dni=?, telefono=?, correo=? WHERE id_huesped=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, huesped.getNombre());
            ps.setString(2, huesped.getApellido());
            ps.setString(3, huesped.getDni());
            ps.setString(4, huesped.getTelefono());
            ps.setString(5, huesped.getCorreo());
            ps.setInt(6, huesped.getIdHuesped());

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            javax.swing.JOptionPane.showMessageDialog(null, "Huésped modificado correctamente.");

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Error al modificar huésped: " + e.getMessage());
        }
    }

    public void eliminarHuesped(int idHuesped) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "DELETE FROM huespedes WHERE id_huesped=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idHuesped);

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            javax.swing.JOptionPane.showMessageDialog(null, "Huésped eliminado correctamente.");

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Error al eliminar huésped: " + e.getMessage());
        }
    }
}