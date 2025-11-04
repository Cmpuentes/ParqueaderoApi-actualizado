package ImpresionP;

import DatosP.Dsalidap;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;

public class ImprimirSalidap {

//    private String sSQL = "";
//
//    public boolean ImprimirFacturaPago() throws SQLException, IOException, PrinterException {
//        sSQL = "SELECT * FROM salida ORDER BY idsalida DESC LIMIT 1";
//
//        try (Statement statement = cn.createStatement(); ResultSet resultSet = statement.executeQuery(sSQL)) {
//            while (resultSet.next()) {
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
//
//                // Obtener la fecha y hora actual del sistema
//                LocalDateTime now = LocalDateTime.now();
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
//                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//                String fechaImpresion = now.format(formatter);
//                String fecha = now.format(formatter1);
//
//                
//                // 2️⃣ IMPRIMIR FACTURA
//                PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
//                if (printService == null) {
//                    System.out.println("No hay impresora disponible.");
//                    return false;
//                }
//
//                try (EscPos escpos = new EscPos(new PrinterOutputStream(printService))) {
//
//                    // Ruta del logo (ajústala según la ubicación de tu imagen)
//                    InputStream is = getClass().getResourceAsStream("/Imagenes/Iconocombugas.png");
//                    if (is == null) {
//                        System.out.println("Error: No se pudo cargar la imagen desde los recursos.");
//                    }
//                    BufferedImage image = ImageIO.read(is);
//
//                    EscPosImage escposImage = new EscPosImage(new CoffeeImageImpl(image), new BitonalThreshold());
//                    ImageWrapperInterface imageWrapper = new RasterBitImageWrapper();
//                    imageWrapper.setJustification(EscPosConst.Justification.Center);
//                    Style styleNormalcenter = new Style()
//                            .setFontSize(FontSize._1, FontSize._1)
//                            .setJustification(Justification.Center);
//                    Style styleNormal = new Style()
//                            .setFontSize(FontSize._1, FontSize._1)
//                            .setJustification(Justification.Left_Default);
//                    Style styleNegritaMediana = new Style()
//                            .setBold(true)
//                            .setFontSize(FontSize._1, FontSize._1)
//                            .setJustification(Justification.Left_Default);
//                    Style styleNegrita = new Style()
//                            .setBold(true)
//                            .setFontSize(FontSize._1, FontSize._1) // Mantiene el tamaño normal
//                            .setJustification(Justification.Left_Default);
//
//                    escpos.write(imageWrapper, escposImage);
//
//                    escpos.writeLF(styleNormalcenter, "COMBUGAS S.A.S");
//                    escpos.writeLF(styleNormalcenter, "NIT: 900139412-4");
//                    escpos.writeLF(styleNormalcenter, "DG 31D N.32A-25B.TERNERA");
//                    escpos.writeLF(styleNormalcenter, "TEL:3205417916");
//                    escpos.writeLF(styleNormalcenter, " CARTAGENA");
//                    escpos.writeLF(styleNormalcenter, "-----------------------------------------");
//                    escpos.feed(1); // Línea en blanco
//                    escpos.writeLF(styleNormal, " Factura FV:" + dts.getNumfactura());
//                    escpos.writeLF(styleNormal, " Fecha:" + fecha);
//                    escpos.feed(1);
//                    escpos.writeLF(styleNegritaMediana, " PLACA:" + dts.getPlaca());
//                    escpos.writeLF(styleNormal, " Fecha Ingreso: " + dts.getFechaentrada());
//                    escpos.writeLF(styleNormal, " Fecha Salida: " + dts.getFechasalida());
//                    escpos.writeLF(styleNormal, " Modalidad: " + dts.getTiposervicio());
//                    escpos.writeLF(styleNormal, " Tipo vehiculo: " + dts.getTipovehiculo());
//                    escpos.writeLF(styleNormal, " Duración: " + dts.getDias() + " Días, "
//                            + dts.getHoras() + " Horas, " + dts.getMinutos() + " Min");
//                    escpos.feed(1);
//                    escpos.writeLF(styleNormal, " Valor: $" + dts.getValor());
//                    escpos.writeLF(styleNormal, " Rrecibo: $" + dts.getNumero_recibo());
//                    escpos.writeLF(styleNormal, " Descuento: $" + dts.getDescuento());
//                    escpos.writeLF(styleNegrita, " Total a pagar: $" + dts.getTotal());
//
//                    if (dts.getEfectivo() > 0) {
//                        escpos.writeLF(styleNormal, " Forma de Pago: Efectivo");
//                    }
//                    if (dts.getTarjeta() > 0) {
//                        escpos.writeLF(styleNormal, " Forma de Pago: Tarjeta");
//                    }
//                    if (dts.getTransferencia() > 0) {
//                        escpos.writeLF(styleNormal, " Forma de Pago: Transferencia");
//                    }
//                    escpos.feed(1);
//                    escpos.writeLF(styleNormal, "----------------------------------------");
//                    escpos.writeLF(styleNormal, "Responsable: " + dts.getEmpleadosalida());
//                    escpos.writeLF(styleNormal, "Equipo: Parqueadero - COMBUGAS");
//                    escpos.writeLF(styleNormal, "Fecha impresión:" + fechaImpresion);
//                    escpos.writeLF(styleNormal, "Fabricante Software: GESNNOVA ");
//                    escpos.writeLF(styleNormal, "NIT:901102506-1 ");
//                    escpos.writeLF(styleNormal, "https://www.grupogesnnova.com");
//                    escpos.feed(5); // Salto de líneas para finalizar la impresión
//
//                    escpos.cut(EscPos.CutMode.FULL); // Realiza el corte del papel
//                }
//                return true; // Se imprimió correctamente
//            }
//        } catch (SQLException e) {
//            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
//        }
//        return false; // No se pudo imprimir
//    }
}
