package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CPagoDAO {

    public void cargarReservas(JComboBox combo) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "SELECT "
                + "r.id_reserva, "
                + "CONCAT('Reserva ', r.id_reserva, ' - ', h.nombre, ' ', h.apellido, "
                + "' - Hab. ', hb.numero, "
                + "' - Total $', DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio) AS descripcion "
                + "FROM reservas r "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "WHERE r.estado = 'Reservada'";

        try {
            combo.removeAllItems();

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                combo.addItem(new ItemCombo(
                        rs.getInt("id_reserva"),
                        rs.getString("descripcion")
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

    public void mostrarPagos(JTable tabla) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID Pago");
        modelo.addColumn("ID Reserva");
        modelo.addColumn("Reserva");
        modelo.addColumn("Huésped");
        modelo.addColumn("Habitación");
        modelo.addColumn("Fecha pago");
        modelo.addColumn("Monto");
        modelo.addColumn("Medio");
        modelo.addColumn("Estado");

        tabla.setModel(modelo);

        String sql = "SELECT "
                + "p.id_pago, "
                + "p.id_reserva, "
                + "CONCAT('Reserva ', r.id_reserva) AS reserva, "
                + "CONCAT(h.nombre, ' ', h.apellido) AS huesped, "
                + "hb.numero AS habitacion, "
                + "p.fecha_pago, "
                + "p.monto, "
                + "p.medio_pago, "
                + "p.estado_pago "
                + "FROM pagos p "
                + "INNER JOIN reservas r ON p.id_reserva = r.id_reserva "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "ORDER BY p.fecha_pago DESC";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[9];

                fila[0] = rs.getInt("id_pago");
                fila[1] = rs.getInt("id_reserva");
                fila[2] = rs.getString("reserva");
                fila[3] = rs.getString("huesped");
                fila[4] = rs.getString("habitacion");
                fila[5] = rs.getDate("fecha_pago");
                fila[6] = rs.getDouble("monto");
                fila[7] = rs.getString("medio_pago");
                fila[8] = rs.getString("estado_pago");

                modelo.addRow(fila);
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

            tabla.getColumnModel().getColumn(1).setMinWidth(0);
            tabla.getColumnModel().getColumn(1).setMaxWidth(0);
            tabla.getColumnModel().getColumn(1).setWidth(0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al mostrar pagos: " + e.getMessage());
        }
    }

    public void guardarPago(CPago pago) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "INSERT INTO pagos "
                + "(id_reserva, fecha_pago, monto, medio_pago, estado_pago) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, pago.getIdReserva());
            ps.setDate(2, pago.getFechaPago());
            ps.setDouble(3, pago.getMonto());
            ps.setString(4, pago.getMedioPago());
            ps.setString(5, pago.getEstadoPago());

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Pago guardado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al guardar pago: " + e.getMessage());
        }
    }

    public void modificarPago(CPago pago) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "UPDATE pagos SET "
                + "id_reserva = ?, "
                + "fecha_pago = ?, "
                + "monto = ?, "
                + "medio_pago = ?, "
                + "estado_pago = ? "
                + "WHERE id_pago = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, pago.getIdReserva());
            ps.setDate(2, pago.getFechaPago());
            ps.setDouble(3, pago.getMonto());
            ps.setString(4, pago.getMedioPago());
            ps.setString(5, pago.getEstadoPago());
            ps.setInt(6, pago.getIdPago());

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Pago modificado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al modificar pago: " + e.getMessage());
        }
    }

    public void eliminarPago(int idPago) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "DELETE FROM pagos WHERE id_pago = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idPago);
            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Pago eliminado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar pago: " + e.getMessage());
        }
    }
}