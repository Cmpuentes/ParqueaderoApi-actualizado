
package Impresionp;


public class CopiaCierreTurno {
//    private final Cconexionp mysql = new Cconexionp();
//    private final Connection cn = mysql.establecerConexionp();
//    private String sSQL = "";

//    public DefaultTableModel mostrar(String buscar) {
//        DefaultTableModel modelo;
//
//        String[] titulos = {"EMPLEADO", "TURNO", "N° TURNO", "FECHA INICIO", "FECHA SALIDA", "EFECTIVO", "TARJETAS",
//            "TRANSFERENCIA", "TOTAL ABONOS", "OTROS INGRESOS", "TOTAL RECAUDADO", "EMTREGA ADMON", "OBSERVACIONES",};
//
//        String[] registro = new String[13];
//
//        modelo = new DefaultTableModel(null, titulos);
//
//        sSQL = "SELECT * FROM salida_turno where turno like ? ORDER BY idfinturno desc";
//
//        try {
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            pst.setString(1, "%" + buscar + "%");
//            ResultSet rs = pst.executeQuery();
//
//            while (rs.next()) {
//                registro[0] = rs.getString("empleado");
//                registro[1] = rs.getString("turno");
//                registro[2] = rs.getString("numero_turno");
//                registro[3] = rs.getString("fechaingreso");
//                registro[4] = rs.getString("fechasalida");
//                registro[5] = rs.getString("efectivo");
//                registro[6] = rs.getString("tarjeta");
//                registro[7] = rs.getString("transferencia");
//                registro[8] = rs.getString("total_abonos");
//                registro[9] = rs.getString("otros_ingresos");
//                registro[10] = rs.getString("total_recaudado");
//                registro[11] = rs.getString("efectivo_liquido");
//                registro[12] = rs.getString("observaciones");
//
//                modelo.addRow(registro);
//            }
//            return modelo;
//
//        } catch (SQLException e) {
//            JOptionPane.showConfirmDialog(null, e);
//            return null;
//        }
//    }

//    public void imprimirFilaSeleccionada(JTable tablalistadoCopiaTurno) throws SQLException, IOException, PrinterException {
//        int fila = tablalistadoCopiaTurno.getSelectedRow(); // Obtener la fila seleccionada
//
//        if (fila == -1) {
//            JOptionPane.showMessageDialog(null, "Seleccione una fila para imprimir.", "Advertencia", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        String numTurno = tablalistadoCopiaTurno.getValueAt(fila, 2).toString();
//
//        sSQL = "SELECT * FROM salida_turno WHERE numero_turno = ?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setString(1, numTurno);
////            System.out.println("numero tirno"+numTurno);
//            ResultSet resultSet = pst.executeQuery();
//
//            if (resultSet.next()) {
//                Dsalidaturnop dts = new Dsalidaturnop();
//                dts.setTurno(resultSet.getString("turno"));
//                dts.setNumero_turno(resultSet.getString("numero_turno"));
//                dts.setEmpleado(resultSet.getString("empleado"));
//                dts.setFechaingreso(resultSet.getString("fechaingreso"));
//                dts.setFechasalida(resultSet.getString("fechasalida"));
//                dts.setTotal_vehiculos(resultSet.getString("total_vehiculos"));
//                dts.setEfectivo(resultSet.getInt("efectivo"));
//                dts.setTarjeta(resultSet.getInt("tarjeta"));
//                dts.setTransferencia(resultSet.getInt("transferencia"));
//                dts.setOtros_ingresos(resultSet.getInt("otros_ingresos"));
//                dts.setEfectivo_liquido(resultSet.getInt("efectivo_liquido"));
//                dts.setTotal_recaudado(resultSet.getInt("total_recaudado"));
//                dts.setObservaciones(resultSet.getString("observaciones"));
//                dts.setTotal_abonos(resultSet.getInt("total_abonos"));
//
//                imprimirDatos(dts); // Llamar al método de impresión
//            } else {
//                JOptionPane.showMessageDialog(null, "No se encontró el turno seleccionado.");
//            }
//        }
//    }

//    public void imprimirDatos(Dsalidaturnop turno) throws IOException, PrinterException {
//
//        // Obtener la fecha y hora actual del sistema
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
//        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        String fechaImpresion = now.format(formatter);
//        String fecha = now.format(formatter1);
//
//        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
//        if (printService == null) {
//            System.out.println("No hay impresora disponible.");
//            return;
//        }
//
//        try (EscPos escpos = new EscPos(new PrinterOutputStream(printService))) {
//
//            // Ruta del logo (ajústala según la ubicación de tu imagen)
//            InputStream is = getClass().getResourceAsStream("/Imagenes/Iconocombugas.png");
//            if (is == null) {
//                System.out.println("Error: No se pudo cargar la imagen desde los recursos.");
//            }
//            BufferedImage image = ImageIO.read(is);
//
//            EscPosImage escposImage = new EscPosImage(new CoffeeImageImpl(image), new BitonalThreshold());
//            ImageWrapperInterface imageWrapper = new RasterBitImageWrapper();
//            imageWrapper.setJustification(EscPosConst.Justification.Center);
//
//            Style styleNormal = new Style()
//                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
//                    .setJustification(EscPosConst.Justification.Left_Default);
//
//            Style styleNormalCenter = new Style()
//                    .setBold(true)
//                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
//                    .setJustification(EscPosConst.Justification.Center);
//
//            escpos.write(imageWrapper, escposImage);
//
//            escpos.writeLF(styleNormalCenter, "HOTEL COMBUGAS S.A.S ");
//            escpos.writeLF(styleNormalCenter, "NIT: 900139412-4 ");
//            escpos.writeLF(styleNormalCenter, "TEL:3205417916");
//            escpos.writeLF(styleNormalCenter, "CARTAGENA");
//            escpos.writeLF(styleNormalCenter, "DG 31D N.32A-25B.TERNERA");
//            escpos.feed(1);
//            escpos.writeLF(styleNormal, "Fecha Hora Inicio: " + turno.getFechaingreso());
//            escpos.writeLF(styleNormal, "Fecha Hora Salida: " + turno.getFechasalida());
//            escpos.feed(1);
//            escpos.writeLF(styleNormal, "Total Recaudo: " + turno.getTotal_recaudado());
//            escpos.feed(1);;
//            escpos.writeLF(styleNormal, "Detalle del recaudado");
//            escpos.feed(1);
//            escpos.writeLF(styleNormal, "Tarjetas: " + turno.getTarjeta());
//            escpos.writeLF(styleNormal, "Efectivo: " + turno.getEfectivo());
//            escpos.writeLF(styleNormal, "Transferencias: " + turno.getTransferencia());
//            escpos.writeLF(styleNormal, "Total Abonos: " + turno.getTotal_abonos());
//            escpos.writeLF(styleNormal, "Otros Ingresos: " + turno.getOtros_ingresos());
//            escpos.writeLF(styleNormal, "Efectivo Liquido: " + turno.getEfectivo_liquido());
//            escpos.writeLF(styleNormal, "Observaciones: " + turno.getObservaciones());
//            escpos.feed(1);
//            escpos.writeLF(styleNormal, "Empleado: " + turno.getEmpleado());
//            escpos.writeLF(styleNormal, "Turno: " + turno.getTurno());
//            escpos.writeLF(styleNormal, "Numero Turno: " + turno.getNumero_turno());
//            escpos.writeLF(styleNormal, "Fecha impresión:" + fechaImpresion);
//            escpos.feed(1);
//            escpos.writeLF(styleNormal, "Fabricante Software: GESNNOVA ");
//            escpos.writeLF(styleNormal, "NIT:901102506-1 ");
//            escpos.writeLF(styleNormal, "https://www.grupogesnnova.com");
//            escpos.feed(5);
//            escpos.cut(EscPos.CutMode.FULL); // Realiza el corte del papel
//        }
//    }
}
