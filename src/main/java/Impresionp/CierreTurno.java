package Impresionp;

import Datos.SalidaTurno;
import Logica.ConfiguradorApi;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class CierreTurno {

    private static final String BASE_URL = ConfiguradorApi.getInstance().getApiBaseUrl();// tu clase config


    public boolean cierreTurno() throws FileNotFoundException, IOException {
        try {
            // Llamada al backend para obtener el último cierre
            URL url = new URL(BASE_URL + "/cierre/cierreturno/ultimo");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                System.out.println("Error al consultar el cierre de turno. Código HTTP: " + conn.getResponseCode());
                return false;
            }

            // Convertir respuesta JSON a objeto Java con Gson
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            SalidaTurno dts = new Gson().fromJson(br, SalidaTurno.class);
            br.close();
            conn.disconnect();

            // Obtener fecha y hora actual del sistema
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String fechaImpresion = now.format(formatter);
            String fecha = now.format(formatter1);

            // Impresora predeterminada
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            if (printService == null) {
                System.out.println("No hay impresora disponible.");
                return false;
            }

            try (EscPos escpos = new EscPos(new PrinterOutputStream(printService))) {

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
                Style styleNegrita = new Style()
                        .setBold(true)
                        .setFontSize(Style.FontSize._1, Style.FontSize._1)
                        .setJustification(EscPosConst.Justification.Left_Default);

                escpos.write(imageWrapper, escposImage);

                escpos.writeLF(styleNormalCenter, "COMBUGAS S.A.S");
                    escpos.writeLF(styleNormalCenter, "NIT: 900139412-4");
                    escpos.writeLF(styleNormalCenter, "DG 31D N.32A-25B.TERNERA");
                    escpos.writeLF(styleNormalCenter, "TEL:3205417916");
                    escpos.writeLF(styleNormalCenter, " CARTAGENA");
                    escpos.writeLF(styleNormalCenter, "-----------------------------------------");
                    escpos.feed(1); // Línea en blanco
                    escpos.writeLF(styleNormal, " Turno:" + dts.getTurno());
                    escpos.writeLF(styleNormal, " Numero Turno:" + dts.getNumeroturno());
                    escpos.writeLF(styleNegrita, " Empleado:" + dts.getEmpleado());
                    escpos.writeLF(styleNormal, " Fecha Ingreso: " + dts.getFechaingreso());
                    escpos.writeLF(styleNormal, " Fecha Salida: " + dts.getFechasalida());
                    escpos.writeLF(styleNormal, " Total Vehiculos: " + dts.getTotalvehiculos());
                    escpos.feed(1);
                    escpos.writeLF(styleNormal, " Efectivo: $" + dts.getEfectivo());
                    escpos.writeLF(styleNormal, " Tarjeta: $" + dts.getTarjeta());
                    escpos.writeLF(styleNormal, " Transferencia: $" + dts.getTransferencia());
                    escpos.writeLF(styleNormal, " Otros Ingresos:" + dts.getOtrosingresos());
                    escpos.writeLF(styleNormal, " Total Abonos:" + dts.getTotalabonos());
                    escpos.writeLF(styleNormal, " Efectivo Liquido:" + dts.getEfectivoliquido());
                    escpos.writeLF(styleNegrita, "  Total Recaudado:" + dts.getTotalrecaudado());
                    escpos.write(styleNormal, "Observaciones" + dts.getObservaciones());
                    escpos.feed(1);
                    escpos.writeLF(styleNormal, "----------------------------------------");
                    escpos.writeLF(styleNormal, "Impresión realizada por Grupo GESNNOVA");
                    escpos.writeLF(styleNormal, "Equipo: Parqueadero - COMBUGAS");
                    escpos.writeLF(styleNormal, "Fecha impresión:" + fechaImpresion);
                    escpos.feed(5); // Salto de líneas para finalizar la impresión
                    escpos.cut(EscPos.CutMode.FULL); // Realiza el corte del papel
                escpos.feed(5);
                escpos.cut(EscPos.CutMode.FULL);
            }

            return true;

        } catch (JsonIOException | JsonSyntaxException | IOException | IllegalArgumentException e) {
            System.out.println("Error al obtener o imprimir el cierre de turno: " + e.getMessage());
            return false;
        }
    }


}
