package LogicaP;


import DatosP.Dotrosserviciosp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Fotrosserviciosp {


//    public DefaultTableModel mostrar(String buscar) {
//        DefaultTableModel modelo;
//
//        String[] titulos = {"Id", "Fecha", "Tipo servicio", "Cliente", "Valor", "Efectivo", "Tarjeta", "Transferencia", "Total", "N° Turno", "Responsable", "Turno"};
//
//        String[] registro = new String[12];
//
//        totalregistros = 0;
//        modelo = new DefaultTableModel(null, titulos);
//
//        sSQL = "SELECT * FROM otrosservicios WHERE fecha LIKE ? ORDER BY idotros";
//
//        try {
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            pst.setString(1, "%" + buscar + "%");
//            ResultSet rs = pst.executeQuery();
//
//            while (rs.next()) {
//                registro[0] = rs.getString("idotros");
//                registro[1] = rs.getString("fecha");
//                registro[2] = rs.getString("tiposervicios");
//                registro[3] = rs.getString("cliente");
//                registro[4] = rs.getString("valor");
//                registro[5] = rs.getString("efectivo");
//                registro[6] = rs.getString("tarjeta");
//                registro[7] = rs.getString("transferencia");
//                registro[8] = rs.getString("total");
//                registro[9] = rs.getString("numturno");
//                registro[10] = rs.getString("responsable");
//                registro[11] = rs.getString("turno");
//
//                totalregistros++;
//                modelo.addRow(registro);
//            }
//            return modelo;
//
//        } catch (SQLException e) {
//            JOptionPane.showConfirmDialog(null, e);
//            return null;
//        }
//    }
//
//    public boolean insertar(Dotrosserviciosp dts) {
//        sSQL = "insert into otrosservicios ( fecha, tiposervicios, cliente, valor, efectivo, tarjeta, transferencia, total, numturno, responsable, turno )"
//                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        try {
//
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//
//            pst.setString(1, dts.getFecha());
//            pst.setString(2, dts.getTiposervicio());
//            pst.setString(3, dts.getCliente());
//            pst.setInt(4, dts.getValor());
//            pst.setInt(5, dts.getEfectivo());
//            pst.setInt(6, dts.getTarjeta());
//            pst.setInt(7, dts.getTransferencia());
//            pst.setInt(8, dts.getTotal());
//            pst.setString(9, dts.getNumturno());
//            pst.setString(10, dts.getResponsable());
//            pst.setString(11, dts.getTurno());
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
//
//    public boolean editar(Dotrosserviciosp dts) throws SQLException {
//        // Actualizar la sentencia SQL para incluir la cláusula WHERE
//        sSQL = "UPDATE otrosservicios SET fecha=?, tiposervicios=?, cliente=?, valor=?, efectivo=?, tarjeta=?, transferencia=?, total=?, "
//                + "numturno=?, responsable=?, turno=? WHERE idotros=?";
//
//        try ( PreparedStatement pst = cn.prepareStatement(sSQL)) {
//
//             pst.setString(1, dts.getFecha());
//            pst.setString(2, dts.getTiposervicio());
//            pst.setString(3, dts.getCliente());
//            pst.setInt(4, dts.getValor());
//            pst.setInt(5, dts.getEfectivo());
//            pst.setInt(6, dts.getTarjeta());
//            pst.setInt(7, dts.getTransferencia());
//            pst.setInt(8, dts.getTotal());
//            pst.setString(9, dts.getNumturno());
//            pst.setString(10, dts.getResponsable());
//            pst.setString(11, dts.getTurno());
//            // Ejecutar la actualización
//            int n = pst.executeUpdate();
//
//            // Devolver true si al menos una fila fue actualizada
//            return n != 0;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al editar: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean eliminar(Dotrosserviciosp dts) {
//        sSQL = "delete from otrosservicios where idotros=?";
//
//        try ( PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setInt(1, dts.getIdotros());
//
//            int n = pst.executeUpdate();
//            return n != 0;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al eliminar la INGRESO: " + e.getMessage());
//            return false;
//        }
//
//    }
}
