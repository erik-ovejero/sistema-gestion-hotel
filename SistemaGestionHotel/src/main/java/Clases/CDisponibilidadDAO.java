package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CDisponibilidadDAO {

    public void consultarDisponibilidad(
            java.sql.Date fechaDesde,
            java.sql.Date fechaHasta,
            JTable tabla) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Número");
        modelo.addColumn("Tipo");
        modelo.addColumn("Capacidad");
        modelo.addColumn("Precio");

        tabla.setModel(modelo);

        String sql =
                "SELECT h.id_habitacion, h.numero, h.tipo, h.capacidad, h.precio "
                + "FROM habitaciones h "
                + "WHERE h.id_habitacion NOT IN ( "
                + "SELECT r.id_habitacion "
                + "FROM reservas r "
                + "WHERE r.estado <> 'Cancelada' "
                + "AND r.fecha_checkin < ? "
                + "AND r.fecha_checkout > ? "
                + ") "
                + "ORDER BY h.numero";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDate(1, fechaHasta);
            ps.setDate(2, fechaDesde);

            ResultSet rs = ps.executeQuery();

            int cantidad = 0;

            while (rs.next()) {
                Object[] fila = new Object[5];

                fila[0] = rs.getInt("id_habitacion");
                fila[1] = rs.getString("numero");
                fila[2] = rs.getString("tipo");
                fila[3] = rs.getInt("capacidad");
                fila[4] = rs.getDouble("precio");

                modelo.addRow(fila);
                cantidad++;
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

            if (cantidad == 0) {
                JOptionPane.showMessageDialog(null,
                        "No hay disponibilidad en las fechas seleccionadas.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al consultar disponibilidad: " + e.getMessage());
        }
    }
}