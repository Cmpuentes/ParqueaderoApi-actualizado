package Impresionp;

import DatosP.Dingresop;
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
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.print.*;

public class ImprimirArqueo {


    public Integer totalPlacas;

    public boolean ImprimirArqueo() throws IOException, PrintException {
        totalPlacas = 0;
        List<Dingresop> ingresosActivos = new ArrayList<>();

        try {
            // 1️⃣ Llamada al backend
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String urlString = baseUrl + "/api/ingresos/escritorio/arqueo";
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            switch (conn.getResponseCode()) {
                case 200 -> {
                    StringBuilder jsonResponse;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        jsonResponse = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            jsonResponse.append(line);
                        }
                    }
                    // 2️⃣ Convertir JSON a objetos
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Dingresop>>() {
                    }.getType();
                    ingresosActivos = gson.fromJson(jsonResponse.toString(), listType);
                }

                case 204 -> {
                    System.out.println("No hay vehículos activos en el parqueadero.");
                    return false;
                }
                default -> {
                    System.out.println("Error al consultar la API: " + conn.getResponseCode());
                    return false;
                }
            }

        } catch (JsonSyntaxException | IOException e) {
            System.out.println("Error al obtener datos desde la API: " + e.getMessage());
            return false;
        }

        // 3️⃣ Si no hay registros, detener
        if (ingresosActivos.isEmpty()) {
            System.out.println("No hay vehículos activos en el parqueadero.");
            return false;
        }

        // 4️⃣ Preparar la impresión (igual que antes)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        String fechaImpresion = now.format(formatter);

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

            Style styleNegrita = new Style().setBold(true).setFontSize(Style.FontSize._1, Style.FontSize._1);
            Style styleNormal = new Style().setFontSize(Style.FontSize._1, Style.FontSize._1);
            Style styleNormalCenter = new Style().setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Center);

            escpos.write(imageWrapper, escposImage);
            escpos.writeLF(styleNormalCenter, "COMBUGAS S.A.S");
            escpos.writeLF(styleNormalCenter, "NIT: 900139412-4");
            escpos.writeLF(styleNormalCenter, "DG 31D N.32A-25B.TERNERA");
            escpos.writeLF(styleNormalCenter, "TEL:3205417916");
            escpos.writeLF(styleNormalCenter, " CARTAGENA");
            escpos.writeLF(styleNormalCenter, "-----------------------------------------");
            escpos.feed(1);
            escpos.writeLF(styleNegrita, " Vehículos activos en el parqueadero: " + ingresosActivos.size());
            escpos.feed(1);
            escpos.writeLF(styleNegrita, " PLACA  TIPO V  TIPO S  CLIENTE");
            escpos.writeLF(styleNormal, "------------------------------------------");

            // 5️⃣ Recorrer la lista obtenida del backend
            for (Dingresop ingreso : ingresosActivos) {
                escpos.writeLF(styleNormal, String.format("%-8s %-8s %-8s %-8s",
                        ingreso.getPlaca().toUpperCase(),
                        ingreso.getTipovehiculo(),
                        ingreso.getTiposervicio(),
                        ingreso.getCliente()));
                totalPlacas++;
            }

            escpos.writeLF(styleNormal, "-------------------------------------------");
            escpos.writeLF(styleNormal, " Impresión realizada por Grupo GESNNOVA");
            escpos.writeLF(styleNormal, " Equipo: Parqueadero - COMBUGAS");
            escpos.writeLF(styleNormal, " Fecha impresión: " + fechaImpresion);
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
        }

        return true;
    }

}
