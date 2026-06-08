package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class CCheckInOutDAO {

    public void cargarReservas(JComboBox combo) {
        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "SELECT r.id_reserva, h.nombre, h.apellido, hb.numero, r.estado "
                + "FROM reservas r "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "WHERE r.estado IN ('Reservada', 'En curso') "
                + "ORDER BY r.fecha_checkin DESC";

        try {
            combo.removeAllItems();

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                combo.addItem(new ItemCombo(
                        rs.getInt("id_reserva"),
                        "Reserva " + rs.getInt("id_reserva")
                        + " - " + rs.getString("nombre") + " " + rs.getString("apellido")
                        + " - Hab. " + rs.getString("numero")
                        + " - " + rs.getString("estado")
                ));
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar reservas: " + e.getMessage());
        }
    }

    public void cargarDatosReserva(int idReserva,
                                   javax.swing.JTextField txtHuesped,
                                   javax.swing.JTextField txtIdHabitacion,
                                   javax.swing.JTextField txtFechaCheckIn,
                                   javax.swing.JTextField txtFechaCheckOut,
                                   javax.swing.JTextField txtEstadoReserva) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "SELECT "
                + "r.id_habitacion, "
                + "CONCAT(h.nombre, ' ', h.apellido) AS huesped, "
                + "r.fecha_checkin, "
                + "r.fecha_checkout, "
                + "r.estado "
                + "FROM reservas r "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "WHERE r.id_reserva = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtHuesped.setText(rs.getString("huesped"));
                txtIdHabitacion.setText(String.valueOf(rs.getInt("id_habitacion")));
                txtFechaCheckIn.setText(rs.getDate("fecha_checkin").toString());
                txtFechaCheckOut.setText(rs.getDate("fecha_checkout").toString());
                txtEstadoReserva.setText(rs.getString("estado"));
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar datos de reserva: " + e.getMessage());
        }
    }

    public void registrarCheckIn(int idReserva, int idHabitacion) {
        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sqlReserva = "UPDATE reservas SET estado = 'En curso' WHERE id_reserva = ?";
        String sqlHabitacion = "UPDATE habitaciones SET estado = 'Ocupada' WHERE id_habitacion = ?";

        try {
            PreparedStatement psReserva = con.prepareStatement(sqlReserva);
            psReserva.setInt(1, idReserva);
            psReserva.executeUpdate();

            PreparedStatement psHabitacion = con.prepareStatement(sqlHabitacion);
            psHabitacion.setInt(1, idHabitacion);
            psHabitacion.executeUpdate();

            psReserva.close();
            psHabitacion.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Check-in registrado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al registrar check-in: " + e.getMessage());
        }
    }

    public void registrarCheckOut(int idReserva, int idHabitacion) {
        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sqlReserva = "UPDATE reservas SET estado = 'Finalizada' WHERE id_reserva = ?";
        String sqlHabitacion = "UPDATE habitaciones SET estado = 'Disponible' WHERE id_habitacion = ?";

        try {
            PreparedStatement psReserva = con.prepareStatement(sqlReserva);
            psReserva.setInt(1, idReserva);
            psReserva.executeUpdate();

            PreparedStatement psHabitacion = con.prepareStatement(sqlHabitacion);
            psHabitacion.setInt(1, idHabitacion);
            psHabitacion.executeUpdate();

            psReserva.close();
            psHabitacion.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Check-out registrado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al registrar check-out: " + e.getMessage());
        }
    }

    public void mostrarReservasCheck(javax.swing.JTable tabla) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel();

        modelo.addColumn("ID Reserva");
        modelo.addColumn("ID Habitación");
        modelo.addColumn("Huésped");
        modelo.addColumn("Habitación");
        modelo.addColumn("Check-in");
        modelo.addColumn("Check-out");
        modelo.addColumn("Estado");

        tabla.setModel(modelo);

        String sql = "SELECT "
                + "r.id_reserva, "
                + "r.id_habitacion, "
                + "CONCAT(h.nombre, ' ', h.apellido) AS huesped, "
                + "hb.numero AS habitacion, "
                + "r.fecha_checkin, "
                + "r.fecha_checkout, "
                + "r.estado "
                + "FROM reservas r "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "WHERE r.estado IN ('Reservada', 'En curso', 'Finalizada') "
                + "ORDER BY r.fecha_checkin DESC";

        try {
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[7];

                fila[0] = rs.getInt("id_reserva");
                fila[1] = rs.getInt("id_habitacion");
                fila[2] = rs.getString("huesped");
                fila[3] = rs.getString("habitacion");
                fila[4] = rs.getDate("fecha_checkin");
                fila[5] = rs.getDate("fecha_checkout");
                fila[6] = rs.getString("estado");

                modelo.addRow(fila);
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

            tabla.getColumnModel().getColumn(1).setMinWidth(0);
            tabla.getColumnModel().getColumn(1).setMaxWidth(0);
            tabla.getColumnModel().getColumn(1).setWidth(0);

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Error al mostrar reservas: " + e.getMessage());
        }
    }
}