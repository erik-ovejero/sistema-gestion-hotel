package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

public class CReporteDAO {

    public int obtenerTotalHabitaciones() {

        int total = 0;

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "SELECT COUNT(*) AS total FROM habitaciones";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Error al obtener total de habitaciones: " + e.getMessage());
        }

        return total;
    }

    public int obtenerDiasOcupados(Date fechaDesde, Date fechaHasta) {

        int diasOcupados = 0;

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql =
                "SELECT SUM( "
                + "DATEDIFF( "
                + "LEAST(fecha_checkout, ?), "
                + "GREATEST(fecha_checkin, ?) "
                + ") "
                + ") AS dias_ocupados "
                + "FROM reservas "
                + "WHERE fecha_checkin < ? "
                + "AND fecha_checkout > ? "
                + "AND estado <> 'Cancelada'";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDate(1, fechaHasta);
            ps.setDate(2, fechaDesde);
            ps.setDate(3, fechaHasta);
            ps.setDate(4, fechaDesde);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                diasOcupados = rs.getInt("dias_ocupados");
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Error al obtener días ocupados: " + e.getMessage());
        }

        return diasOcupados;
    }

    public double obtenerMontoGenerado(Date fechaDesde, Date fechaHasta) {

        double monto = 0;

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql =
                "SELECT SUM( "
                + "DATEDIFF( "
                + "LEAST(r.fecha_checkout, ?), "
                + "GREATEST(r.fecha_checkin, ?) "
                + ") * h.precio "
                + ") AS monto_generado "
                + "FROM reservas r "
                + "INNER JOIN habitaciones h ON r.id_habitacion = h.id_habitacion "
                + "WHERE r.fecha_checkin < ? "
                + "AND r.fecha_checkout > ? "
                + "AND r.estado <> 'Cancelada'";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDate(1, fechaHasta);
            ps.setDate(2, fechaDesde);
            ps.setDate(3, fechaHasta);
            ps.setDate(4, fechaDesde);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                monto = rs.getDouble("monto_generado");
            }

            rs.close();
            ps.close();
            conexion.cerrarConexion();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Error al obtener monto generado: " + e.getMessage());
        }

        return monto;
    }
}