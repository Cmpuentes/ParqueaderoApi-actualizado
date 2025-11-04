/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Datos.ConteoActivoDTO;
import Datos.SalidaConsultaDTO;
import DatosP.Dingresop;
import DatosP.IngresoEscritorioDTO;
import EstilosExcelPDF.GeneradorEstilo;
import EstilosExcelPDF.GeneradorFuente;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Carlos Mario
 */
public class IngresoService {

    public Integer totalregistros;
    private static final String BASE_URL = ConfiguradorApi.getInstance().getApiBaseUrl();// tu clase config

    //FUNCIÓN PARA MOSTRAR LOS DATOS DE LOS VEHÍCULOS ACTIVOS EN UNA TABLA
    public DefaultTableModel mostrar(String buscar, JTable tabla) {
        DefaultTableModel modelo = null;
        try {
            String endpoint = BASE_URL + "/api/ingresos/escritorio/activos?buscar=" + buscar;
            URL url = new URL(endpoint);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Convertir JSON → List<IngresoEscritorioDTO>
            Gson gson = new Gson();
            Type listType = new TypeToken<List<IngresoEscritorioDTO>>() {
            }.getType();
            List<IngresoEscritorioDTO> lista = gson.fromJson(response.toString(), listType);

            String[] titulos = {
                "ID", "Turno", "Número Turno", "Empleado", "Placa",
                "Fecha Ingreso", "Vehículo", "Servicio", "Cliente",
                "Zona", "Observaciones", "Estado"
            };

            totalregistros = 0;
            modelo = new DefaultTableModel(null, titulos);

            for (IngresoEscritorioDTO dto : lista) {
                Object[] fila = new Object[]{
                    dto.getIdingreso(),
                    dto.getTurno(),
                    dto.getNumeroturno(),
                    dto.getEmpleado(),
                    dto.getPlaca(),
                    dto.getFechaingreso(),
                    dto.getTipovehiculo(),
                    dto.getTiposervicio(),
                    dto.getCliente(),
                    dto.getZona(),
                    dto.getObservaciones(),
                    dto.getEstado()
                };
                totalregistros++;
                modelo.addRow(fila);
            }

            // Asignar modelo al JTable
            tabla.setModel(modelo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelo; // 🔥 IMPORTANTE: retornar el modelo como en la función vieja
    }

    //FUNCIÓN QUE CONSULTA SI HAY UN INGRESO ACTIVO ANTES DE INGRESAR UNA PLACA
    public boolean verificarPlacaActiva(String placa) {
        try {
            URL url = new URL(BASE_URL + "/api/ingresos/escritorio/ingresos/placa-activa?placa=" + URLEncoder.encode(placa, "UTF-8"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int code = con.getResponseCode();
            InputStream is = (code >= 200 && code < 300) ? con.getInputStream() : con.getErrorStream();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String resp = br.readLine();
                // El backend responde true o false directamente
                return Boolean.parseBoolean(resp);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al verificar la placa: " + e.getMessage());
            return false;
        }
    }

    //FUNCIÓN PARA INGRESAR CEHÍCULOS AL APRQUEADERO
    public String guardarIngreso(Dingresop ingreso) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(ingreso);

            //URL url = new URL(API_URL);
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/ingresos/escritorio");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString(); // Devuelve la respuesta del servidor
                }
            } else {
                return "Error al guardar ingreso: " + responseCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error de conexión o servidor: " + e.getMessage();
        }
    }

    //FUNCIÓN PARA EDITAR LOS VEHICULOS INGRESADOS
    public boolean actualizarIngresoEnAPI(Dingresop ingreso) {
        try {

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/ingresos/escritorio/" + ingreso.getIdingreso());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            Gson gson = new Gson();
            String json = gson.toJson(ingreso);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                System.err.println("Error al actualizar ingreso. Código: " + responseCode);
                return false;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la API: " + e.getMessage());
            return false;
        }
    }

    //FUNCIÓN PARA ELIMINAR UN INGRESO
    public boolean eliminarIngreso(Dingresop dts) {
        try {

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/ingresos/escritorio/eliminar/" + dts.getIdingreso());

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Accept", "application/json");

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                return true;
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                JOptionPane.showMessageDialog(null, "Ingreso no encontrado con ID: " + dts.getIdingreso());
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar ingreso. Código: " + responseCode);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de conexión al eliminar ingreso: " + e.getMessage());
        }

        return false;
    }

    //FUNCIÓN PARA OBTENER SOLO LAS PLACAS QUE ESTÁN ACTIVAS
    public static List<String> obtenerPlacasActivas() {
        List<String> placas = new ArrayList<>();
        try {

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/ingresos/escritorio/placas-activas");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Convertir JSON a lista de Strings con Gson
                Gson gson = new Gson();
                Type listType = new TypeToken<List<String>>() {
                }.getType();
                placas = gson.fromJson(response.toString(), listType);
            } else {
                System.err.println("Error al obtener placas activas: " + con.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return placas;
    }

    public static SalidaConsultaDTO consultarIngresoPorPlaca(String placa) {
        SalidaConsultaDTO dto = null;
        try {

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/ingresos/escritorio/detalle/" + placa);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Gson gson = new Gson();
                dto = gson.fromJson(response.toString(), SalidaConsultaDTO.class);

            } else {
                JOptionPane.showMessageDialog(null, "Placa no encontrada o error del servidor.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al consultar placa: " + e.getMessage());
        }
        return dto;
    }

    //fUNCIÓN OPENFILE QUE COMPLEMENTA LA FUNCIÓN DE EXPORTAR EXCEL
    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }

    //FUNCIÓN PARA EXPORTAR EN EXCEL LOS DATOS DE INGRESO
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

    //Metodo para contar vehiculos activos en parquedaro llamado en Jfinturnop
    public int contarEstadoActivo() throws Exception {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/api/ingresos/escritorio/contar-activos");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error HTTP : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        ObjectMapper mapper = new ObjectMapper();
        ConteoActivoDTO conteo = mapper.readValue(br, ConteoActivoDTO.class);

        conn.disconnect();

        return (int) conteo.getTotalActivos(); // devolver el total directamente
    }
}
