package EstilosExcelPDF;

import DatosP.Dsalidap;
import Logica.ConfiguradorApi;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.DefaultComboBoxModel;
import javax.swing.*;
import java.io.File;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class Metodo_Excel_xlsx {

    public static void llenarcboturno(JComboBox<String> combo) {
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        combo.setModel(modelo);
        modelo.addElement("Seleccione un turno");

        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String urlString = baseUrl + "/turno/ultimos";

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    Gson gson = new Gson();
                    String[] turnos = gson.fromJson(response.toString(), String[].class);

                    for (String turno : turnos) {
                        modelo.addElement(turno);
                    }
                }
            } else {
                System.err.println("Error al obtener turnos: " + conn.getResponseCode());
            }

        } catch (JsonSyntaxException | IOException e) {
            System.err.println("Excepción al llenar combo de turnos: " + e.getMessage());
        }
    }

    public DefaultTableModel exportarReporteDia(JComboBox<String> turno1, JComboBox<String> turno2, JComboBox<String> turno3, JTable tablalistado) {
        DefaultTableModel model = (DefaultTableModel) tablalistado.getModel();
        model.setRowCount(0); // Limpiar la tabla

        String turnoSeleccionado1 = (String) turno1.getSelectedItem();
        String turnoSeleccionado2 = (String) turno2.getSelectedItem();
        String turnoSeleccionado3 = (String) turno3.getSelectedItem();

        String[] titulos = {"PLACA", "TIPO VEHI", "TIPO SERVI", "FECHA ENTRA", "FECHA SALI", "ZONA",
            "EFECTIVO", "TARJETA", "TRANSFERENCIA", "TOTAL", "RESPONSABLE SALIDA"};
        model.setColumnIdentifiers(titulos);

        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String ruta = "/api/salidas/escritorio/reporte-dia";

            // Construimos la URL con parámetros
            String urlString = String.format("%s%s?turno1=%s&turno2=%s&turno3=%s",
                    baseUrl,
                    ruta,
                    URLEncoder.encode(turnoSeleccionado1, StandardCharsets.UTF_8),
                    URLEncoder.encode(turnoSeleccionado2, StandardCharsets.UTF_8),
                    URLEncoder.encode(turnoSeleccionado3, StandardCharsets.UTF_8)
            );

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonResponse.append(line);
                }
                reader.close();

                // 🔹 Usamos Gson para convertir el JSON en lista de objetos Dsalidap
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Dsalidap>>() {
                }.getType();
                List<Dsalidap> salidas = gson.fromJson(jsonResponse.toString(), listType);

                // 🔹 Llenamos la tabla
                for (Dsalidap s : salidas) {
                    Object[] registro = {
                        s.getPlaca(),
                        s.getTipovehiculo(),
                        s.getTiposervicio(),
                        s.getFechaentrada(),
                        s.getFechasalida(),
                        s.getZona(),
                        s.getEfectivo(),
                        s.getTarjeta(),
                        s.getTransferencia(),
                        s.getTotal(),
                        s.getEmpleadosalida()
                    };
                    model.addRow(registro);
                }

            } else if (conn.getResponseCode() == 204) {
                JOptionPane.showMessageDialog(null, "No hay datos disponibles para el reporte seleccionado.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al consultar reporte Método excel: " + conn.getResponseCode());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al exportar reporte Método excel: " + e.getMessage());
            e.printStackTrace();
        }

        return model;
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }

    public void exportarExcel(JTable tablalistado) {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(tablalistado);
            File saveFile = jFileChooser.getSelectedFile();
            if (saveFile != null) {
                saveFile = new File(saveFile.toString() + ".xlsx");
                FileOutputStream out;
                try (XSSFWorkbook wb = new XSSFWorkbook()) {
                    Sheet sheet = wb.createSheet("Reporte");
                    Row rowCol = sheet.createRow(0);
                    // Crear una fuente personalizada
                    XSSFFont fuenteTitulo = new GeneradorFuente.Builder()
                            .setNombreFuente("Arial")
                            .setConNegrita(true)
                            .setTamanioFuente((short) 12)
                            .setColorDefecto(IndexedColors.WHITE.getIndex())
                            .build(wb);
                    // Crear un estilo personalizado
                    XSSFCellStyle estiloTitulo = new GeneradorEstilo.Builder()
                            .setFuente(fuenteTitulo)
                            .setColorDefecto(IndexedColors.BLUE.getIndex())
                            .setTipoPatron(FillPatternType.SOLID_FOREGROUND)
                            .setAlineacionHorizontal(HorizontalAlignment.CENTER)
                            .setAlineacionVertical(VerticalAlignment.CENTER)
                            .setBordeArriba(BorderStyle.THIN)
                            .setBordeAbajo(BorderStyle.THIN)
                            .setBordeDerecho(BorderStyle.THIN)
                            .setBordeIzquierdo(BorderStyle.THIN)
                            .build(wb);
                    // Aplicar estilo a los títulos
                    for (int i = 0; i < tablalistado.getColumnCount(); i++) {
                        Cell cell = rowCol.createCell(i);
                        cell.setCellValue(tablalistado.getColumnName(i));
                        cell.setCellStyle(estiloTitulo);
                    }   // Estilo para los datos
                    XSSFFont fuenteDatos = new GeneradorFuente.Builder()
                            .setNombreFuente("Calibri")
                            .setTamanioFuente((short) 12)
                            .build(wb);
                    XSSFCellStyle estiloDatos = new GeneradorEstilo.Builder()
                            .setFuente(fuenteDatos)
                            .setBordeArriba(BorderStyle.THIN)
                            .setBordeAbajo(BorderStyle.THIN)
                            .setBordeDerecho(BorderStyle.THIN)
                            .setBordeIzquierdo(BorderStyle.THIN)
                            .setAlineacionHorizontal(HorizontalAlignment.LEFT)
                            .setAlineacionVertical(VerticalAlignment.CENTER)
                            .build(wb);
                    // Llenar los datos
                    for (int j = 0; j < tablalistado.getRowCount(); j++) {
                        Row row = sheet.createRow(j + 1);
                        for (int k = 0; k < tablalistado.getColumnCount(); k++) {
                            Cell cell = row.createCell(k);
                            if (tablalistado.getValueAt(j, k) != null) {
                                cell.setCellValue(tablalistado.getValueAt(j, k).toString());
                            }
                            cell.setCellStyle(estiloDatos);
                        }
                    }   // Ajustar automáticamente el ancho de las columnas
                    for (int i = 0; i < tablalistado.getColumnCount(); i++) {
                        sheet.autoSizeColumn(i);
                    }   // Guardar el archivo
                    out = new FileOutputStream(new File(saveFile.toString()));
                    wb.write(out);
                }
                out.close();
                openFile(saveFile.toString());
            } else {
                JOptionPane.showMessageDialog(null, "Error al generar el archivo Excel");
            }
        } catch (HeadlessException | IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
// public void exportarJTableAPDF(JTable table, String rutaArchivo) {
//        try {
//            // Crear documento PDF
//            PdfWriter writer = new PdfWriter(new FileOutputStream(rutaArchivo));
//            PdfDocument pdf = new PdfDocument(writer);
//            Document document = new Document(pdf);
//
//            // Agregar título al documento
//            Paragraph titulo = new Paragraph("Reporte de Datos")
//                    .setTextAlignment(TextAlignment.CENTER)
//                    .setBold()
//                    .setFontSize(14);
//            document.add(titulo);
//
//            // Crear tabla PDF con el mismo número de columnas que el JTable
//            Table pdfTable = new Table(table.getColumnCount());
//
//            // Agregar encabezados de la tabla
//            TableModel model = table.getModel();
//            for (int i = 0; i < model.getColumnCount(); i++) {
//                pdfTable.addCell(new Cell().add(new Paragraph(model.getColumnName(i)).setBold()));
//            }
//
//            // Agregar filas al PDF
//            for (int row = 0; row < model.getRowCount(); row++) {
//                for (int col = 0; col < model.getColumnCount(); col++) {
//                    pdfTable.addCell(new Cell().add(new Paragraph(model.getValueAt(row, col).toString())));
//                }
//            }
//
//            // Agregar la tabla al documento
//            document.add(pdfTable);
//            document.close();
//
//            System.out.println("¡PDF generado con éxito en: " + rutaArchivo);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
