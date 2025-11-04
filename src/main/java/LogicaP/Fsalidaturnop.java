package LogicaP;


import DatosP.Dsalidaturnop;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * @author Alexander nieves romero
 */
public class Fsalidaturnop {

//    private final Cconexionp mysql = new Cconexionp();
//    private final Connection cn = mysql.establecerConexionp();
//    private String sSQL = "";
//    public Integer totalregistros;

//    public boolean insertar(Dsalidaturnop dts) {
//        sSQL = "insert into salida_turno ( idfinturno, turno, numero_turno, empleado, fechaingreso, fechasalida, recibos, total_vehiculos,"
//                + " base, efectivo, tarjeta, transferencia, otros_ingresos, efectivo_liquido, total_recaudado, estado, observaciones, total_abonos)"
//                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        try {
//
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//
//            pst.setInt(1, dts.getIdfinturno());
//            pst.setString(2, dts.getTurno());
//            pst.setString(3, dts.getNumero_turno());
//            pst.setString(4, dts.getEmpleado());
//            pst.setString(5, dts.getFechaingreso());
//            pst.setString(6, dts.getFechasalida());
//            pst.setString(7, dts.getRecibos());
//            pst.setString(8, dts.getTotal_vehiculos());
//            pst.setInt(9, dts.getBase());
//            pst.setInt(10, dts.getEfectivo());
//            pst.setInt(11, dts.getTarjeta());
//            pst.setInt(12, dts.getTransferencia());
//            pst.setInt(13, dts.getOtros_ingresos());
//            pst.setInt(14, dts.getEfectivo_liquido());
//            pst.setInt(15, dts.getTotal_recaudado());
//            pst.setInt(16, dts.getBase());
//            pst.setString(17, dts.getObservaciones());
//            pst.setInt(18, dts.getTotal_abonos());
//
//            int n = pst.executeUpdate();
//
////        JOptionPane.showMessageDialog(null, "DATOS ALMACENADOS CORRECTAMENTE");
//            return n != 0;
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, e);
//            return false;
//        }
//    }

//    public ResultSet realizarConsulta(int inicioturno) throws SQLException {
//
//        sSQL = "select *from inicio_turno where numero_turno=?";
//
//        PreparedStatement pst = cn.prepareStatement(sSQL);
//        pst.setInt(1, inicioturno);
//
//        return pst.executeQuery();
//    }

//    public int numeroturno() {
//        int numeroturno = 0;
//        sSQL = "SELECT numero_turno FROM inicio_turno WHERE estado = 'Activo' ORDER BY numero_turno DESC LIMIT 1";
//        try {
//
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            ResultSet rs = pst.executeQuery();
//
//            if (rs.next()) {
//                numeroturno = rs.getInt("numero_turno");
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al obtener el numero del turno: " + e.getMessage());
//        }
//
//        return numeroturno;
//    }

//    public int[] totalmedio_pagos(int numeroturno) {
//        int[] mediosDePago = new int[3]; // Almacena los totales de efectivo, tarjeta y transferencia
//
//        sSQL = "SELECT "
//                + " SUM(efectivo) AS total_efectivo, "
//                + " SUM(tarjeta) AS total_tarjeta, "
//                + " SUM(transferencia) AS total_transferencia "
//                + " FROM ( "
//                + "    SELECT s.efectivo, s.tarjeta, s.transferencia FROM salida s "
//                + "    JOIN ingreso i ON i.idingreso = s.idingreso "
//                + "    WHERE s.turnosalida = ? AND i.tiposervicio = 'GENERAL' "
//                + "    UNION ALL "
//                + "    SELECT a.efectivo, a.tarjeta, a.transferencia FROM abonos a "
//                + "    WHERE a.numero_turno = ? "
//                + ") AS union_tablas";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setInt(1, numeroturno);
//            pst.setInt(2, numeroturno); // Se debe pasar el segundo parámetro también
//
//            try (ResultSet rs = pst.executeQuery()) {
//                if (rs.next()) {
//                    // Verificar si los valores son NULL antes de asignarlos
//                    mediosDePago[0] = rs.getInt("total_efectivo");
//                    if (rs.wasNull()) {
//                        mediosDePago[0] = 0;
//                    }
//
//                    mediosDePago[1] = rs.getInt("total_tarjeta");
//                    if (rs.wasNull()) {
//                        mediosDePago[1] = 0;
//                    }
//
//                    mediosDePago[2] = rs.getInt("total_transferencia");
//                    if (rs.wasNull()) {
//                        mediosDePago[2] = 0;
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al obtener totales en medios de pago: " + e.getMessage());
//            return new int[]{0, 0, 0}; // En caso de error, retornar valores 0
//        }
//
//        return mediosDePago; // Retornar el array con los totales
//    }

//    public int[] totalmedio_pagos_abonos(int NumeroTurno) {
//        int[] mediosDePago_abonos = new int[3]; // Almacena los totales de efectivo, tarjeta y transferencia
//
//        sSQL = "SELECT sum(efectivo) as efectivo, sum(tarjeta) as tarjeta, sum(transferencia) as transferencia "
//                + "from abonos where numero_turno = ? ";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setInt(1, NumeroTurno);
//
//            try (ResultSet rs = pst.executeQuery()) {
//                if (rs.next()) {
//                    // Verificar si los valores son NULL antes de asignarlos
//                    mediosDePago_abonos[0] = rs.getInt("efectivo");
//                    if (rs.wasNull()) {
//                        mediosDePago_abonos[0] = 0;
//                    }
//
//                    mediosDePago_abonos[1] = rs.getInt("tarjeta");
//                    if (rs.wasNull()) {
//                        mediosDePago_abonos[1] = 0;
//                    }
//
//                    mediosDePago_abonos[2] = rs.getInt("transferencia");
//                    if (rs.wasNull()) {
//                        mediosDePago_abonos[2] = 0;
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al obtener totales en medios de pago: " + e.getMessage());
//            return new int[]{0, 0, 0}; // En caso de error, retornar valores 0
//        }
//
//        return mediosDePago_abonos; // Retornar el array con los totales
//    }

//    public String Consultaempleado(int idturno) throws SQLException {
//        String empleado1 = "";
//        sSQL = "SELECT empleado FROM inicio_turno WHERE idturno =? AND estado ='Activo';";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setInt(1, idturno);
//
//            try (ResultSet rs = pst.executeQuery()) {
//                if (rs.next()) {
//                    empleado1 = rs.getString("empleado");
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al obtener el dato del empleado: " + e.getMessage());
//        }
//
//        return empleado1;
//    }

}
