package LogicaP;

import EstilosExcelPDF.GeneradorEstilo;
import EstilosExcelPDF.GeneradorFuente;
import LogicaP.Cconexionp;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Reporte_Prepagos {

//    private final Cconexionp mysql = new Cconexionp();
//    private final Connection cn = mysql.establecerConexionp();
//    private String sSQL = "";

//    public DefaultTableModel exportarReportePrepagos(JTable TablaPrepago) {
//
//        DefaultTableModel model = (DefaultTableModel) TablaPrepago.getModel();
//        model.setRowCount(0); // Limpiar la tabla antes de llenarla
//
//        // Definir los títulos de la tabla
//        String[] titulos = {"PLACA", "CLIENTE", "TIPO VEHI", "TIPO SERVI", "FECHA ENTRA", "FECHA SALIDA",
//            "VALOR", "DIAS", "TURNO", "EMPLEADO"};
//        model.setColumnIdentifiers(titulos);
//
//        // Consulta SQL con parámetros
//        sSQL = "SELECT placa, cliente, tipovehiculo, tiposervicio, fechaentrada, fechasalida, valor, dias, turno, empleadosalida "
//                + "FROM salida WHERE tiposervicio = 'PREPAGO' "
//                + "ORDER BY idsalida DESC";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            ResultSet rs = pst.executeQuery();
//
//            while (rs.next()) {
//                String[] registro = new String[10];
//                registro[0] = rs.getString("placa");
//                registro[1] = rs.getString("cliente");
//                registro[2] = rs.getString("tipovehiculo");
//                registro[3] = rs.getString("tiposervicio");
//                registro[4] = rs.getString("fechaentrada");
//                registro[5] = rs.getString("fechasalida");
//                registro[6] = rs.getString("valor");
//                registro[7] = rs.getString("dias");
//                registro[8] = rs.getString("turno");
//                registro[9] = rs.getString("empleadosalida");
//
//                model.addRow(registro);
//            }
//            // Aplicar el sorter para permitir filtrado
//            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
//            TablaPrepago.setRowSorter(sorter);
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error en la consulta: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
//        }
//
//        return model;
//    }

//    public void openFile(String file) {
//        try {
//            File path = new File(file);
//            Desktop.getDesktop().open(path);
//        } catch (IOException ioe) {
//            System.out.println(ioe);
//        }
//
//    }

//    public void exportarExcel(JTable TablaPrepago) {
//        try {
//            JFileChooser jFileChooser = new JFileChooser();
//            jFileChooser.showSaveDialog(TablaPrepago);
//            File saveFile = jFileChooser.getSelectedFile();
//            if (saveFile != null) {
//                saveFile = new File(saveFile.toString() + ".xlsx");
//                FileOutputStream out;
//                try (XSSFWorkbook wb = new XSSFWorkbook()) {
//                    Sheet sheet = wb.createSheet("Reporte");
//                    Row rowCol = sheet.createRow(0);
//                    // Crear una fuente personalizada
//                    XSSFFont fuenteTitulo = new GeneradorFuente.Builder()
//                            .setNombreFuente("Arial")
//                            .setConNegrita(true)
//                            .setTamanioFuente((short) 12)
//                            .setColorDefecto(IndexedColors.WHITE.getIndex())
//                            .build(wb);
//                    // Crear un estilo personalizado
//                    XSSFCellStyle estiloTitulo = new GeneradorEstilo.Builder()
//                            .setFuente(fuenteTitulo)
//                            .setColorDefecto(IndexedColors.BLUE.getIndex())
//                            .setTipoPatron(FillPatternType.SOLID_FOREGROUND)
//                            .setAlineacionHorizontal(HorizontalAlignment.CENTER)
//                            .setAlineacionVertical(VerticalAlignment.CENTER)
//                            .setBordeArriba(BorderStyle.THIN)
//                            .setBordeAbajo(BorderStyle.THIN)
//                            .setBordeDerecho(BorderStyle.THIN)
//                            .setBordeIzquierdo(BorderStyle.THIN)
//                            .build(wb);
//                    // Aplicar estilo a los títulos
//                    for (int i = 0; i < TablaPrepago.getColumnCount(); i++) {
//                        Cell cell = rowCol.createCell(i);
//                        cell.setCellValue(TablaPrepago.getColumnName(i));
//                        cell.setCellStyle(estiloTitulo);
//                    }   // Estilo para los datos
//                    XSSFFont fuenteDatos = new GeneradorFuente.Builder()
//                            .setNombreFuente("Calibri")
//                            .setTamanioFuente((short) 12)
//                            .build(wb);
//                    XSSFCellStyle estiloDatos = new GeneradorEstilo.Builder()
//                            .setFuente(fuenteDatos)
//                            .setBordeArriba(BorderStyle.THIN)
//                            .setBordeAbajo(BorderStyle.THIN)
//                            .setBordeDerecho(BorderStyle.THIN)
//                            .setBordeIzquierdo(BorderStyle.THIN)
//                            .setAlineacionHorizontal(HorizontalAlignment.LEFT)
//                            .setAlineacionVertical(VerticalAlignment.CENTER)
//                            .build(wb);
//                    // Llenar los datos
//                    for (int j = 0; j < TablaPrepago.getRowCount(); j++) {
//                        Row row = sheet.createRow(j + 1);
//                        for (int k = 0; k < TablaPrepago.getColumnCount(); k++) {
//                            Cell cell = row.createCell(k);
//                            if (TablaPrepago.getValueAt(j, k) != null) {
//                                cell.setCellValue(TablaPrepago.getValueAt(j, k).toString());
//                            }
//                            cell.setCellStyle(estiloDatos);
//                        }
//                    }   // Ajustar automáticamente el ancho de las columnas
//                    for (int i = 0; i < TablaPrepago.getColumnCount(); i++) {
//                        sheet.autoSizeColumn(i);
//                    }   // Guardar el archivo
//                    out = new FileOutputStream(new File(saveFile.toString()));
//                    wb.write(out);
//                }
//                out.close();
//                openFile(saveFile.toString());
//            } else {
//                JOptionPane.showMessageDialog(null, "Error al generar el archivo Excel");
//            }
//        } catch (HeadlessException | IOException e) {
//            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
//        }
//    }

}
