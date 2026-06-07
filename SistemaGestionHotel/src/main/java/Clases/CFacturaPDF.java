package Clases;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class CFacturaPDF {

    public void exportarFacturaPDF(int idFactura) {

        CConexion conexion = new CConexion();
        Connection con = conexion.estableceConexion();

        String sql = "SELECT "
                + "f.id_factura, "
                + "f.fecha_emision, "
                + "f.tipo_factura, "
                + "f.importe_total, "
                + "f.estado_factura, "
                + "r.id_reserva, "
                + "r.fecha_checkin, "
                + "r.fecha_checkout, "
                + "DATEDIFF(r.fecha_checkout, r.fecha_checkin) AS noches, "
                + "h.nombre, "
                + "h.apellido, "
                + "h.dni, "
                + "hb.numero AS habitacion, "
                + "hb.tipo, "
                + "hb.precio AS precio_noche, "
                + "p.medio_pago, "
                + "p.estado_pago "
                + "FROM facturas f "
                + "INNER JOIN reservas r ON f.id_reserva = r.id_reserva "
                + "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped "
                + "INNER JOIN habitaciones hb ON r.id_habitacion = hb.id_habitacion "
                + "LEFT JOIN pagos p ON r.id_reserva = p.id_reserva "
                + "WHERE f.id_factura = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idFactura);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "No se encontró la factura.");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar factura PDF");
            fileChooser.setSelectedFile(new File("Factura_" + idFactura + ".pdf"));

            int seleccion = fileChooser.showSaveDialog(null);

            if (seleccion != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File archivo = fileChooser.getSelectedFile();

            if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
                archivo = new File(archivo.getAbsolutePath() + ".pdf");
            }

            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream(archivo));

            documento.open();

            Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font subtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font negrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

            Paragraph encabezado = new Paragraph("SISTEMA DE GESTIÓN HOTELERA", titulo);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            encabezado.setSpacingAfter(6);
            documento.add(encabezado);

            Paragraph sub = new Paragraph("Factura de estadía", subtitulo);
            sub.setAlignment(Element.ALIGN_CENTER);
            sub.setSpacingAfter(20);
            documento.add(sub);

            PdfPTable tablaDatosFactura = new PdfPTable(2);
            tablaDatosFactura.setWidthPercentage(100);

            agregarCelda(tablaDatosFactura, "N° Factura:", negrita);
            agregarCelda(tablaDatosFactura, String.valueOf(rs.getInt("id_factura")), normal);

            agregarCelda(tablaDatosFactura, "Fecha de emisión:", negrita);
            agregarCelda(tablaDatosFactura, rs.getDate("fecha_emision").toString(), normal);

            agregarCelda(tablaDatosFactura, "Tipo de factura:", negrita);
            agregarCelda(tablaDatosFactura, rs.getString("tipo_factura"), normal);

            agregarCelda(tablaDatosFactura, "Estado:", negrita);
            agregarCelda(tablaDatosFactura, rs.getString("estado_factura"), normal);

            documento.add(tablaDatosFactura);

            Paragraph datosHuesped = new Paragraph("Datos del huésped", subtitulo);
            datosHuesped.setSpacingBefore(18);
            datosHuesped.setSpacingAfter(8);
            documento.add(datosHuesped);

            PdfPTable tablaHuesped = new PdfPTable(2);
            tablaHuesped.setWidthPercentage(100);

            agregarCelda(tablaHuesped, "Nombre y apellido:", negrita);
            agregarCelda(tablaHuesped,
                    rs.getString("nombre") + " " + rs.getString("apellido"),
                    normal);

            agregarCelda(tablaHuesped, "DNI:", negrita);
            agregarCelda(tablaHuesped, rs.getString("dni"), normal);

            documento.add(tablaHuesped);

            Paragraph datosReserva = new Paragraph("Detalle de la reserva", subtitulo);
            datosReserva.setSpacingBefore(18);
            datosReserva.setSpacingAfter(8);
            documento.add(datosReserva);

            PdfPTable tablaReserva = new PdfPTable(2);
            tablaReserva.setWidthPercentage(100);

            agregarCelda(tablaReserva, "N° Reserva:", negrita);
            agregarCelda(tablaReserva, String.valueOf(rs.getInt("id_reserva")), normal);

            agregarCelda(tablaReserva, "Habitación:", negrita);
            agregarCelda(tablaReserva,
                    rs.getString("habitacion") + " - " + rs.getString("tipo"),
                    normal);

            agregarCelda(tablaReserva, "Check-in:", negrita);
            agregarCelda(tablaReserva, rs.getDate("fecha_checkin").toString(), normal);

            agregarCelda(tablaReserva, "Check-out:", negrita);
            agregarCelda(tablaReserva, rs.getDate("fecha_checkout").toString(), normal);

            agregarCelda(tablaReserva, "Noches:", negrita);
            agregarCelda(tablaReserva, String.valueOf(rs.getInt("noches")), normal);

            agregarCelda(tablaReserva, "Precio por noche:", negrita);
            agregarCelda(tablaReserva, "$" + rs.getDouble("precio_noche"), normal);

            documento.add(tablaReserva);

            Paragraph datosPago = new Paragraph("Datos del pago", subtitulo);
            datosPago.setSpacingBefore(18);
            datosPago.setSpacingAfter(8);
            documento.add(datosPago);

            PdfPTable tablaPago = new PdfPTable(2);
            tablaPago.setWidthPercentage(100);

            agregarCelda(tablaPago, "Medio de pago:", negrita);
            agregarCelda(tablaPago,
                    rs.getString("medio_pago") == null ? "No registrado" : rs.getString("medio_pago"),
                    normal);

            agregarCelda(tablaPago, "Estado del pago:", negrita);
            agregarCelda(tablaPago,
                    rs.getString("estado_pago") == null ? "No registrado" : rs.getString("estado_pago"),
                    normal);

            documento.add(tablaPago);

            Paragraph espacioTotal = new Paragraph(" ");
            espacioTotal.setSpacingBefore(18);
            documento.add(espacioTotal);

            PdfPTable tablaTotal = new PdfPTable(2);
            tablaTotal.setWidthPercentage(100);

            agregarCelda(tablaTotal, "TOTAL A FACTURAR:", negrita);
            agregarCelda(tablaTotal, "$" + rs.getDouble("importe_total"), negrita);

            documento.add(tablaTotal);

            Paragraph pie = new Paragraph(
                    "Documento generado automáticamente por el Sistema de Gestión Hotelera.",
                    normal);
            pie.setSpacingBefore(24);
            documento.add(pie);

            documento.close();

            rs.close();
            ps.close();
            conexion.cerrarConexion();

            JOptionPane.showMessageDialog(null, "Factura exportada correctamente.");

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(archivo);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al exportar factura PDF: " + e.getMessage());
        }
    }

    private void agregarCelda(PdfPTable tabla, String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setPadding(6);
        tabla.addCell(celda);
    }
}