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
                + "' - Saldo $', ROUND(((DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio) "
                + "- IFNULL(SUM(CASE WHEN p.estado_pago <> 'Rechazado' THEN p.monto ELSE 0 END), 0)), 2)) AS descripcion "
                + "FROM reservas r "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "LEFT JOIN pagos p ON r.id_reserva = p.id_reserva "
                + "WHERE r.estado <> 'Cancelada' "
                + "GROUP BY r.id_reserva, h.nombre, h.apellido, r.fecha_checkin, r.fecha_checkout, hb.precio "
                + "HAVING ((DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio) "
                + "- IFNULL(SUM(CASE WHEN p.estado_pago <> 'Rechazado' THEN p.monto ELSE 0 END), 0)) > 0 "
                + "ORDER BY r.id_reserva DESC";

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

    public void cargarReservas(JComboBox combo, int idReservaIncluida) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "SELECT "
                + "r.id_reserva, "
                + "CONCAT('Reserva ', r.id_reserva, ' - ', h.nombre, ' ', h.apellido, "
                + "' - Saldo $', ROUND(((DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio) "
                + "- IFNULL(SUM(CASE WHEN p.estado_pago <> 'Rechazado' THEN p.monto ELSE 0 END), 0)), 2)) AS descripcion "
                + "FROM reservas r "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "LEFT JOIN pagos p ON r.id_reserva = p.id_reserva "
                + "WHERE r.estado <> 'Cancelada' "
                + "GROUP BY r.id_reserva, h.nombre, h.apellido, r.fecha_checkin, r.fecha_checkout, hb.precio "
                + "HAVING (((DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio) "
                + "- IFNULL(SUM(CASE WHEN p.estado_pago <> 'Rechazado' THEN p.monto ELSE 0 END), 0)) > 0 "
                + "OR r.id_reserva = ?) "
                + "ORDER BY r.id_reserva DESC";

        try {
            combo.removeAllItems();

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idReservaIncluida);
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
        modelo.addColumn("Total reserva");
        modelo.addColumn("Pagado");
        modelo.addColumn("Saldo");

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
                + "p.estado_pago, "
                + "(DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio) AS total_reserva, "
                + "IFNULL((SELECT SUM(p2.monto) FROM pagos p2 "
                + "WHERE p2.id_reserva = r.id_reserva AND p2.estado_pago <> 'Rechazado'), 0) AS total_pagado, "
                + "((DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio) - "
                + "IFNULL((SELECT SUM(p3.monto) FROM pagos p3 "
                + "WHERE p3.id_reserva = r.id_reserva AND p3.estado_pago <> 'Rechazado'), 0)) AS saldo "
                + "FROM pagos p "
                + "INNER JOIN reservas r ON p.id_reserva = r.id_reserva "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "ORDER BY p.fecha_pago DESC";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[12];

                fila[0] = rs.getInt("id_pago");
                fila[1] = rs.getInt("id_reserva");
                fila[2] = rs.getString("reserva");
                fila[3] = rs.getString("huesped");
                fila[4] = rs.getString("habitacion");
                fila[5] = rs.getDate("fecha_pago");
                fila[6] = rs.getDouble("monto");
                fila[7] = rs.getString("medio_pago");
                fila[8] = rs.getString("estado_pago");
                fila[9] = rs.getDouble("total_reserva");
                fila[10] = rs.getDouble("total_pagado");
                fila[11] = rs.getDouble("saldo");

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

        if (!validarPago(pago, 0)) {
            return;
        }

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

        if (!validarPago(pago, pago.getIdPago())) {
            return;
        }

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

    private boolean validarPago(CPago pago, int idPagoExcluir) {

        if (pago.getMonto() <= 0) {
            JOptionPane.showMessageDialog(null, "El monto del pago debe ser mayor a cero.");
            return false;
        }

        // Los pagos rechazados no descuentan saldo, por lo tanto no se validan contra el saldo pendiente.
        if (pago.getEstadoPago().equalsIgnoreCase("Rechazado")) {
            return true;
        }

        double totalReserva = obtenerTotalReserva(pago.getIdReserva());
        double totalPagado = obtenerTotalPagado(pago.getIdReserva(), idPagoExcluir);
        double saldoPendiente = totalReserva - totalPagado;

        if (pago.getMonto() > saldoPendiente) {
            JOptionPane.showMessageDialog(null,
                    "El monto ingresado supera el saldo pendiente.\n"
                    + "Total de la reserva: $" + totalReserva + "\n"
                    + "Pagado hasta ahora: $" + totalPagado + "\n"
                    + "Saldo pendiente: $" + saldoPendiente);
            return false;
        }

        return true;
    }

    private double obtenerTotalReserva(int idReserva) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "SELECT (DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio) AS total "
                + "FROM reservas r "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "WHERE r.id_reserva = ?";

        double total = 0;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al calcular total de reserva: " + e.getMessage());
        }

        return total;
    }

    private double obtenerTotalPagado(int idReserva, int idPagoExcluir) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "SELECT IFNULL(SUM(monto), 0) AS total_pagado "
                + "FROM pagos "
                + "WHERE id_reserva = ? "
                + "AND estado_pago <> 'Rechazado' "
                + "AND id_pago <> ?";

        double totalPagado = 0;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva);
            ps.setInt(2, idPagoExcluir);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalPagado = rs.getDouble("total_pagado");
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al calcular pagos registrados: " + e.getMessage());
        }

        return totalPagado;
    }
}
