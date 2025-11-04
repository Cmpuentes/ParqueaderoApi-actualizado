package LogicaP;


import DatosP.Dcliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Fcliente {

//    private final Cconexionp mysql = new Cconexionp();
//    private final Connection cn = mysql.establecerConexionp();
//    private String sSQL = "";
//    public Integer totalregistros;
//    
//     ArrayList<Dcliente> listacliente = new ArrayList<>();
//
//    public void agregarcliente(Dcliente placa) {
//        listacliente.add(placa);
//    }
//
//    public DefaultTableModel mostrar( JTable tablaprepago) {
//        DefaultTableModel modelo;
//
//        String[] titulos = {"idcliente", "Fecha", "Placa", "Tipo vehiculo", "Tipo servicio",
//            "Cliente", "Telefono", "Tarifas", "Estado", "Observaciones"};
//
//        String[] registro = new String[10];
//
//        totalregistros = 0;
//        modelo = new DefaultTableModel(null, titulos);
//
//        sSQL = "SELECT * FROM cliente ORDER BY idcliente DESC";
//
//        try {
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//           
//            ResultSet rs = pst.executeQuery();
//
//            while (rs.next()) {
//                registro[0] = rs.getString("idcliente");
//                registro[1] = rs.getString("fecha");
//                registro[2] = rs.getString("placa");
//                registro[3] = rs.getString("tipo_vehiculo");
//                registro[4] = rs.getString("tipo_servicio");
//                registro[5] = rs.getString("cliente");
//                registro[6] = rs.getString("telefono");
//                registro[7] = rs.getString("tarifas");
//                registro[8] = rs.getString("estado");
//                registro[9] = rs.getString("observaciones");
//                totalregistros++;
//                modelo.addRow(registro);
//            }
//            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
//            tablaprepago.setRowSorter(sorter);
//            
//            return modelo;
//
//        } catch (SQLException e) {
//            JOptionPane.showConfirmDialog(null, e);
//            return null;
//        }
//    }
//
//    public boolean insertar(Dcliente dts) {
//        sSQL = "insert into cliente ( fecha, placa, tipo_vehiculo, tipo_servicio, cliente, telefono, tarifas, estado, observaciones)"
//                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        try {
//
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//
//            pst.setString(1, dts.getFecha());
//            pst.setString(2, dts.getPlaca());
//            pst.setString(3, dts.getTipo_vehiculo());
//            pst.setString(4, dts.getTipo_servicio());
//            pst.setString(5, dts.getCliente());
//            pst.setString(6, dts.getTelefono());
//            pst.setInt(7, dts.getTarifas());
//            pst.setString(8, dts.getEstado());
//            pst.setString(9, dts.getObservaciones());
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
//    public boolean editar(Dcliente dts) throws SQLException {
//        // Actualizar la sentencia SQL para incluir la cláusula WHERE
//        sSQL = "UPDATE cliente SET fecha=?, placa=?, tipo_vehiculo=?, tipo_servicio=?, cliente=?, telefono=?, tarifas=?, estado=?,"
//                + " observaciones=? WHERE idcliente=?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//
//            // Establecer los parámetros en el orden correcto
//            pst.setString(1, dts.getFecha());
//            pst.setString(2, dts.getPlaca());
//            pst.setString(3, dts.getTipo_vehiculo());
//            pst.setString(4, dts.getTipo_servicio());
//            pst.setString(5, dts.getCliente());
//            pst.setString(6, dts.getTelefono());
//            pst.setInt(7, dts.getTarifas());
//            pst.setString(8, dts.getEstado());
//            pst.setString(9, dts.getObservaciones());
//
//            pst.setInt(10, dts.getIdcliente());
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
//
//    public boolean eliminar(Dcliente dts) {
//        sSQL = "delete from cliente where idcliente=?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setInt(1, dts.getIdcliente());
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
