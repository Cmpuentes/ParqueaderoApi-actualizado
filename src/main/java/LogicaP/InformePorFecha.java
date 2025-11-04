package LogicaP;

import Datos.InformeDetalleDTO;
import Logica.ConfiguradorApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//POR REVISAR, HAY DOS CLASES CON ESTE NOMBRE
public class InformePorFecha {

//    private final Cconexionp mysql = new Cconexionp();
//    private final Connection cn = mysql.establecerConexionp();
//    private String sSQL = "";

//    public DefaultTableModel obtenerRegistrosPorFechas(String fechaInicio, String fechaFin, JTable tablalistadoinfo) {
//        DefaultTableModel model = (DefaultTableModel) tablalistadoinfo.getModel();
//        model.setRowCount(0);  // Limpiar la tabla antes de llenarla
//
//        String[] titulos = {"TURNO", "NUMERO_TURNO", "EMPLEADO", "FECHA INICIO", "FECHA SALIDA", "EFECTIVO", "TARJETA", "TRANSFERENCIA", "OTROS INGRESOS", "EFECTIVO LIQUIDO", "TOTAL RECAUDADO"};
//        model.setColumnIdentifiers(titulos);
//
//        // Construir la consulta SQL de forma dinámica según si solo se pasa fechaInicio o ambos parámetros
//         sSQL = "SELECT turno, numero_turno, empleado, fechaingreso, fechasalida, "
//                + "SUM(efectivo) AS efectivo, SUM(tarjeta) AS tarjeta, SUM(transferencia) AS transferencia, "
//                + "SUM(otros_ingresos) AS otros_ingresos, SUM(efectivo_liquido) AS efectivo_liquido, "
//                + "SUM(total_recaudado) AS total_recaudado "
//                + "FROM salida_turno "
//                + "WHERE DATE(STR_TO_DATE(fechasalida, '%d-%m-%Y %h:%i %p')) >= STR_TO_DATE(?, '%d-%m-%Y') ";
//
//        // Si se ha pasado fechaFin, agregar el filtro correspondiente
//        if (fechaFin != null && !fechaFin.isEmpty()) {
//            sSQL += "AND DATE(STR_TO_DATE(fechasalida, '%d-%m-%Y %h:%i %p')) <= STR_TO_DATE(?, '%d-%m-%Y') ";
//        }
//
//        sSQL += "GROUP BY turno, numero_turno, empleado, fechaingreso, fechasalida "
//                + "UNION ALL "
//                + "SELECT 'TOTAL' AS turno, NULL AS numero_turno, NULL AS empleado, NULL AS fechaingreso, NULL AS fechasalida, "
//                + "SUM(efectivo) AS efectivo, SUM(tarjeta) AS tarjeta, SUM(transferencia) AS transferencia, "
//                + "SUM(otros_ingresos) AS otros_ingresos, SUM(efectivo_liquido) AS efectivo_liquido, "
//                + "SUM(total_recaudado) AS total_recaudado "
//                + "FROM salida_turno "
//                + "WHERE DATE(STR_TO_DATE(fechasalida, '%d-%m-%Y %h:%i %p')) >= STR_TO_DATE(?, '%d-%m-%Y') ";
//
//        // Si se ha pasado fechaFin, agregar el filtro correspondiente
//        if (fechaFin != null && !fechaFin.isEmpty()) {
//            sSQL += "AND DATE(STR_TO_DATE(fechasalida, '%d-%m-%Y %h:%i %p')) <= STR_TO_DATE(?, '%d-%m-%Y') ";
//        }
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            // Siempre se pasa la fecha de inicio
//            pst.setString(1, fechaInicio);
//
//            // Si fechaFin se ha pasado, también se debe incluir en los parámetros
//            if (fechaFin != null && !fechaFin.isEmpty()) {
//                pst.setString(2, fechaFin);
//                pst.setString(3, fechaInicio); // Fecha de inicio para la segunda parte de la consulta
//                pst.setString(4, fechaFin); // Fecha de fin para la segunda parte de la consulta
//            } else {
//                // Si solo se pasa fechaInicio, solo asignamos los primeros parámetros
//                pst.setString(2, fechaInicio);
//            }
//
//            // Ejecutar la consulta
//            ResultSet rs = pst.executeQuery();
//            while (rs.next()) {
//                String[] registro = new String[11]; // Corregir el número de columnas
//                registro[0] = rs.getString("turno");
//                registro[1] = rs.getString("numero_turno");
//                registro[2] = rs.getString("empleado");
//                registro[3] = rs.getString("fechaingreso");
//                registro[4] = rs.getString("fechasalida");
//                registro[5] = String.valueOf(rs.getDouble("efectivo"));
//                registro[6] = String.valueOf(rs.getDouble("tarjeta"));
//                registro[7] = String.valueOf(rs.getDouble("transferencia"));
//                registro[8] = String.valueOf(rs.getDouble("otros_ingresos"));
//                registro[9] = String.valueOf(rs.getDouble("efectivo_liquido"));
//                registro[10] = String.valueOf(rs.getDouble("total_recaudado"));
//
//                model.addRow(registro);
//            }
//            return model;
//        } catch (SQLException e) {
//        }
//        return null;
//    }
//       public void openFile(String file) {
//        try {
//            File path = new File(file);
//            Desktop.getDesktop().open(path);
//        } catch (IOException ioe) {
//            System.out.println(ioe);
//        }
//
//    }
    //FUNCIÓN obtenerRegistrosPorFechas MIGRADA
    public DefaultTableModel obtenerRegistrosPorFechas(String fechaInicio, String fechaFin, JTable tablalistadoinfo) {
        String[] titulos = {"TURNO", "NUMERO_TURNO", "EMPLEADO", "FECHA INICIO", "FECHA SALIDA", "EFECTIVO", "TARJETA", "TRANSFERENCIA", "OTROS INGRESOS", "EFECTIVO LIQUIDO", "TOTAL RECAUDADO"};
        DefaultTableModel model = new DefaultTableModel(null, titulos);

        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String endpointDetalle;
            String endpointTotales;

            if (fechaFin == null || fechaFin.isEmpty()) {
                endpointDetalle = baseUrl + "/api/escritorio/informes/detalle-desde?inicio=" + fechaInicio;
                endpointTotales = baseUrl + "/api/escritorio/informes/totales-desde?inicio=" + fechaInicio;
            } else {
                endpointDetalle = baseUrl + "/api/escritorio/informes/detalle-entre?inicio=" + fechaInicio + "&fin=" + fechaFin;
                endpointTotales = baseUrl + "/api/escritorio/informes/totales-entre?inicio=" + fechaInicio + "&fin=" + fechaFin;
            }

            // -------- DETALLES --------
            URL urlDetalle = new URL(endpointDetalle);
            HttpURLConnection connDetalle = (HttpURLConnection) urlDetalle.openConnection();
            connDetalle.setRequestMethod("GET");
            connDetalle.setRequestProperty("Accept", "application/json");

            if (connDetalle.getResponseCode() == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connDetalle.getInputStream(), StandardCharsets.UTF_8))) {
                    String response = br.lines().collect(Collectors.joining());
                    Gson gson = new Gson();
                    List<InformeDetalleDTO> registros = gson.fromJson(response, new TypeToken<List<InformeDetalleDTO>>() {
                    }.getType());

                    for (InformeDetalleDTO fila : registros) {
                        model.addRow(new Object[]{
                            fila.getTurno(),
                            fila.getNumeroTurno(),
                            fila.getEmpleado(),
                            fila.getFechaIngreso(),
                            fila.getFechaSalida(),
                            fila.getEfectivo(),
                            fila.getTarjeta(),
                            fila.getTransferencia(),
                            fila.getOtrosIngresos(),
                            fila.getEfectivoLiquido(),
                            fila.getTotalRecaudado()
                        });
                    }
                }
            }

            // -------- TOTALES --------
            URL urlTotales = new URL(endpointTotales);
            HttpURLConnection connTotales = (HttpURLConnection) urlTotales.openConnection();
            connTotales.setRequestMethod("GET");
            connTotales.setRequestProperty("Accept", "application/json");

            if (connTotales.getResponseCode() == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connTotales.getInputStream(), StandardCharsets.UTF_8))) {
                    String response = br.lines().collect(Collectors.joining());
                    Gson gson = new Gson();
                    Map<String, Object> totales = gson.fromJson(response, new TypeToken<Map<String, Object>>() {
                    }.getType());

                    model.addRow(new Object[]{
                        "TOTAL", "", "", "", "",
                        totales.get("efectivo"),
                        totales.get("tarjeta"),
                        totales.get("transferencia"),
                        totales.get("otrosIngresos"),
                        totales.get("efectivoLiquido"),
                        totales.get("totalRecaudado")
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener registros: " + e.getMessage());
        }

        return model;
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
                    // Crear una fuente personalizada para el encabezado
                    XSSFFont fuenteEncabezado = wb.createFont();
                    fuenteEncabezado.setFontName("Arial");
                    fuenteEncabezado.setBold(true);
                    fuenteEncabezado.setFontHeightInPoints((short) 16);
                    fuenteEncabezado.setColor(IndexedColors.WHITE.getIndex());
                    // Crear un estilo para el encabezado
                    XSSFCellStyle estiloEncabezado = wb.createCellStyle();
                    estiloEncabezado.setFont(fuenteEncabezado);
                    estiloEncabezado.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
                    estiloEncabezado.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
                    estiloEncabezado.setVerticalAlignment(VerticalAlignment.CENTER);
                    // Crear una fila para el encabezado
                    Row rowEncabezado = sheet.createRow(0);
                    rowEncabezado.setHeightInPoints(50); // Ajustar altura de la fila
                    Cell cellEncabezado = rowEncabezado.createCell(0);
                    cellEncabezado.setCellValue("SISTEMA GESNNOVA - PITS PARKING");
                    cellEncabezado.setCellStyle(estiloEncabezado);
                    // Fusionar celdas para el encabezado (toda la fila de las columnas)
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, tablalistado.getColumnCount() - 1));
                    // Crear una fila para el subtítulo
                    Row rowSubtitulo = sheet.createRow(1);
                    rowSubtitulo.setHeightInPoints(30); // Ajustar altura de la fila
                    Cell cellSubtitulo = rowSubtitulo.createCell(0);
                    cellSubtitulo.setCellValue("Reporte de Movimientos");
                    cellSubtitulo.setCellStyle(estiloEncabezado);
                    // Combinar celdas para el subtítulo
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, tablalistado.getColumnCount() - 1));
                    // Crear fila para los nombres de las columnas
                    Row rowCol = sheet.createRow(2);
                    // Crear una fuente personalizada para los títulos
                    XSSFFont fuenteTitulo = wb.createFont();
                    fuenteTitulo.setFontName("Arial");
                    fuenteTitulo.setBold(true);
                    fuenteTitulo.setFontHeightInPoints((short) 12);
                    fuenteTitulo.setColor(IndexedColors.WHITE.getIndex());
                    // Crear un estilo para los títulos de las columnas
                    XSSFCellStyle estiloTitulo = wb.createCellStyle();
                    estiloTitulo.setFont(fuenteTitulo);
                    estiloTitulo.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                    estiloTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    estiloTitulo.setAlignment(HorizontalAlignment.CENTER);
                    estiloTitulo.setVerticalAlignment(VerticalAlignment.CENTER);
                    estiloTitulo.setBorderTop(BorderStyle.THIN);
                    estiloTitulo.setBorderBottom(BorderStyle.THIN);
                    estiloTitulo.setBorderLeft(BorderStyle.THIN);
                    estiloTitulo.setBorderRight(BorderStyle.THIN);
                    // Crear celdas para los títulos de las columnas
                    for (int i = 0; i < tablalistado.getColumnCount(); i++) {
                        Cell cell = rowCol.createCell(i);
                        cell.setCellValue(tablalistado.getColumnName(i));
                        cell.setCellStyle(estiloTitulo);
                    }   // Crear una fuente para los datos
                    XSSFFont fuenteDatos = wb.createFont();
                    fuenteDatos.setFontName("Calibri");
                    fuenteDatos.setFontHeightInPoints((short) 11);
                    // Crear un estilo para los datos
                    XSSFCellStyle estiloDatos = wb.createCellStyle();
                    estiloDatos.setFont(fuenteDatos);
                    estiloDatos.setBorderTop(BorderStyle.THIN);
                    estiloDatos.setBorderBottom(BorderStyle.THIN);
                    estiloDatos.setBorderLeft(BorderStyle.THIN);
                    estiloDatos.setBorderRight(BorderStyle.THIN);
                    estiloDatos.setAlignment(HorizontalAlignment.LEFT);
                    estiloDatos.setVerticalAlignment(VerticalAlignment.CENTER);
                    // Llenar los datos
                    for (int j = 0; j < tablalistado.getRowCount(); j++) {
                        Row row = sheet.createRow(j + 3); // +2 para dejar espacio para el encabezado y los títulos
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

    private void openFile(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
