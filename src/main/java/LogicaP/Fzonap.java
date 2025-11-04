package LogicaP;


import DatosP.Dzona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Fzonap {
;

//    public DefaultTableModel mostrar(String buscar) {
//        DefaultTableModel modelo;
//
//        String[] titulos = {"Id", "Estado", "Numero"};
//
//        String[] registro = new String[4];
//
//        totalregistros = 0;
//        modelo = new DefaultTableModel(null, titulos);
//
//        sSQL = "SELECT * FROM zona WHERE numero LIKE ? ORDER BY idzona";
//
//        try {
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            pst.setString(1, "%" + buscar + "%");
//            ResultSet rs = pst.executeQuery();
//
//            while (rs.next()) {
//                registro[0] = rs.getString("idzona");
//                registro[1] = rs.getString("estado");
//                registro[2] = rs.getString("numero");
//               
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

//    public boolean insertar(Dzona dts) {
//        sSQL = "insert into zona ( estado, numero)"
//                + " values (?, ?)";
//        try {
//
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//
//            pst.setString(1, dts.getEstado());
//            pst.setString(2, dts.getNumero());
//           
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

//    public boolean editar(Dzona dts) throws SQLException {
//        // Actualizar la sentencia SQL para incluir la cláusula WHERE
//        sSQL = "UPDATE zona SET estado=?, numero=? WHERE idzona=?";
//
//        try ( PreparedStatement pst = cn.prepareStatement(sSQL)) {
//
//            // Establecer los parámetros en el orden correcto
//            pst.setString(1, dts.getEstado());
//            pst.setString(2, dts.getNumero());
//            // 
//            pst.setInt(3, dts.getIdzona());
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

//    public String obtenerEstadozona(int numero) {
//        String estado = "Disponible";
//         sSQL = "SELECT estado FROM zona WHERE numero = ?";
//
//        try ( PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setInt(1, numero);
//            try ( ResultSet rs = pst.executeQuery()) {
//                if (rs.next()) {
//                    estado = rs.getString("estado");
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al obtener el estado de la zona: " + e.getMessage());
//        }
//
//        return estado;
//    }
//
//    public boolean ocupar(Dzona dts) {
//        sSQL = "UPDATE zona SET estado='Ocupado' WHERE numero=?";
//
//        try ( PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setString(1, dts.getNumero());
//
//            int n = pst.executeUpdate();
//            return n != 0;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al ocupar la zona: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean Desocupar(Dzona dtszona) {
//        sSQL = "UPDATE zona SET estado='Disponible' WHERE numero=?";
//
//        try ( PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setString(1, dtszona.getNumero());
//
//            int n = pst.executeUpdate();
//            return n != 0;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al desocupar la zona: " + e.getMessage());
//            return false;
//        }
//    }
}
