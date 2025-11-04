package LogicaP;

import DatosP.Dcliente;
import DatosP.Dingresop;
import DatosP.Dinicioturnop;
import DatosP.Dtarifas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Fsalidap {

//    private final Cconexionp mysql = new Cconexionp();
//    private final Connection cn = mysql.establecerConexionp();
//    private String sSQL = "";
//    public Integer totalregistros;
//    Statement st;
//    ResultSet rs;
//
//    public DefaultTableModel mostrarsalida(String buscar) {
//        DefaultTableModel modelo;
//
//        String[] titulos = {"ID", "placa", "tipovehiculo", "tiposervicio", "cliente", "fechaentrada", "fechasalida", "dias", "horas", "minutos",
//            "valor", "numero_recibo", "descuento", "subtotal", "efectivo", "tarjeta", "transferencia", "total", "Acción"};
//
//        String[] registro = new String[18];
//
//        totalregistros = 0;
//        modelo = new DefaultTableModel(null, titulos);
//
//        sSQL = "SELECT * FROM salida s "
//                + "inner join inicio_turno i on i.numero_turno = s.turnosalida "
//                + " where i.estado = 'Activo'  ORDER BY idsalida desc";
//
//        try {
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            ResultSet rs1 = pst.executeQuery();
//
//            while (rs1.next()) {
//                registro[0] = rs1.getString("idsalida");
//                registro[1] = rs1.getString("placa");
//                registro[2] = rs1.getString("tipovehiculo");
//                registro[3] = rs1.getString("tiposervicio");
//                registro[4] = rs1.getString("cliente");
//                registro[5] = rs1.getString("fechaentrada");
//                registro[6] = rs1.getString("fechasalida");
//                registro[7] = rs1.getString("dias");
//                registro[8] = rs1.getString("horas");
//                registro[9] = rs1.getString("minutos");
//                registro[10] = rs1.getString("valor");
//                registro[11] = rs1.getString("numero_recibo");
//                registro[12] = rs1.getString("descuento");
//                registro[13] = rs1.getString("subtotal");
//                registro[14] = rs1.getString("efectivo");
//                registro[15] = rs1.getString("tarjeta");
//                registro[16] = rs1.getString("transferencia");
//                registro[17] = rs1.getString("total");
//
//                totalregistros++;
//                modelo.addRow(registro.clone()); // Clonamos el arreglo antes de añadirlo al modelo
//            }
//            return modelo;
//
//        } catch (SQLException e) {
//            JOptionPane.showConfirmDialog(null, e);
//            return null;
//        }
//    }
//
////    public boolean insertar(Dsalidap dts) {
////        sSQL = "insert into salida (idingreso,placa,tipovehiculo,"
////                + "tiposervicio, cliente, fechaentrada,fechasalida,zona,numfactura,dias,horas,minutos,"
////                + "valor, numero_recibo, descuento, subtotal,efectivo,tarjeta,transferencia,total,turno,"
////                + "turnoentrada,empleadoentrada,turnosalida,empleadosalida )"
////                + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
////
////        try {
////
////            PreparedStatement pst = cn.prepareStatement(sSQL);
////            pst.setInt(1, dts.getIdingreso());
////            pst.setString(2, dts.getPlaca());
////            pst.setString(3, dts.getTipovehiculo());
////            pst.setString(4, dts.getTiposervicio());
////            pst.setString(5, dts.getCliente());
////            pst.setString(6, dts.getFechaentrada());
////            pst.setString(7, dts.getFechasalida());
////            pst.setString(8, dts.getZona());
////            pst.setString(9, dts.getNumfactura());
////            pst.setInt(10, dts.getDias());
////            pst.setInt(11, dts.getHoras());
////            pst.setInt(12, dts.getMinutos());
////            pst.setInt(13, dts.getValor());
////            pst.setString(14, dts.getNumero_recibo());
////            pst.setInt(15, dts.getDescuento());
////            pst.setInt(16, dts.getSubtotal());
////            pst.setInt(17, dts.getEfectivo());
////            pst.setInt(18, dts.getTarjeta());
////            pst.setInt(19, dts.getTransferencia());
////            pst.setInt(20, dts.getTotal());
////            pst.setString(21, dts.getTurno());
////            pst.setString(22, dts.getTurnoentrada());
////            pst.setString(23, dts.getEmpleadoentrada());
////            pst.setString(24, dts.getTurnosalida());
////            pst.setString(25, dts.getEmpleadosalida());
////
////            int n = pst.executeUpdate();
////
//////            JOptionPane.showMessageDialog(null, "DATOS ALMACENADOS CORRECTAMENTE");
////            return n != 0;
////
////        } catch (SQLException e) {
////            JOptionPane.showConfirmDialog(null, e);
////            return false;
////        }
////    }
//
//    public Dinicioturnop numeroturnop() {
//        int numeroturno = 0;
//        int idturno = 0;
//        sSQL = "SELECT numero_turno, idturno FROM inicio_turno WHERE estado = 'Activo' ORDER BY numero_turno DESC LIMIT 1";
//        try {
//
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            ResultSet rs2 = pst.executeQuery();
//
//            if (rs2.next()) {
//                numeroturno = rs2.getInt("numero_turno");
//                idturno = rs2.getInt("idturno");
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al obtener el numero del turno: " + e.getMessage());
//        }
//
//        return new Dinicioturnop(numeroturno, idturno);
//    }
//
//    public Dinicioturnop empleado(String placa) {
//        Dinicioturnop datoempleado = null;
//        sSQL = "SELECT numeroturno, empleado FROM ingreso WHERE placa = ? AND estado = 'Activo'";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setString(1, placa); // Asigna el valor de la placa al parámetro de la consulta
//
//            try (ResultSet rs3 = pst.executeQuery()) {
//                if (rs3.next()) { // Verifica si hay resultados
//                    // Procesa el resultado si es necesario, por ejemplo:
//                    int numeroTurno = rs3.getInt("numeroturno");
//                    String empleado = rs3.getString("empleado");
////                    System.out.println("Turno: " + numeroTurno + ", Empleado: " + empleado);
//
//                    datoempleado = new Dinicioturnop(numeroTurno, empleado);
//
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al buscar el empleado: " + e.getMessage());
//        }
//
//        return datoempleado; // Retorna false si no se encuentra un registro
//    }
//
//    public int generarComprobante() {
//        String serie = "";
//        sSQL = "SELECT MAX(CAST(numfactura AS UNSIGNED)) FROM salida WHERE numfactura REGEXP '^[0-9]+$';";
//
//        try {
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            ResultSet rs4 = pst.executeQuery();
//            if (rs4.next()) {
//                serie = rs4.getString(1);
//                if (serie != null) {
//                    // Incrementar el valor de serie
//                    return Integer.parseInt(serie) + 1;
//                } else {
//                    // Si no hay registros, el primer número de turno es 1
//                    return 1;
//                }
//            } else {
//                // Si la consulta no devuelve resultados, también retornamos 1
//                return 1;
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al generar el número: " + e.getMessage());
//            return 0;
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(null, "Error al convertir el número: " + e.getMessage());
//            return 0;
//        }
//    }
//
//    public int[] contarEstadoActivo() {
//        int[] conteos = new int[2]; // Índice 0 para Autos, índice 1 para Motos
//        String sqlAuto = "SELECT COUNT(tipovehiculo) AS estado FROM ingreso WHERE estado = 'Activo' ";
//
//        try {
//            // Consulta para Autos
//            PreparedStatement pstAuto = cn.prepareStatement(sqlAuto);
//            ResultSet rsAuto = pstAuto.executeQuery();
//            if (rsAuto.next()) {
//                conteos[0] = rsAuto.getInt("estado");
//            }
//
//        } catch (SQLException e) {
//            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
//        }
//
//        return conteos; // Retornar los resultados
//    }
//
//    //consulta para traer el tipo de servicio de ingreso y hacer el calculo
//    public Dtarifas tiposervicio(String Placa, String tiposervicio, String tipovehiculo) {
//        int precio12h = 0;
//        int preciohora = 0;
//        sSQL = "SELECT t.precio12h, t.preciohoras "
//                + "FROM tarifas t "
//                + "JOIN ingreso i ON i.tiposervicio = t.tiposervicio "
//                + "WHERE  t.tiposervicio = ? AND i.placa = ? AND t.tipovehiculo = ?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            // Asigna el valor de la placa al parámetro de la consulta
//            pst.setString(1, tiposervicio);
//            pst.setString(2, Placa);
//            pst.setString(3, tipovehiculo);
//
//            try (ResultSet rs5 = pst.executeQuery()) {
//                if (rs5.next()) { // Verifica si hay resultados
//                    // Procesa el resultado si es necesario, por ejemplo:
//                    precio12h = rs5.getInt("precio12h");
//                    preciohora = rs5.getInt("preciohoras");
//
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al buscar el precio: " + e.getMessage());
//        }
//
//        return new Dtarifas(precio12h, preciohora);
//    }
//
//    //consulta para recalcular el tipo de servicio
//    public Dcliente recalculartiposervicio(String Placa, String tiposervicio, String tipovehiculo) {
//        int tarifas = 0;
//
//        sSQL = "SELECT c.tarifas FROM cliente c JOIN ingreso i ON i.tiposervicio = c.tipo_servicio "
//                + "WHERE  c.tipo_servicio = ? AND c.placa = ? AND c.tipo_vehiculo = ?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            // Asigna el valor de la placa al parámetro de la consulta
//            pst.setString(1, tiposervicio);
//            pst.setString(2, Placa);
//            pst.setString(3, tipovehiculo);
//
//            try (ResultSet rs7 = pst.executeQuery()) {
//                if (rs7.next()) { // Verifica si hay resultados
//                    // Procesa el resultado si es necesario, por ejemplo:
//                    tarifas = rs7.getInt("tarifas");
//
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al buscar el precio: " + e.getMessage());
//        }
//
//        return new Dcliente(tarifas);
//    }
//
//    public void llenarcboturno(JComboBox<String> combo) {
//
//        DefaultComboBoxModel<String> com = new DefaultComboBoxModel<>();  // Modelo para el JComboBox
//        combo.setModel(com);  // Asignamos el modelo al combo
//        Cconexionp conexion = new Cconexionp();  // Conexión a la base de datos
//
//        com.addElement("PLACA");  // Primer elemento del ComboBox
//
//        try {
//            // Establecer la conexión y ejecutar la consulta SQL
//            Connection conectar = conexion.establecerConexionp();
//            st = conectar.createStatement();
//            rs = st.executeQuery("select placa from ingreso where estado = 'Activo' order by idingreso");
//
//            // Recorrer los resultados de la consulta
//            while (rs.next()) {
//                // Obtener placa desde el ResultSet
//                String placa = rs.getString("placa");
//
//                // Agregar la placa modelo del ComboBox
//                com.addElement(placa);
//
//                Lingresop func = new Lingresop();
//                Dingresop placas = new Dingresop();
//                placas.setPlaca(placa);
//                //func.agregarplaca(placas);
//            }
//        } catch (SQLException e) {
//            System.out.println("ERROR: " + e.getMessage());
//        }
//
//    }
//
//    public double obtenerPrecio(String tiposervicio, String tipovehiculo) {
//        double precio = 0;
//        try {
//            sSQL = "SELECT precio FROM tarifas WHERE tipovehiculo = ? AND tipo_servicio = ?";
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            pst.setString(1, tipovehiculo);
//            pst.setString(2, tiposervicio);
//            ResultSet rs6 = pst.executeQuery();
//
//            if (rs6.next()) {
//                precio = rs6.getInt("precio");
//
//            } else {
//
//            }
//        } catch (SQLException e) {
//
//        }
//        return precio;
//    }
//
//    public int[] obtenerTarifa(String tiposervicio, String placa, String tipovehiculo) {
//        int[] tarifa = new int[2]; // índice 0: descuento, índice 1: preciohoras
//        tarifa[0] = 0; // Valor inicial para descuento
//        tarifa[1] = 0; // Valor inicial para preciohoras
//
//        sSQL = "SELECT t.descuentorecibo, t.preciohoras "
//                + "FROM tarifas t "
//                + "JOIN ingreso i ON i.tiposervicio = t.tiposervicio "
//                + "WHERE t.tiposervicio = ? AND i.placa = ? AND t.tipovehiculo = ?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            // Asignar los parámetros a la consulta
//            pst.setString(1, tiposervicio);
//            pst.setString(2, placa);
//            pst.setString(3, tipovehiculo);
//
//            try (ResultSet rs8 = pst.executeQuery()) {
//                if (rs8.next()) {
//                    // Obtener los valores de descuento y preciohoras
//                    tarifa[0] = rs8.getInt("descuentorecibo");
//                    tarifa[1] = rs8.getInt("preciohoras");
////                    System.out.println("Descuento obtenido: " + tarifa[0]);
////                    System.out.println("Precio por hora obtenido: " + tarifa[1]);
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al obtener los datos de la tarifa: " + e.getMessage());
//        }
//
//        return tarifa;
//    }
//
//    public boolean editarSalida(int idSalida, String efectivo, String tarjeta, String transferencia, String total) {
//        sSQL = "UPDATE salida SET efectivo = ?, tarjeta = ?, transferencia = ?, total = ? "
//                + "WHERE idsalida = ?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setInt(1, Integer.parseInt(efectivo));
//            pst.setInt(2, Integer.parseInt(tarjeta));
//            pst.setInt(3, Integer.parseInt(transferencia));
//            pst.setInt(4, Integer.parseInt(total));
//            pst.setInt(5, idSalida);
//
//            int n = pst.executeUpdate();
//            return n != 0;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al editar el ingreso: " + e.getMessage());
//            return false;
//        }
//    }
//
//
//    public int[] obtenerTotalesSalida() {
//         sSQL = "SELECT SUM(efectivo) AS efectivo, SUM(tarjeta) AS tarjeta, SUM(transferencia) AS transferencia "
//                    + "FROM salida s "
//                    + "JOIN inicio_turno i ON s.turnosalida = i.numero_turno "
//                    + "WHERE i.estado = 'Activo'";
//
//        int[] totales = new int[3]; // [efectivo, tarjeta, transferencia]
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL);
//             ResultSet rs = pst.executeQuery()) {
//
//            if (rs.next()) {
//                totales[0] = rs.getInt("efectivo");
//                totales[1] = rs.getInt("tarjeta");
//                totales[2] = rs.getInt("transferencia");
//            }
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al consultar totales: " + e.getMessage());
//        }
//
//        return totales;
//    }


 

}
    
