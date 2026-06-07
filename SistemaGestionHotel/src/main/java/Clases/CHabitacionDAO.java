package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

public class CHabitacionDAO {

    public void mostrarHabitaciones(JTable tabla) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Número");
        modelo.addColumn("Tipo");
        modelo.addColumn("Capacidad");
        modelo.addColumn("Precio");
        modelo.addColumn("Estado");

        tabla.setModel(modelo);

        String sql = "SELECT id_habitacion, numero, tipo, capacidad, precio, estado FROM habitaciones";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[6];

                fila[0] = rs.getInt("id_habitacion");
                fila[1] = rs.getString("numero");
                fila[2] = rs.getString("tipo");
                fila[3] = rs.getInt("capacidad");
                fila[4] = rs.getDouble("precio");
                fila[5] = rs.getString("estado");

                modelo.addRow(fila);
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al mostrar habitaciones: " + e.getMessage());
        }
    }

    public void guardarHabitacion(CHabitacion habitacion) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "INSERT INTO habitaciones(numero, tipo, capacidad, precio, estado) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, habitacion.getNumero());
            ps.setString(2, habitacion.getTipo());
            ps.setInt(3, habitacion.getCapacidad());
            ps.setDouble(4, habitacion.getPrecio());
            ps.setString(5, habitacion.getEstado());

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Habitación guardada correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al guardar habitación: " + e.getMessage());
        }
    }

    public void modificarHabitacion(CHabitacion habitacion) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "UPDATE habitaciones SET numero=?, tipo=?, capacidad=?, precio=?, estado=? WHERE id_habitacion=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, habitacion.getNumero());
            ps.setString(2, habitacion.getTipo());
            ps.setInt(3, habitacion.getCapacidad());
            ps.setDouble(4, habitacion.getPrecio());
            ps.setString(5, habitacion.getEstado());
            ps.setInt(6, habitacion.getIdHabitacion());

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Habitación modificada correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al modificar habitación: " + e.getMessage());
        }
    }

    public void eliminarHabitacion(int idHabitacion) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "DELETE FROM habitaciones WHERE id_habitacion=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idHabitacion);
            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Habitación eliminada correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar habitación: " + e.getMessage());
        }
    }
}