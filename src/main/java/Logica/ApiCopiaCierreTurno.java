/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Datos.SalidaTurnoDTO;
import Datos.SalidaTurnoPrintDTO;
import DatosP.Dsalidaturnop;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.CoffeeImageImpl;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.ImageWrapperInterface;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos Mario
 */
public class ApiCopiaCierreTurno {

    //FUNCIÓN PARA MOSTRAR LOS CIERRES DE TURNO EN LOS ADMINISTRATIVOS
    public DefaultTableModel mostrar(String buscar) {
        String[] titulos = {
            "EMPLEADO", "TURNO", "N° TURNO", "FECHA INICIO", "FECHA SALIDA",
            "EFECTIVO", "TARJETAS", "TRANSFERENCIA", "TOTAL ABONOS",
            "OTROS INGRESOS", "TOTAL RECAUDADO", "ENTREGA ADMON", "OBSERVACIONES"
        };
        DefaultTableModel modelo = new DefaultTableModel(null, titulos);

        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String endpoint = baseUrl + "/salida_turno";

            if (buscar != null && !buscar.isEmpty()) {
                endpoint += "?turno=" + buscar;
            }

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    Gson gson = new Gson();
                    List<SalidaTurnoDTO> lista = gson.fromJson(
                            response.toString(),
                            new TypeToken<List<SalidaTurnoDTO>>() {
                            }.getType()
                    );

                    for (SalidaTurnoDTO s : lista) {
                        modelo.addRow(new Object[]{
                            s.getEmpleado(),
                            s.getTurno(),
                            s.getNumeroturno(),
                            s.getFechaingreso(),
                            s.getFechasalida(),
                            s.getEfectivo(),
                            s.getTarjeta(),
                            s.getTransferencia(),
                            s.getTotalabonos(),
                            s.getOtrosingresos(),
                            s.getTotalrecaudado(),
                            s.getEfectivoliquido(), // entrega admin
                            s.getObservaciones()
                        });
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error en la respuesta del servidor copia cierre de turno: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al consumir la API: " + e.getMessage());
        }

        return modelo;
    }

    //FUNCIÓN QUE HACE EL LLAMADO AL BACK PARA TRAER LOS VALORES A IMPRIMIR 
    public SalidaTurnoPrintDTO obtenerPorNumeroTurno(String numTurno) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/salida_turno/numero/" + numTurno);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    Gson gson = new Gson();
                    return gson.fromJson(response.toString(), SalidaTurnoPrintDTO.class);
                }
            } else {
                System.out.println("Error al obtener salida turno, código: " + code);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //FUNCIÓN PARA IMPRIMIR LA FILA SELECCIONADA DE LA TABLA
    public void imprimirFilaSeleccionada(JTable tablalistadoCopiaTurno) throws IOException, PrinterException {
        int fila = tablalistadoCopiaTurno.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una fila para imprimir.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String numTurno = tablalistadoCopiaTurno.getValueAt(fila, 2).toString();

        ApiCopiaCierreTurno api = new ApiCopiaCierreTurno();
        SalidaTurnoPrintDTO dts = api.obtenerPorNumeroTurno(numTurno);

        if (dts != null) {
            imprimirDatos(dts); // usas la función que ya tienes
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el turno seleccionado.");
        }
    }
    
    //FUNCIÓN PARA IMPRIMIR LOS DATOS
    public void imprimirDatos(SalidaTurnoPrintDTO turno) throws IOException, PrinterException {

        // Obtener la fecha y hora actual del sistema
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaImpresion = now.format(formatter);
        String fecha = now.format(formatter1);

        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        if (printService == null) {
            System.out.println("No hay impresora disponible.");
            return;
        }

        try (EscPos escpos = new EscPos(new PrinterOutputStream(printService))) {

            // Ruta del logo (ajústala según la ubicación de tu imagen)
            InputStream is = getClass().getResourceAsStream("/Imagenes/Iconocombugas.png");
            if (is == null) {
                System.out.println("Error: No se pudo cargar la imagen desde los recursos.");
            }
            BufferedImage image = ImageIO.read(is);

            EscPosImage escposImage = new EscPosImage(new CoffeeImageImpl(image), new BitonalThreshold());
            ImageWrapperInterface imageWrapper = new RasterBitImageWrapper();
            imageWrapper.setJustification(EscPosConst.Justification.Center);

            Style styleNormal = new Style()
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Left_Default);

            Style styleNormalCenter = new Style()
                    .setBold(true)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Center);

            escpos.write(imageWrapper, escposImage);

            escpos.writeLF(styleNormalCenter, "HOTEL COMBUGAS S.A.S ");
            escpos.writeLF(styleNormalCenter, "NIT: 900139412-4 ");
            escpos.writeLF(styleNormalCenter, "TEL:3205417916");
            escpos.writeLF(styleNormalCenter, "CARTAGENA");
            escpos.writeLF(styleNormalCenter, "DG 31D N.32A-25B.TERNERA");
            escpos.feed(1);
            escpos.writeLF(styleNormal, "Fecha Hora Inicio: " + turno.getFechaingreso());
            escpos.writeLF(styleNormal, "Fecha Hora Salida: " + turno.getFechasalida());
            escpos.feed(1);
            escpos.writeLF(styleNormal, "Total Recaudo: " + turno.getTotalrecaudado());
            escpos.feed(1);
            escpos.writeLF(styleNormal, "Detalle del recaudado");
            escpos.feed(1);
            escpos.writeLF(styleNormal, "Tarjetas: " + turno.getTarjeta());
            escpos.writeLF(styleNormal, "Efectivo: " + turno.getEfectivo());
            escpos.writeLF(styleNormal, "Transferencias: " + turno.getTransferencia());
            escpos.writeLF(styleNormal, "Total Abonos: " + turno.getTotalabonos());
            escpos.writeLF(styleNormal, "Otros Ingresos: " + turno.getOtrosingresos());
            escpos.writeLF(styleNormal, "Efectivo Liquido: " + turno.getEfectivoliquido());
            escpos.writeLF(styleNormal, "Observaciones: " + turno.getObservaciones());
            escpos.feed(1);
            escpos.writeLF(styleNormal, "Empleado: " + turno.getEmpleado());
            escpos.writeLF(styleNormal, "Turno: " + turno.getTurno());
            escpos.writeLF(styleNormal, "Numero Turno: " + turno.getNumeroturno());
            escpos.writeLF(styleNormal, "Fecha impresión:" + fechaImpresion);
            escpos.feed(1);
            escpos.writeLF(styleNormal, "Fabricante Software: GESNNOVA ");
            escpos.writeLF(styleNormal, "NIT:901102506-1 ");
            escpos.writeLF(styleNormal, "https://www.grupogesnnova.com");
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL); // Realiza el corte del papel
        }
    }

}
