package Impresionp;

import DatosP.Dsalidaturnop;
import LogicaP.Cconexionp;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.CoffeeImageImpl;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.ImageWrapperInterface;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;
import com.github.anastaciocintra.output.PrinterOutputStream;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/**
 *
 * @author alexn
 */
public class cierreTurnoP {

    private final Cconexionp mysql = new Cconexionp();
    private final Connection cn = mysql.establecerConexionp();
    private String sSQL = "";

    public boolean ImprimirCierreT() throws FileNotFoundException, IOException, SQLException, PrintException {
        sSQL = "SELECT * FROM salida_turno ORDER BY idfinturno DESC LIMIT 1";

        try (Statement statement = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); ResultSet resultSet = statement.executeQuery(sSQL)) {

            while (resultSet.next()) {
                Dsalidaturnop dts = new Dsalidaturnop();

                dts.setTurno(resultSet.getString("turno"));
                dts.setNumero_turno(resultSet.getString("numero_turno"));
                dts.setEmpleado(resultSet.getString("empleado"));
                dts.setFechaingreso(resultSet.getString("fechaingreso"));
                dts.setFechasalida(resultSet.getString("fechasalida"));
                dts.setTotal_vehiculos(resultSet.getString("total_vehiculos"));
                dts.setEfectivo(resultSet.getInt("efectivo"));
                dts.setTarjeta(resultSet.getInt("tarjeta"));
                dts.setTransferencia(resultSet.getInt("transferencia"));
                dts.setOtros_ingresos(resultSet.getInt("otros_ingresos"));
                dts.setEfectivo_liquido(resultSet.getInt("efectivo_liquido"));
                dts.setTotal_recaudado(resultSet.getInt("total_recaudado"));
                dts.setObservaciones(resultSet.getString("observaciones"));
                dts.setTotal_abonos(resultSet.getInt("total_abonos"));

                // Obtener la fecha y hora actual del sistema
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
                    Style styleNegrita = new Style()
                            .setBold(true)
                            .setFontSize(Style.FontSize._1, Style.FontSize._1) // Mantiene el tamaño normal
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
                    escpos.writeLF(styleNormal, " Numero Turno:" + dts.getNumero_turno());
                    escpos.writeLF(styleNegrita, " Empleado:" + dts.getEmpleado());
                    escpos.writeLF(styleNormal, " Fecha Ingreso: " + dts.getFechaingreso());
                    escpos.writeLF(styleNormal, " Fecha Salida: " + dts.getFechasalida());
                    escpos.writeLF(styleNormal, " Total Vehiculos: " + dts.getTotal_vehiculos());
                    escpos.feed(1);
                    escpos.writeLF(styleNormal, " Efectivo: $" + dts.getEfectivo());
                    escpos.writeLF(styleNormal, " Tarjeta: $" + dts.getTarjeta());
                    escpos.writeLF(styleNormal, " Transferencia: $" + dts.getTransferencia());
                    escpos.writeLF(styleNormal, " Otros Ingresos:" + dts.getOtros_ingresos());
                    escpos.writeLF(styleNormal, " Total Abonos:" + dts.getTotal_abonos());
                    escpos.writeLF(styleNormal, " Efectivo Liquido:" + dts.getEfectivo_liquido());
                    escpos.writeLF(styleNegrita, "Total Recaudado:" + dts.getTotal_recaudado());
                    escpos.write(styleNormal, "Observaciones" + dts.getObservaciones());
                    escpos.feed(1);
                    escpos.writeLF(styleNormal, "----------------------------------------");
                    escpos.writeLF(styleNormal, "Impresión realizada por Grupo GESNNOVA");
                    escpos.writeLF(styleNormal, "Equipo: Parqueadero - COMBUGAS");
                    escpos.writeLF(styleNormal, "Fecha impresión:" + fechaImpresion);
                    escpos.feed(5); // Salto de líneas para finalizar la impresión
                    escpos.cut(EscPos.CutMode.FULL); // Realiza el corte del papel
                }
                return true; // Se imprimió correctamente
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return false; // No se pudo imprimir
    }
}
