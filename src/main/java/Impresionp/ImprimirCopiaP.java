package Impresionp;

import DatosP.Dsalidap;
import Logica.ConfiguradorApi;
import java.io.IOException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterException;
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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ImprimirCopiaP {

//    private final Cconexionp mysql = new Cconexionp();
//    private final Connection cn = mysql.establecerConexionp();
//    private String sSQL = "";

//    public DefaultTableModel mostrarP(String buscar) {
//       
//        DefaultTableModel modelo;
//
//        String[] titulos = {"Fecha Entrada", "Fecha Salida", "Placa", "Tipo Servicio", "Dias", "Horas", "Minutos",
//            "N° Factura", "Valor", "Recibo", "Descuento", "Subtotal", "Efectivo", "Tarjeta", "Transferencia", "Total",
//            "Responsable"};
//
//        String[] registro = new String[17];
//
//        modelo = new DefaultTableModel(null, titulos);
//
//        sSQL = "SELECT * FROM salida WHERE placa LIKE ? ORDER BY idsalida desc";
//
//        try {
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            pst.setString(1, "%" + buscar + "%");
//            ResultSet rs = pst.executeQuery();
//
//            while (rs.next()) {
//                registro[0] = rs.getString("fechaentrada");
//                registro[1] = rs.getString("fechasalida");
//                registro[2] = rs.getString("placa");
//                registro[3] = rs.getString("tiposervicio");
//                registro[4] = rs.getString("dias");
//                registro[5] = rs.getString("horas");
//                registro[6] = rs.getString("minutos");
//                registro[7] = rs.getString("numfactura");
//                registro[8] = rs.getString("valor");
//                registro[9] = rs.getString("numero_recibo");
//                registro[10] = rs.getString("descuento");
//                registro[11] = rs.getString("total");
//                registro[12] = rs.getString("subtotal");
//                registro[13] = rs.getString("efectivo");
//                registro[14] = rs.getString("tarjeta");
//                registro[15] = rs.getString("transferencia");
//                registro[16] = rs.getString("empleadosalida");
//
//                modelo.addRow(registro);
//            }
//            return modelo;
//
//        } catch (SQLException e) {
//           JOptionPane.showMessageDialog(null, "Error en la consulta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//
//            return null;
//        }
//    }
    public DefaultTableModel mostrarP(String buscar) {
        DefaultTableModel modelo;
        String[] titulos = {"Fecha Entrada", "Fecha Salida", "Placa", "Tipo Servicio", "Dias", "Horas", "Minutos",
            "N° Factura", "Valor", "Recibo", "Descuento", "Subtotal", "Efectivo", "Tarjeta", "Transferencia", "Total",
            "Responsable"};

        modelo = new DefaultTableModel(null, titulos);

        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String urlString;

            // 🟢 Si el campo buscar está vacío, llamamos sin parámetro → muestra todo
            if (buscar == null || buscar.trim().isEmpty()) {
                urlString = String.format("%s/api/salidas/escritorio/buscar-placa", baseUrl);
            } else {
                // 🟢 Si hay una placa, se envía como parámetro
                urlString = String.format("%s/api/salidas/escritorio/buscar-placa?placa=%s",
                        baseUrl, URLEncoder.encode(buscar, StandardCharsets.UTF_8));
            }

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            System.out.println("Código de respuesta HTTP: " + responseCode);

            if (responseCode == 200) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Map<String, Object>>>() {
                    }.getType();
                    List<Map<String, Object>> resultados = gson.fromJson(reader, listType);

                    for (Map<String, Object> item : resultados) {
                        String[] registro = new String[17];
                        registro[0] = String.valueOf(item.get("fechaentrada"));
                        registro[1] = String.valueOf(item.get("fechasalida"));
                        registro[2] = String.valueOf(item.get("placa"));
                        registro[3] = String.valueOf(item.get("tiposervicio"));
                        registro[4] = String.valueOf(item.get("dias"));
                        registro[5] = String.valueOf(item.get("horas"));
                        registro[6] = String.valueOf(item.get("minutos"));
                        registro[7] = String.valueOf(item.get("numfactura"));
                        registro[8] = String.valueOf(item.get("valor"));
                        registro[9] = String.valueOf(item.get("numero_recibo"));
                        registro[10] = String.valueOf(item.get("descuento"));
                        registro[11] = String.valueOf(item.get("subtotal"));
                        registro[12] = String.valueOf(item.get("efectivo"));
                        registro[13] = String.valueOf(item.get("tarjeta"));
                        registro[14] = String.valueOf(item.get("transferencia"));
                        registro[15] = String.valueOf(item.get("total"));
                        registro[16] = String.valueOf(item.get("empleadosalida"));

                        modelo.addRow(registro);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error al consultar la API: Código " + responseCode,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al conectar con la API: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return modelo;
    }

//    public void imprimirFilaSeleccionada(JTable TablaCopia) throws SQLException, IOException, PrinterException {
//        int fila = TablaCopia.getSelectedRow(); // Obtener la fila seleccionada
//
//        if (fila == -1) {
//            JOptionPane.showMessageDialog(null, "Seleccione una fila para imprimir.", "Advertencia", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // Obtener valores de la fila seleccionada
//        String numFactura = TablaCopia.getValueAt(fila, 7).toString(); // Suponiendo que la columna 7 es 'numfactura'
//
//        // Consultar la base de datos con el número de factura seleccionado
//        sSQL = "SELECT * FROM salida WHERE numfactura = ? ";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setString(1, numFactura);
//            System.out.print("numero factura:" + numFactura);
//            ResultSet resultSet = pst.executeQuery();
//
//            if (resultSet.next()) {
//                Dsalidap dts = new Dsalidap();
//                dts.setNumfactura(resultSet.getString("numfactura"));
//                dts.setFechaentrada(resultSet.getString("fechaentrada"));
//                dts.setFechasalida(resultSet.getString("fechasalida"));
//                dts.setPlaca(resultSet.getString("placa"));
//                dts.setTiposervicio(resultSet.getString("tiposervicio"));
//                dts.setTipovehiculo(resultSet.getString("tipovehiculo"));
//                dts.setDias(resultSet.getInt("dias"));
//                dts.setHoras(resultSet.getInt("horas"));
//                dts.setMinutos(resultSet.getInt("minutos"));
//                dts.setValor(resultSet.getInt("valor"));
//                dts.setTotal(resultSet.getInt("total"));
//                dts.setEmpleadosalida(resultSet.getString("empleadosalida"));
//                dts.setDescuento(resultSet.getInt("descuento"));
//                dts.setEfectivo(resultSet.getInt("efectivo"));
//                dts.setTarjeta(resultSet.getInt("tarjeta"));
//                dts.setTransferencia(resultSet.getInt("transferencia"));
//                dts.setNumero_recibo(resultSet.getInt("numero_recibo"));
//                dts.setSubtotal(resultSet.getInt("subtotal"));
//
//                imprimirDatos(dts); // Llamar al método de impresión
//            } else {
//                JOptionPane.showMessageDialog(null, "No se encontró la factura seleccionada.");
//            }
//        }
//    }
    public void imprimirFilaSeleccionada(JTable TablaCopia) {
        int fila = TablaCopia.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null,
                    "Seleccione una fila para imprimir.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener y limpiar el número de factura
        String numFacturaStr = TablaCopia.getValueAt(fila, 7).toString().trim();
        int numFactura;

        try {
            // Maneja tanto "14894" como "14894.0"
            numFactura = (int) Double.parseDouble(numFacturaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El número de factura no es válido: " + numFacturaStr,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String urlString = baseUrl + "/api/salidas/escritorio/factura?numfactura=" + numFactura;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

                    Gson gson = new Gson();
                    Dsalidap dts = gson.fromJson(reader, Dsalidap.class);
                    imprimirDatos(dts);
                }
            } else if (responseCode == 404) {
                JOptionPane.showMessageDialog(null,
                        "No se encontró la factura seleccionada.",
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error al consultar la factura: Código " + responseCode,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al conectar con la API: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void imprimirDatos(Dsalidap dts) throws IOException, PrinterException {

        // Obtener la fecha y hora actual del sistema
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaImpresion = now.format(formatter);
        String fecha = now.format(formatter1);

        // 2️⃣ IMPRIMIR FACTURA
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
            Style styleNormalcenter = new Style()
                    .setFontSize(FontSize._1, FontSize._1)
                    .setJustification(Justification.Center);
            Style styleNormal = new Style()
                    .setFontSize(FontSize._1, FontSize._1)
                    .setJustification(Justification.Left_Default);
            Style styleNegritaMediana = new Style()
                    .setBold(true)
                    .setFontSize(FontSize._1, FontSize._1)
                    .setJustification(Justification.Left_Default);
            Style styleNegrita = new Style()
                    .setBold(true)
                    .setFontSize(FontSize._1, FontSize._1) // Mantiene el tamaño normal
                    .setJustification(Justification.Left_Default);

            escpos.write(imageWrapper, escposImage);

            escpos.writeLF(styleNormalcenter, "COMBUGAS S.A.S");
            escpos.writeLF(styleNormalcenter, "NIT: 900139412-4");
            escpos.writeLF(styleNormalcenter, "DG 31D N.32A-25B.TERNERA");
            escpos.writeLF(styleNormalcenter, "TEL:3205417916");
            escpos.writeLF(styleNormalcenter, " CARTAGENA");
            escpos.writeLF(styleNormalcenter, "-----------------------------------------");
            escpos.feed(1); // Línea en blanco
            escpos.writeLF(styleNormal, " Factura FV:" + dts.getNumfactura());
            escpos.writeLF(styleNormal, " Fecha:" + fecha);
            escpos.feed(1);
            escpos.writeLF(styleNegritaMediana, " PLACA:" + dts.getPlaca());
            escpos.writeLF(styleNormal, " Fecha Ingreso: " + dts.getFechaentrada());
            escpos.writeLF(styleNormal, " Fecha Salida: " + dts.getFechasalida());
            escpos.writeLF(styleNormal, " Modalidad: " + dts.getTiposervicio());
            escpos.writeLF(styleNormal, " Tipo vehiculo: " + dts.getTipovehiculo());
            escpos.writeLF(styleNormal, " Duración: " + dts.getDias() + " Días, "
                    + dts.getHoras() + " Horas, " + dts.getMinutos() + " Min");
            escpos.feed(1);
            escpos.writeLF(styleNormal, " Valor: $" + dts.getValor());
            escpos.writeLF(styleNormal, " Recibo: $" + dts.getNumero_recibo());
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
            escpos.writeLF(styleNormal, "Equipo: Parqueadero - combugas");
            escpos.writeLF(styleNormal, "Fecha impresión:" + fechaImpresion);
            escpos.writeLF(styleNormal, "Fabricante Software: GESNNOVA ");
            escpos.writeLF(styleNormal, "NIT:901102506-1 ");
            escpos.writeLF(styleNormal, "https://www.grupogesnnova.com");
            escpos.feed(5); // Salto de líneas para finalizar la impresión

            escpos.cut(EscPos.CutMode.FULL); // Realiza el corte del papel
        }
    }

}
