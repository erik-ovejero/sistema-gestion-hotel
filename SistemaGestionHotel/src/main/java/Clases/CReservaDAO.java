package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CReservaDAO {

    public void cargarHuespedes(JComboBox combo) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "SELECT id_huesped, nombre, apellido, dni FROM huespedes";

        try {
            combo.removeAllItems();

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_huesped");

                String descripcion = rs.getString("nombre") + " "
                        + rs.getString("apellido")
                        + " - DNI: "
                        + rs.getString("dni");

                combo.addItem(new ItemCombo(id, descripcion));
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar huéspedes: " + e.getMessage());
        }
    }

    public void cargarHabitacionesDisponibles(JComboBox combo) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        // Se cargan todas las habitaciones que NO están en mantenimiento.
        // Una habitación marcada como "Ocupada" hoy puede estar disponible para fechas futuras.
        String sql = "SELECT id_habitacion, numero, tipo, capacidad, precio, estado "
                + "FROM habitaciones "
                + "WHERE estado <> 'Mantenimiento'";

        try {
            combo.removeAllItems();

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_habitacion");

                String descripcion = "Hab. "
                        + rs.getString("numero")
                        + " - "
                        + rs.getString("tipo")
                        + " - Cap: "
                        + rs.getInt("capacidad")
                        + " - $"
                        + rs.getDouble("precio")
                        + " - Estado actual: "
                        + rs.getString("estado");

                combo.addItem(new ItemCombo(id, descripcion));
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar habitaciones: " + e.getMessage());
        }
    }

    public void mostrarReservas(JTable tabla) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID Reserva");
        modelo.addColumn("ID Huésped");
        modelo.addColumn("ID Habitación");
        modelo.addColumn("Huésped");
        modelo.addColumn("Habitación");
        modelo.addColumn("Check-in");
        modelo.addColumn("Check-out");
        modelo.addColumn("Noches");
        modelo.addColumn("Precio noche");
        modelo.addColumn("Total");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Estado");
        modelo.addColumn("Observaciones");

        tabla.setModel(modelo);

        String sql = "SELECT "
                + "r.id_reserva, "
                + "r.id_huesped, "
                + "r.id_habitacion, "
                + "CONCAT(h.nombre, ' ', h.apellido) AS huesped, "
                + "hb.numero AS habitacion, "
                + "r.fecha_checkin, "
                + "r.fecha_checkout, "
                + "DATEDIFF(r.fecha_checkout, r.fecha_checkin) AS noches, "
                + "hb.precio AS precio_noche, "
                + "(DATEDIFF(r.fecha_checkout, r.fecha_checkin) * hb.precio) AS precio_total, "
                + "r.cantidad_huespedes, "
                + "r.estado, "
                + "r.observaciones "
                + "FROM reservas r "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "ORDER BY r.fecha_checkin DESC";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[13];

                fila[0] = rs.getInt("id_reserva");
                fila[1] = rs.getInt("id_huesped");
                fila[2] = rs.getInt("id_habitacion");
                fila[3] = rs.getString("huesped");
                fila[4] = rs.getString("habitacion");
                fila[5] = rs.getDate("fecha_checkin");
                fila[6] = rs.getDate("fecha_checkout");
                fila[7] = rs.getInt("noches");
                fila[8] = rs.getDouble("precio_noche");
                fila[9] = rs.getDouble("precio_total");
                fila[10] = rs.getInt("cantidad_huespedes");
                fila[11] = rs.getString("estado");
                fila[12] = rs.getString("observaciones");

                modelo.addRow(fila);
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

            // Ocultar columnas técnicas
            tabla.getColumnModel().getColumn(1).setMinWidth(0);
            tabla.getColumnModel().getColumn(1).setMaxWidth(0);
            tabla.getColumnModel().getColumn(1).setWidth(0);

            tabla.getColumnModel().getColumn(2).setMinWidth(0);
            tabla.getColumnModel().getColumn(2).setMaxWidth(0);
            tabla.getColumnModel().getColumn(2).setWidth(0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al mostrar reservas: " + e.getMessage());
        }
    }

    public void guardarReserva(
            int idHuesped,
            int idHabitacion,
            int idUsuario,
            java.sql.Date fechaCheckIn,
            java.sql.Date fechaCheckOut,
            int cantidadHuespedes,
            String estado,
            String observaciones) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "INSERT INTO reservas "
                + "(id_huesped, id_habitacion, id_usuario, fecha_checkin, fecha_checkout, cantidad_huespedes, estado, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            if (!validarReserva(con, idHabitacion, fechaCheckIn, fechaCheckOut, cantidadHuespedes, 0)) {
                conexion.cerrarConexion();
                return;
            }

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idHuesped);
            ps.setInt(2, idHabitacion);
            ps.setInt(3, idUsuario);
            ps.setDate(4, fechaCheckIn);
            ps.setDate(5, fechaCheckOut);
            ps.setInt(6, cantidadHuespedes);
            ps.setString(7, estado);
            ps.setString(8, observaciones);

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Reserva guardada correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al guardar reserva: " + e.getMessage());
        }
    }

    public void modificarReserva(
            int idReserva,
            int idHuesped,
            int idHabitacion,
            java.sql.Date fechaCheckIn,
            java.sql.Date fechaCheckOut,
            int cantidadHuespedes,
            String estado,
            String observaciones) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "UPDATE reservas SET "
                + "id_huesped = ?, "
                + "id_habitacion = ?, "
                + "fecha_checkin = ?, "
                + "fecha_checkout = ?, "
                + "cantidad_huespedes = ?, "
                + "estado = ?, "
                + "observaciones = ? "
                + "WHERE id_reserva = ?";

        try {
            if (!validarReserva(con, idHabitacion, fechaCheckIn, fechaCheckOut, cantidadHuespedes, idReserva)) {
                conexion.cerrarConexion();
                return;
            }

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idHuesped);
            ps.setInt(2, idHabitacion);
            ps.setDate(3, fechaCheckIn);
            ps.setDate(4, fechaCheckOut);
            ps.setInt(5, cantidadHuespedes);
            ps.setString(6, estado);
            ps.setString(7, observaciones);
            ps.setInt(8, idReserva);

            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Reserva modificada correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al modificar reserva: " + e.getMessage());
        }
    }

    public void eliminarReserva(int idReserva) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "DELETE FROM reservas WHERE id_reserva = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idReserva);
            ps.executeUpdate();

            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Reserva eliminada correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar reserva: " + e.getMessage());
        }
    }

    private boolean validarReserva(Connection con,
            int idHabitacion,
            java.sql.Date fechaCheckIn,
            java.sql.Date fechaCheckOut,
            int cantidadHuespedes,
            int idReservaExcluir) throws Exception {

        if (fechaCheckIn == null || fechaCheckOut == null) {
            JOptionPane.showMessageDialog(null, "Debe ingresar fecha de check-in y check-out.");
            return false;
        }

        if (!fechaCheckOut.after(fechaCheckIn)) {
            JOptionPane.showMessageDialog(null, "La fecha de check-out debe ser posterior al check-in.");
            return false;
        }

        if (cantidadHuespedes <= 0) {
            JOptionPane.showMessageDialog(null, "La cantidad de huéspedes debe ser mayor a cero.");
            return false;
        }

        if (habitacionEnMantenimiento(con, idHabitacion)) {
            JOptionPane.showMessageDialog(null,
                    "No se puede reservar una habitación que se encuentra en mantenimiento.");
            return false;
        }

        int capacidad = obtenerCapacidadHabitacion(con, idHabitacion);

        if (cantidadHuespedes > capacidad) {
            JOptionPane.showMessageDialog(null,
                    "La cantidad de huéspedes supera la capacidad de la habitación seleccionada.\n"
                    + "Capacidad máxima: " + capacidad);
            return false;
        }

        if (haySuperposicion(con, idHabitacion, fechaCheckIn, fechaCheckOut, idReservaExcluir)) {
            JOptionPane.showMessageDialog(null,
                    "La habitación seleccionada ya posee una reserva en el rango de fechas indicado.");
            return false;
        }

        return true;
    }

    private boolean habitacionEnMantenimiento(Connection con, int idHabitacion) throws Exception {

        String sql = "SELECT estado FROM habitaciones WHERE id_habitacion = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idHabitacion);
        ResultSet rs = ps.executeQuery();

        boolean enMantenimiento = false;

        if (rs.next()) {
            String estado = rs.getString("estado");
            enMantenimiento = estado != null && estado.equalsIgnoreCase("Mantenimiento");
        }

        rs.close();
        ps.close();

        return enMantenimiento;
    }

    private int obtenerCapacidadHabitacion(Connection con, int idHabitacion) throws Exception {

        String sql = "SELECT capacidad FROM habitaciones WHERE id_habitacion = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idHabitacion);
        ResultSet rs = ps.executeQuery();

        int capacidad = 0;

        if (rs.next()) {
            capacidad = rs.getInt("capacidad");
        }

        rs.close();
        ps.close();

        return capacidad;
    }

    private boolean haySuperposicion(Connection con,
            int idHabitacion,
            java.sql.Date fechaCheckIn,
            java.sql.Date fechaCheckOut,
            int idReservaExcluir) throws Exception {

        String sql = "SELECT COUNT(*) AS total "
                + "FROM reservas "
                + "WHERE id_habitacion = ? "
                + "AND estado <> 'Cancelada' "
                + "AND (? < fecha_checkout AND ? > fecha_checkin)";

        if (idReservaExcluir > 0) {
            sql += " AND id_reserva <> ?";
        }

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idHabitacion);
        ps.setDate(2, fechaCheckIn);
        ps.setDate(3, fechaCheckOut);

        if (idReservaExcluir > 0) {
            ps.setInt(4, idReservaExcluir);
        }

        ResultSet rs = ps.executeQuery();

        boolean existeSuperposicion = false;

        if (rs.next()) {
            existeSuperposicion = rs.getInt("total") > 0;
        }

        rs.close();
        ps.close();

        return existeSuperposicion;
    }
}
