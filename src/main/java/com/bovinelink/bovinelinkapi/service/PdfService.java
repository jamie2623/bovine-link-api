package com.bovinelink.bovinelinkapi.service;

import com.bovinelink.bovinelinkapi.dto.ReporteFiltroRequest;
import com.bovinelink.bovinelinkapi.dto.ReporteItemResponse;
import com.bovinelink.bovinelinkapi.entity.Publicacion;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Generacion del PDF del reporte de publicaciones con iText 8.
 */
@Service
public class PdfService {

    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    // El Salvador usa USD. El locale es_SV mapea a la moneda historica (colon, "C"),
    // por eso se fuerza formato de dolar: "$1,234.56".
    private static final Locale LOCALE_MONEDA = Locale.US;

    private static final String[] ENCABEZADOS =
            {"ID", "Titulo", "Raza", "Precio", "Ubicacion", "Estado", "Vendedor", "Fecha"};
    private static final float[] COLUMNAS =
            {5f, 20f, 13f, 12f, 16f, 9f, 15f, 12f};

    public byte[] generarReporteDePublicaciones(List<ReporteItemResponse> filas, ReporteFiltroRequest filtro) {
        NumberFormat moneda = NumberFormat.getCurrencyInstance(LOCALE_MONEDA);

        try (ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
            PdfDocument pdf = new PdfDocument(new PdfWriter(salida));
            Document doc = new Document(pdf, PageSize.A4.rotate());

            doc.add(new Paragraph("Bovine Link - Reporte de publicaciones")
                    .setBold()
                    .setFontSize(16));
            doc.add(new Paragraph(construirSubtitulo(filtro))
                    .setFontSize(9)
                    .setFontColor(ColorConstants.GRAY));

            Table tabla = new Table(UnitValue.createPercentArray(COLUMNAS)).useAllAvailableWidth();
            for (String encabezado : ENCABEZADOS) {
                tabla.addHeaderCell(celdaEncabezado(encabezado));
            }

            BigDecimal montoTotal = BigDecimal.ZERO;
            for (ReporteItemResponse fila : filas) {
                tabla.addCell(celda(String.valueOf(fila.getId())));
                tabla.addCell(celda(fila.getTitulo()));
                tabla.addCell(celda(fila.getRaza()));
                tabla.addCell(celda(moneda.format(fila.getPrecio())).setTextAlignment(TextAlignment.RIGHT));
                tabla.addCell(celda(fila.getUbicacion()));
                tabla.addCell(celda(etiquetaEstado(fila.getEstado())));
                tabla.addCell(celda(fila.getVendedor()));
                tabla.addCell(celda(fila.getFechaPublicacion() == null
                        ? "" : fila.getFechaPublicacion().format(FECHA_HORA)));
                montoTotal = montoTotal.add(fila.getPrecio());
            }
            doc.add(tabla);

            doc.add(new Paragraph("Registros: " + filas.size()
                    + "     Monto total: " + moneda.format(montoTotal))
                    .setBold()
                    .setFontSize(11)
                    .setMarginTop(10));

            doc.close();
            return salida.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo generar el PDF del reporte", e);
        }
    }

    private String construirSubtitulo(ReporteFiltroRequest filtro) {
        String desde = filtro.getDesde() == null ? "inicio" : filtro.getDesde().toString();
        String hasta = filtro.getHasta() == null ? "hoy" : filtro.getHasta().toString();
        String estado = (filtro.getEstado() == null || filtro.getEstado().isBlank())
                ? "todos" : filtro.getEstado();
        return "Rango: " + desde + " a " + hasta
                + "   |   Estado: " + estado
                + "   |   Generado: " + LocalDateTime.now().format(FECHA_HORA);
    }

    private Cell celdaEncabezado(String texto) {
        return new Cell()
                .add(new Paragraph(texto).setBold().setFontSize(9))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY);
    }

    private Cell celda(String texto) {
        return new Cell().add(new Paragraph(texto == null ? "" : texto).setFontSize(8));
    }

    private String etiquetaEstado(Publicacion.Estado estado) {
        return switch (estado) {
            case EN_VENTA -> "En venta";
            case VENDIDO -> "Vendido";
        };
    }
}
