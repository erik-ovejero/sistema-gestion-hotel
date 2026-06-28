package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CFacturaDAO {

    public void cargarReservasPagadas(JComboBox combo) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "SELECT "
                + "r.id_reserva, "
                + "CONCAT('Reserva ', r.id_reserva, ' - ', h.nombre, ' ', h.apellido, "
                + "' - Total $', ROUND((DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio), 2)) AS descripcion "
                + "FROM reservas r "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "LEFT JOIN pagos p ON r.id_reserva = p.id_reserva "
                + "WHERE r.estado <> 'Cancelada' "
                + "AND r.id_reserva NOT IN ( "
                + "    SELECT id_reserva "
                + "    FROM facturas "
                + "    WHERE estado_factura <> 'Anulada' "
                + ") "
                + "GROUP BY r.id_reserva, h.nombre, h.apellido, r.fecha_checkin, r.fecha_checkout, hb.precio "
                + "HAVING ((DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio) "
                + "- IFNULL(SUM(CASE WHEN p.estado_pago = 'Aprobado' THEN p.monto ELSE 0 END), 0)) <= 0 "
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
                    "Error al cargar reservas pagadas: " + e.getMessage());
        }
    }

    public double obtenerImporteReserva(int idReserva) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        double importe = 0;

        String sql = "SELECT "
                + "(DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio) AS importe_total "
                + "FROM reservas r "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "WHERE r.id_reserva = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                importe = rs.getDouble("importe_total");
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al obtener importe: " + e.getMessage());
        }

        return importe;
    }

    public void mostrarFacturas(JTable tabla) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID Factura");
        modelo.addColumn("ID Reserva");
        modelo.addColumn("Reserva");
        modelo.addColumn("Huésped");
        modelo.addColumn("Habitación");
        modelo.addColumn("Fecha emisión");
        modelo.addColumn("Tipo");
        modelo.addColumn("Importe");
        modelo.addColumn("Estado");

        tabla.setModel(modelo);

        String sql = "SELECT "
                + "f.id_factura, "
                + "f.id_reserva, "
                + "CONCAT('Reserva ', r.id_reserva) AS reserva, "
                + "CONCAT(h.nombre, ' ', h.apellido) AS huesped, "
                + "hb.numero AS habitacion, "
                + "f.fecha_emision, "
                + "f.tipo_factura, "
                + "f.importe_total, "
                + "f.estado_factura "
                + "FROM facturas f "
                + "INNER JOIN reservas r ON f.id_reserva = r.id_reserva "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "ORDER BY f.fecha_emision DESC";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[9];

                fila[0] = rs.getInt("id_factura");
                fila[1] = rs.getInt("id_reserva");
                fila[2] = rs.getString("reserva");
                fila[3] = rs.getString("huesped");
                fila[4] = rs.getString("habitacion");
                fila[5] = rs.getDate("fecha_emision");
                fila[6] = rs.getString("tipo_factura");
                fila[7] = rs.getDouble("importe_total");
                fila[8] = rs.getString("estado_factura");

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
                    "Error al mostrar facturas: " + e.getMessage());
        }
    }

    public void emitirFactura(CFactura factura) {

        if (existeFacturaEmitida(factura.getIdReserva(), 0)) {
            JOptionPane.showMessageDialog(null,
                    "La reserva seleccionada ya posee una factura emitida.");
            return;
        }

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "INSERT INTO facturas "
                + "(id_reserva, fecha_emision, tipo_factura, importe_total, estado_factura) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, factura.getIdReserva());
            ps.setDate(2, factura.getFechaEmision());
            ps.setString(3, factura.getTipoFactura());
            ps.setDouble(4, factura.getImporteTotal());
            ps.setString(5, factura.getEstadoFactura());

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Factura emitida correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al emitir factura: " + e.getMessage());
        }
    }

    public void modificarFactura(CFactura factura) {

        if (existeFacturaEmitida(factura.getIdReserva(), factura.getIdFactura())) {
            JOptionPane.showMessageDialog(null,
                    "Ya existe otra factura emitida para esa reserva.");
            return;
        }

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "UPDATE facturas SET "
                + "id_reserva = ?, "
                + "fecha_emision = ?, "
                + "tipo_factura = ?, "
                + "importe_total = ?, "
                + "estado_factura = ? "
                + "WHERE id_factura = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, factura.getIdReserva());
            ps.setDate(2, factura.getFechaEmision());
            ps.setString(3, factura.getTipoFactura());
            ps.setDouble(4, factura.getImporteTotal());
            ps.setString(5, factura.getEstadoFactura());
            ps.setInt(6, factura.getIdFactura());

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Factura modificada correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al modificar factura: " + e.getMessage());
        }
    }

    public void eliminarFactura(int idFactura) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "DELETE FROM facturas WHERE id_factura = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idFactura);

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Factura eliminada correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar factura: " + e.getMessage());
        }
    }

    private boolean existeFacturaEmitida(int idReserva, int idFacturaExcluir) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "SELECT COUNT(*) AS cantidad "
                + "FROM facturas "
                + "WHERE id_reserva = ? "
                + "AND estado_factura <> 'Anulada' "
                + "AND id_factura <> ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idReserva);
            ps.setInt(2, idFacturaExcluir);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int cantidad = rs.getInt("cantidad");

                rs.close();
                ps.close();
                conexion.cerrarConexion();

                return cantidad > 0;
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al validar factura existente: " + e.getMessage());
        }

        return false;
    }
}