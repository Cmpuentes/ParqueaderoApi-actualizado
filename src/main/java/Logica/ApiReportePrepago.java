/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Datos.SalidaDTO;
import EstilosExcelPDF.GeneradorEstilo;
import EstilosExcelPDF.GeneradorFuente;
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
import java.util.List;
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

/**
 *
 * @author Carlos Mario
 */
public class ApiReportePrepago {

    //FUNCIÓN PARA EXPORTAR LOS REPORTES PREPAGO DE FUNCIONES DE ADMINISTRADORES
    public DefaultTableModel exportarReportePrepagos(JTable TablaPrepago) {
        DefaultTableModel model = (DefaultTableModel) TablaPrepago.getModel();
        model.setRowCount(0);

        String[] titulos = {"PLACA", "CLIENTE", "TIPO VEHI", "TIPO SERVI", "FECHA ENTRA", "FECHA SALIDA",
            "VALOR", "DIAS", "TURNO", "EMPLEADO"};
        model.setColumnIdentifiers(titulos);

        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/salidas/prepagos");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    Gson gson = new Gson();
                    List<SalidaDTO> lista = gson.fromJson(response.toString(), new TypeToken<List<SalidaDTO>>() {
                    }.getType());

                    for (SalidaDTO s : lista) {
                        model.addRow(new Object[]{
                            s.getPlaca(),
                            s.getCliente(),
                            s.getTipovehiculo(),
                            s.getTiposervicio(),
                            s.getFechaentrada(),
                            s.getFechasalida(),
                            s.getValor(),
                            s.getDias(),
                            s.getTurno(),
                            s.getEmpleadosalida()
                        });
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error en la respuesta del servidor Reporte prepago: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al consumir la API: " + e.getMessage());
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        TablaPrepago.setRowSorter(sorter);

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
    
    public void exportarExcel(JTable TablaPrepago) {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(TablaPrepago);
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
                    for (int i = 0; i < TablaPrepago.getColumnCount(); i++) {
                        Cell cell = rowCol.createCell(i);
                        cell.setCellValue(TablaPrepago.getColumnName(i));
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
                    for (int j = 0; j < TablaPrepago.getRowCount(); j++) {
                        Row row = sheet.createRow(j + 1);
                        for (int k = 0; k < TablaPrepago.getColumnCount(); k++) {
                            Cell cell = row.createCell(k);
                            if (TablaPrepago.getValueAt(j, k) != null) {
                                cell.setCellValue(TablaPrepago.getValueAt(j, k).toString());
                            }
                            cell.setCellStyle(estiloDatos);
                        }
                    }   // Ajustar automáticamente el ancho de las columnas
                    for (int i = 0; i < TablaPrepago.getColumnCount(); i++) {
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

}
