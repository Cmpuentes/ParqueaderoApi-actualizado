package LogicaP;


import DatosP.Dtarifas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Ftarifas {

//    private final Cconexionp mysql = new Cconexionp();
//    private final Connection cn = mysql.establecerConexionp();
//    private String sSQL = "";
//    public Integer totalregistros;

//    public DefaultTableModel mostrar(String buscar) {
//        DefaultTableModel modelo;
//
//        String[] titulos = {"ID", "Tipo Vehiculo", "Tipo Servicio", "Precio 12h", "Descuento Recibo", "Precio Horas"};
//
//        String[] registro = new String[6];
//
//        totalregistros = 0;
//        modelo = new DefaultTableModel(null, titulos);
//
//        sSQL = "SELECT * FROM tarifas WHERE tipovehiculo LIKE ? ORDER BY idtarifas";
//
//        try {
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            pst.setString(1, "%" + buscar + "%");
//            ResultSet rs = pst.executeQuery();
//
//            while (rs.next()) {
//                registro[0] = rs.getString("idtarifas");
//                registro[1] = rs.getString("tipovehiculo");
//                registro[2] = rs.getString("tiposervicio");
//                registro[3] = rs.getString("precio12h");
//                registro[4] = rs.getString("descuentorecibo");
//                registro[5] = rs.getString("preciohoras");
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

    //Vamos por aquí
//    public boolean insertar(Dtarifas dts) {
//        sSQL = "insert into tarifas ( tipovehiculo, tiposervicio, precio12h, descuentorecibo, preciohoras)"
//                + " values (?, ?, ?, ?, ?)";
//        try {
//
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//
//            pst.setString(1, dts.getTipovehiculo());
//            pst.setString(2, dts.getTiposervicio());
//            pst.setInt(3, dts.getDescuentorecibo());
//            pst.setInt(4, dts.getPrecio12h());
//            pst.setInt(5, dts.getPreciohoras());
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

//    public boolean editar(Dtarifas dts) throws SQLException {
//        // Actualizar la sentencia SQL para incluir la cláusula WHERE
//        sSQL = "UPDATE tarifas SET  tipovehiculo=?, tiposervicio=?, precio12h=?, descuentorecibo=?, preciohoras=? WHERE idtarifas=?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//
//            // Establecer los parámetros en el orden correcto
//             pst.setString(1, dts.getTipovehiculo());
//            pst.setString(2, dts.getTiposervicio());
//            pst.setInt(3, dts.getDescuentorecibo());
//            pst.setInt(4, dts.getPrecio12h());
//            pst.setInt(5, dts.getPreciohoras());
//            pst.setInt(6, dts.getIdtarifas());  // idtarifas (el identificador que se debe actualizar)
//
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

//    public boolean eliminar(Dtarifas dts) throws SQLException {
//        sSQL = "delete from tarifas where idtarifas=?";
//
//           try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setInt(1, dts.getIdtarifas());
//
//            int n = pst.executeUpdate();
//            return n != 0;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
//            return false;
//        }
//
//    }

}
