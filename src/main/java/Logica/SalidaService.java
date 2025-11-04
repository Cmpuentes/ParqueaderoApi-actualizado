
package Logica;

import Datos.TotalesSalidaDTO;
import DatosP.Dsalidap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.EscPosConst.Justification;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.Style.FontSize;
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
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JOptionPane;

/**
 *
 * @author Carlos Mario
 */
public class SalidaService {

    //FUNCIÓN PARA OBTENER LAS LISTA DE SALIDAS SEGÚN EL TURNO QUE ESTE ACTIVO
    public List<Dsalidap> obtenerListaSalidas() {
        try {

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/salidas/escritorio/activas");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    json.append(line);
                }
                in.close();

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Dsalidap>>() {
                }.getType();
                return gson.fromJson(json.toString(), listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    //FUNCIÓN PARA EDITAR LOS VALORES DE SALIDA DE VEHÍCULOS
    public boolean editarSalida(int idSalida, String efectivoslda, String tarjetaslda, String transferenciaslda, String totalslda) {
        try {

            int efectivo = efectivoslda.isEmpty() ? 0 : Integer.parseInt(efectivoslda);
            int tarjeta = tarjetaslda.isEmpty() ? 0 : Integer.parseInt(tarjetaslda);
            int transferencia = transferenciaslda.isEmpty() ? 0 : Integer.parseInt(transferenciaslda);
            int total = totalslda.isEmpty() ? 0 : Integer.parseInt(totalslda);

            // Construimos el JSON manualmente o usando una librería si prefieres
            String jsonInputString = String.format(
                    "{\"efectivo\": %s, \"tarjeta\": %s, \"transferencia\": %s, \"total\": %s}",
                    efectivo, tarjeta, transferencia, total
            );

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/salidas/escritorio" + idSalida);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Escribimos el body
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = con.getResponseCode();
            System.out.println("idsalida: " + idSalida);
            System.out.println("Código de respuesta: " + code);

            return (code == 200 || code == 204); // 200 OK o 204 No Content (si no devuelves body)

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //FUNCIÓN PARA CALCULAR LOS VALORES TOTALES DE LA SALIDA SEGÚN EL TURNO
    public static TotalesSalidaDTO obtenerTotalesSalida() throws Exception {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/api/salidas/escritorio/totales-calculados");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error HTTP : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        ObjectMapper mapper = new ObjectMapper();
        TotalesSalidaDTO totales = mapper.readValue(br, TotalesSalidaDTO.class);

        conn.disconnect();
        return totales;
    }

    public static Integer generarComprobante() {
        try {
            // Usamos la URL base desde la configuración centralizada
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String ruta = "/api/salidas/escritorio/generar-comprobante";

            String urlString = baseUrl + ruta;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String linea = reader.readLine();
                    return Integer.parseInt(linea); // Devuelve el número generado
                }
            } else {
                System.err.println("❌ Error al generar comprobante: " + conn.getResponseCode());
            }

        } catch (Exception e) {
            System.err.println("⚠️ Excepción al generar comprobante: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    //FUNCIÓN PARA REGISTRAR LA SALIDA DE LOS VEHICULOS
    public boolean registrarSalida(Dsalidap salida) {
        try {
            //URL url = new URL(BASE_URL + "/registrar");

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/salidas/escritorio/registrar");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Convertir salida a JSON
            Gson gson = new Gson();
            String json = gson.toJson(salida);

            // Enviar JSON
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                return true;
            } else {
                InputStream errorStream = con.getErrorStream();
                if (errorStream != null) {
                    String errorMsg = new BufferedReader(new InputStreamReader(errorStream))
                            .lines().collect(Collectors.joining("\n"));
                    JOptionPane.showMessageDialog(null, "Error al registrar salida: " + errorMsg);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar salida. Código: " + responseCode);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de conexión al registrar salida: " + e.getMessage());
        }

        return false;
    }

    public boolean ImprimirFacturaPago() throws IOException, PrinterException {
        try {
            // 1️⃣ Llamar a la API
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String urlString = baseUrl + "/api/salidas/escritorio/ultima-salida";

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                // 2️⃣ Leer el JSON y mapearlo a Dsalidap
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

                    StringBuilder jsonBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line);
                    }

                    Gson gson = new Gson();
                    Dsalidap dts = gson.fromJson(jsonBuilder.toString(), Dsalidap.class);

                    // 3️⃣ La lógica de impresión queda igual
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String fechaImpresion = now.format(formatter);
                    String fecha = now.format(formatter1);

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

                        // estilos
                        Style styleNormalcenter = new Style()
                                .setFontSize(FontSize._1, FontSize._1)
                                .setJustification(Justification.Center);
                        Style styleNormal = new Style()
                                .setFontSize(FontSize._1, FontSize._1)
                                .setJustification(Justification.Left_Default);
                        Style styleNegrita = new Style()
                                .setBold(true)
                                .setFontSize(FontSize._1, FontSize._1)
                                .setJustification(Justification.Left_Default);

                        // impresión
                        escpos.write(imageWrapper, escposImage);
                        escpos.writeLF(styleNormalcenter, "COMBUGAS S.A.S");
                        escpos.writeLF(styleNormalcenter, "NIT: 900139412-4");
                        escpos.writeLF(styleNormalcenter, "DG 31D N.32A-25B.TERNERA");
                        escpos.writeLF(styleNormalcenter, "TEL:3205417916");
                        escpos.writeLF(styleNormalcenter, " CARTAGENA");
                        escpos.writeLF(styleNormalcenter, "-----------------------------------------");
                        escpos.feed(1);
                        escpos.writeLF(styleNormal, " Factura FV:" + dts.getNumfactura());
                        escpos.writeLF(styleNormal, " Fecha:" + fecha);
                        escpos.feed(1);
                        escpos.writeLF(styleNegrita, " PLACA:" + dts.getPlaca());
                        escpos.writeLF(styleNormal, " Fecha Ingreso: " + dts.getFechaentrada());
                        escpos.writeLF(styleNormal, " Fecha Salida: " + dts.getFechasalida());
                        escpos.writeLF(styleNormal, " Modalidad: " + dts.getTiposervicio());
                        escpos.writeLF(styleNormal, " Tipo vehiculo: " + dts.getTipovehiculo());
                        escpos.writeLF(styleNormal, " Duración: " + dts.getDias() + " Días, "
                                + dts.getHoras() + " Horas, " + dts.getMinutos() + " Min");
                        escpos.feed(1);
                        escpos.writeLF(styleNormal, " Valor: $" + dts.getValor());
                        escpos.writeLF(styleNormal, " Descuento: $" + dts.getDescuento());
                        escpos.writeLF(styleNegrita, " Total a pagar: $" + dts.getTotal());
                        if (dts.getEfectivo() > 0) {
                            escpos.writeLF(styleNormal, " Forma de Pago: Efectivo");
                        }
                        if (dts.getTarjeta() > 0) {
                            escpos.writeLF(styleNormal, " Forma de Pago: Tarjeta");
                        }
                        if (dts.getTransferencia() > 0) {
                            escpos.writeLF(styleNormal, " Forma de Pago: Transferencia");
                        }

                        escpos.feed(1);
                        escpos.writeLF(styleNormal, "----------------------------------------");
                        escpos.writeLF(styleNormal, "Responsable: " + dts.getEmpleadosalida());
                        escpos.writeLF(styleNormal, "Equipo: Parqueadero - COMBUGAS");
                        escpos.writeLF(styleNormal, "Fecha impresión:" + fechaImpresion);
                        escpos.writeLF(styleNormal, "Fabricante Software: GESNNOVA");
                        escpos.writeLF(styleNormal, "NIT:901102506-1");
                        escpos.writeLF(styleNormal, "https://www.grupogesnnova.com");
                        escpos.feed(5);
                        escpos.cut(EscPos.CutMode.FULL);
                    }
                    return true;
                }
            } else {
                System.out.println("Error al consultar la última salida: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            System.out.println("Error al imprimir factura: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
