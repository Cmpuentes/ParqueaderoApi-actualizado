package LogicaP;

import DatosP.Dabonosp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alexn
 */
public class Fabonosp {

//    private final Cconexionp mysql = new Cconexionp();
//    private final Connection cn = mysql.establecerConexionp();
//    private String sSQL = "";
//    public Integer totalregistros;
//    Statement st;
//    ResultSet rs;
//    ArrayList<Dabonosp> listacliente = new ArrayList<>();
//
//    public void agregarCliente(Dabonosp cliente) {
//        listacliente.add(cliente);
//    }

//    public DefaultTableModel mostrar(String buscar) {
//        DefaultTableModel modelo;
//
//        String[] titulos = {"idabono", "Fecha", "Cliente", "Valor", "Efectivo", "Tarjeta", "Transferencia",
//            "Total", "Saldo", "Observaciones", "Empleado", "Turno", "numero_turno"};
//
//        String[] registro = new String[13];
//
//        totalregistros = 0;
//        modelo = new DefaultTableModel(null, titulos);
//
//        sSQL = "SELECT * FROM abonos WHERE cliente LIKE ? ORDER BY idabonos";
//
//        try {
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            pst.setString(1, "%" + buscar + "%");
//            ResultSet rs1 = pst.executeQuery();
//
//            while (rs1.next()) {
//                registro[0] = rs1.getString("idabonos");
//                registro[1] = rs1.getString("fecha");
//                registro[2] = rs1.getString("cliente");
//                registro[3] = rs1.getString("valor");
//                registro[4] = rs1.getString("efectivo");
//                registro[5] = rs1.getString("tarjeta");
//                registro[6] = rs1.getString("transferencia");
//                registro[7] = rs1.getString("total");
//                registro[8] = rs1.getString("saldo");
//                registro[9] = rs1.getString("observaciones");
//                registro[10] = rs1.getString("empleado");
//                registro[11] = rs1.getString("turno");
//                registro[12] = rs1.getString("numero_turno");
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

//    public boolean insertar(Dabonosp dts) {
//        sSQL = "insert into abonos ( fecha, cliente, valor, efectivo, tarjeta,"
//                + " transferencia, total, observaciones, empleado, turno, numero_turno, saldo)"
//                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
//        try {
//
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//
//            pst.setString(1, dts.getFecha());
//            pst.setString(2, dts.getCliente());
//            pst.setInt(3, dts.getValor());
//            pst.setInt(4, dts.getEfectivo());
//            pst.setInt(5, dts.getTarjeta());
//            pst.setInt(6, dts.getTransferencia());
//            pst.setInt(7, dts.getTotal());
//            pst.setString(8, dts.getObservaciones());
//            pst.setString(9, dts.getEmpleado());
//            pst.setString(10, dts.getTurno());
//            pst.setString(11, dts.getNumeroturno());
//            pst.setInt(12, dts.getSaldo());
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

//    public boolean editar(Dabonosp dts) throws SQLException {
//        // Actualizar la sentencia SQL para incluir la cláusula WHERE
//        sSQL = "UPDATE abonos SET  fecha=?, cliente=?, valor=?, efectivo=?, tarjeta=?,"
//                + " transferencia=?, total=?, observaciones=?, empleado=?, turno=?, numero_turno=?, saldo=?  WHERE idabonos=?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//
//            // Establecer los parámetros en el orden correcto
//            pst.setString(1, dts.getFecha());
//            pst.setString(2, dts.getCliente());
//            pst.setInt(3, dts.getValor());
//            pst.setInt(4, dts.getEfectivo());
//            pst.setInt(5, dts.getTarjeta());
//            pst.setInt(6, dts.getTransferencia());
//            pst.setInt(7, dts.getTotal());
//            pst.setString(8, dts.getObservaciones());
//            pst.setString(9, dts.getEmpleado());
//            pst.setString(10, dts.getTurno());
//            pst.setString(11, dts.getNumeroturno());
//            pst.setInt(12, dts.getSaldo());
//
//            //pst.setInt(13, dts.getIdabonos);
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

//    public boolean eliminar(Dabonosp dts) {
//        sSQL = "delete from abonos where idabonos=?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setInt(1, dts.getIdabono());
//
//            int n = pst.executeUpdate();
//            return n != 0;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
//            return false;
//        }
//
//    }

//    public void llenarcboClientes(JComboBox<String> combo) {
//
//        DefaultComboBoxModel<String> com = new DefaultComboBoxModel<>();  // Modelo para el JComboBox
//        combo.setModel(com);  // Asignamos el modelo al combo
//        Cconexionp conexion = new Cconexionp();  // Conexión a la base de datos
//
//        com.addElement("SELECCIONE UN CLIENTE");  // Primer elemento del ComboBox
//
//        try {
//            Connection conectar = conexion.establecerConexionp();
//            st = conectar.createStatement();
//            rs = st.executeQuery("SELECT DISTINCT cliente FROM cliente");
//
//            while (rs.next()) {
//                String cliente = rs.getString("cliente");
//
//                com.addElement(cliente);
//
//                Fabonosp func = new Fabonosp();
//                Dabonosp clientes = new Dabonosp();
//                clientes.setCliente(cliente);
//                //func.agregarCliente(clientes);
//            }
//        } catch (SQLException e) {
//            System.out.println("ERROR: " + e.getMessage());
//        }
//
//    }

//    public int obtenerPrepago(String cliente) {
//        int prepago = 0;
//        // Consulta para obtener el total del consumo
//        sSQL = "SELECT valor FROM salida WHERE tiposervicio = 'PREPAGO' AND cliente = ?  order by idsalida desc limit 1 ";
//
//        // Consulta para obtener el saldo actual del cliente en abonos
//        String obtenerSaldoSQL = "SELECT saldo FROM abonos WHERE cliente = ? ";
//
//        // Consulta para actualizar el saldo del cliente en abonos
//        String actualizarSaldoSQL = "UPDATE abonos SET saldo = saldo - ? WHERE cliente = ? ";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            // Asignar los parámetros a la consulta
//            pst.setString(1, cliente);
//
//            try (ResultSet rs2 = pst.executeQuery()) {
//                if (rs2.next()) {
//                    // Obtener el total a descontar
//                    prepago = rs2.getInt("valor");
//
//                    if (prepago > 0) {
//                        // Obtener el saldo actual del cliente
//                        try (PreparedStatement pstSaldo = cn.prepareStatement(obtenerSaldoSQL)) {
//                            pstSaldo.setString(1, cliente);
//                            try (ResultSet rsSaldo = pstSaldo.executeQuery()) {
//                                if (rsSaldo.next()) {
//                                    int saldoActual = rsSaldo.getInt("saldo");
//
//                                    // Validar si el saldo es suficiente
//                                    if (saldoActual >= prepago) {
//                                        // Descontar el saldo
//                                        try (PreparedStatement pstUpdate = cn.prepareStatement(actualizarSaldoSQL)) {
//                                            pstUpdate.setInt(1, prepago);
//                                            pstUpdate.setString(2, cliente);
//
//                                            int filasAfectadas = pstUpdate.executeUpdate();
//                                            if (filasAfectadas > 0) {
//                                                System.out.println("Saldo descontado correctamente.");
//                                            } else {
//                                                System.out.println("No se pudo actualizar el saldo. Verifica el cliente.");
//                                            }
//                                        }
//                                    } else {
//                                        JOptionPane.showMessageDialog(null, "Saldo insuficiente para descontar " + prepago + ". Saldo actual: " + saldoActual);
//                                    }
//                                } else {
//                                    JOptionPane.showMessageDialog(null, "No se encontró saldo para el cliente.");
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al obtener o descontar el saldo: " + e.getMessage());
//        }
//
//        return prepago;
//    }

}
