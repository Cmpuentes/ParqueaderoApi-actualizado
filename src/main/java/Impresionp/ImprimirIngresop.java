package ImpresionP;

import DatosP.Dingresop;
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
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class ImprimirIngresop {

    private final Cconexionp mysql = new Cconexionp();
    private final Connection cn = mysql.establecerConexionp();
    private String sSQL = "";

    public boolean ImprimirFactuIngreso() throws FileNotFoundException, IOException, SQLException {
        sSQL = "SELECT * FROM ingreso ORDER BY idingreso DESC LIMIT 1";

        try (Statement statement = cn.createStatement(); ResultSet resultSet = statement.executeQuery(sSQL)) {

            while (resultSet.next()) {
                Dingresop dts = new Dingresop();

                dts.setFechaingreso(resultSet.getString("fechaingreso"));
                dts.setPlaca(resultSet.getString("placa"));
                dts.setTiposervicio(resultSet.getString("tiposervicio"));
                dts.setTipovehiculo(resultSet.getString("tipovehiculo"));
                dts.setEmpleado(resultSet.getString("empleado"));
                String tipoVehiculo = resultSet.getString("tipovehiculo"); // Extraer el valor del ResultSet

                // Obtener la fecha y hora actual del sistema
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String fechaImpresion = now.format(formatter);
                String fecha = now.format(formatter1);

                // 1️⃣ GUARDAR FACTURA EN ARCHIVO DE TEXTO
//                String filePath = "C:\\Users\\Usuario\\Desktop\\ImprimirConJava" + dts.getPlaca() + ".txt";
//                try ( FileWriter fileWriter = new FileWriter(filePath);  PrintWriter printWriter = new PrintWriter(fileWriter)) {
//
//                    printWriter.println(" PITS S.A.S ");
//                    printWriter.println(" NIT: 901 121 607 ");
//                    printWriter.println(" GT JMC 0,2 Via Guarne");
//                    printWriter.println(" Tel: 604 4086060");
//                    printWriter.println(" Rionegro");
//                    printWriter.println(" REsponsable de iva");
//                    printWriter.println(" Fecha Ingreso: " + dts.getFechaingreso());
//                    printWriter.println(" Placa:" + dts.getPlaca());
//                    printWriter.println(" Modalidad de tarifa: " + dts.getTiposervicio());
//                    printWriter.println(" Tipo vehiculo:" + dts.getTipovehiculo());
//                    printWriter.println(" Numero: $" + dts.getNumero());
//                    printWriter.println(" Zona:" + dts.getCalle());
//                    printWriter.println(" REGLAMENTO");
//                    if (tipoVehiculo.equals("AUTO")) {
//                        printWriter.println("");
//                    } else if (tipoVehiculo.equals("MOTO")) {
//                        printWriter.println("No se responde por objetos dejados en la moto que no sea el caso");
//                    }
//                    printWriter.println();
//                    printWriter.println("-----------------------------------------------");
//                    printWriter.println(" Impresión realizada por Grupo GESNNOVA");
//                    printWriter.println(" Equipo: Parqueadero - JM");
//                    printWriter.println(" Fecha impresión:" + fechaImpresion);
//                }
//                System.out.println("Factura guardada en: " + filePath);
// 2️⃣ IMPRIMIR FACTURA
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

                    Style styleNormalCenter = new Style()
                            .setFontSize(Style.FontSize._1, Style.FontSize._1)
                            .setJustification(EscPosConst.Justification.Center);
                    Style styleNormal = new Style()
                            .setFontSize(Style.FontSize._1, Style.FontSize._1)
                            .setJustification(EscPosConst.Justification.Left_Default);
                    Style styleNegritaGrande = new Style()
                            .setBold(true)
                            .setFontSize(Style.FontSize._2, Style.FontSize._2)
                            .setJustification(EscPosConst.Justification.Left_Default);
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
                    escpos.writeLF(styleNormal, " Fecha Ingreso: " + dts.getFechaingreso());
                    escpos.writeLF(styleNegritaGrande, " Placa:" + dts.getPlaca());
                    escpos.writeLF(styleNormal, " Modalidad de tarifa: " + dts.getTiposervicio());
                    escpos.writeLF(styleNormal, " Tipo vehiculo:" + dts.getTipovehiculo());
                    escpos.writeLF(styleNormal, " REGLAMENTO");
                    if (tipoVehiculo.equals("AUTO")) {
                        escpos.writeLF(styleNormal, "");
                    } else if (tipoVehiculo.equals("MOTO")) {
                        escpos.writeLF(styleNormal, "No se responde por objetos dejados en la moto que no sea el caso");
                    }

                    escpos.feed(1);
                    escpos.writeLF(styleNormal, "----------------------------------------");
                    escpos.writeLF(styleNormal, "Impresión realizada por Grupo GESNNOVA");
                    escpos.writeLF(styleNormal, "Equipo: Parqueadero - COMBUGAS");
                    escpos.writeLF(styleNormal, "Fecha impresión:" + fechaImpresion);
                    escpos.writeLF(styleNormal, "Fabricante Software: GESNNOVA ");
                    escpos.writeLF(styleNormal, "NIT:901102506-1 ");
                    escpos.writeLF(styleNormal, "https://www.grupogesnnova.com");
                    escpos.feed(5); // Salto de líneas para finalizar la impresión
                    escpos.cut(EscPos.CutMode.FULL);
                }
                return true; // Se imprimió correctamente
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return false; // No se pudo imprimir
    }

}
