package LogicaP;


import DatosP.Dingresop;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Lingresop {

   
    ArrayList<Dingresop> listaplaca = new ArrayList<>();

//    public void agregarplaca(Dingresop placa) {
//        listaplaca.add(placa);
//    }

//    public DefaultTableModel mostrar(String buscar, JTable TablaIngreso) {
//        DefaultTableModel model = (DefaultTableModel) TablaIngreso.getModel();
//        model.setRowCount(0); // Limpiar la tabla antes de llenarla
//
//        String[] titulos = {"ID INGRESO", "Turno", "N turno", "Empleado",
//            "placa", "Fecha Ingreso", "Tipo Vehiculo", "Tipo Servicio", "Cliente",
//            "Zona", "Observaciones", "Estado"};
//
//        String[] registro = new String[12];
//
//        totalregistros = 0;
//        model = new DefaultTableModel(null, titulos);
//
//        sSQL = "SELECT * FROM ingreso WHERE placa LIKE ? ORDER BY estado = 'Activo' desc";
//
//        try {
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            pst.setString(1, "%" + buscar + "%");
//            ResultSet rs = pst.executeQuery();
//
//            while (rs.next()) {
//                registro[0] = rs.getString("idingreso");
//                registro[1] = rs.getString("turno");
//                registro[2] = rs.getString("numeroturno");
//                registro[3] = rs.getString("empleado");
//                registro[4] = rs.getString("placa");
//                registro[5] = rs.getString("fechaingreso");
//                registro[6] = rs.getString("tipovehiculo");
//                registro[7] = rs.getString("tiposervicio");
//                registro[8] = rs.getString("cliente");
//                registro[9] = rs.getString("zona");
//                registro[10] = rs.getString("observaciones");
//                registro[11] = rs.getString("estado");
//
//                totalregistros++;
//                model.addRow(registro.clone()); // Clonamos el arreglo antes de añadirlo al modelo
//            }
//              // Aplicar el sorter para permitir filtrado
//                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
//                TablaIngreso.setRowSorter(sorter);
//            return model;
//
//        } catch (SQLException e) {
//            JOptionPane.showConfirmDialog(null, e);
//            return null;
//        }
//    }
//
//    public boolean insertar(Dingresop dts) {
//        sSQL = "INSERT INTO ingreso ( turno, numeroturno, empleado, placa, fechaingreso, tipovehiculo,"
//                + " tiposervicio, cliente, zona, observaciones, estado)"
//                + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//
//            pst.setString(1, dts.getTurno());
//            pst.setString(2, dts.getNumeroturno());
//            pst.setString(3, dts.getEmpleado());
//            pst.setString(4, dts.getPlaca());
//            pst.setString(5, dts.getFechaingreso());
//            pst.setString(6, dts.getTipovehiculo());
//            pst.setString(7, dts.getTiposervicio());
//            pst.setString(8, dts.getCliente());
//            pst.setString(9, dts.getZona());
//            pst.setString(10, dts.getObservaciones());
//            pst.setString(11, dts.getEstado());
//
//            int n = pst.executeUpdate();
//            return n != 0;
//        } catch (SQLException e) {
//            JOptionPane.showConfirmDialog(null, e);
//            return false;
//        }
//    }
//
//    public boolean editar(Dingresop dts) {
//        sSQL = "UPDATE ingreso SET  turno=?, numeroturno=?, empleado=?, placa=?,"
//                + " fechaingreso=?, tipovehiculo=?, tiposervicio=?, cliente=?, zona=?, "
//                + " observaciones=?, estado=? where idingreso=?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//
//            pst.setString(1, dts.getTurno());
//            pst.setString(2, dts.getNumeroturno());
//            pst.setString(3, dts.getEmpleado());
//            pst.setString(4, dts.getPlaca());
//            pst.setString(5, dts.getFechaingreso());
//            pst.setString(6, dts.getTipovehiculo());
//            pst.setString(7, dts.getTiposervicio());
//            pst.setString(8, dts.getCliente());
//            pst.setString(9, dts.getZona());
//            pst.setString(10, dts.getObservaciones());
//            pst.setString(11, dts.getEstado());
//
//            pst.setInt(12, dts.getIdingreso());
//            int n = pst.executeUpdate();
//            return n != 0;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al editar EL INGRESO: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean eliminar(Dingresop dts) {
//        sSQL = "delete from ingreso where idingreso=?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setInt(1, dts.getIdingreso());
//
//            int n = pst.executeUpdate();
//            return n != 0;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al eliminar la INGRESO: " + e.getMessage());
//            return false;
//        }
//
//    }
//
//    public boolean CambioEstado(Dingresop dts) {
//        sSQL = "UPDATE ingreso SET estado='Finalizado' where idingreso=?";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//
//            pst.setInt(1, dts.getIdingreso());
//            int n = pst.executeUpdate();
//            return n != 0;
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al editar EL ESTADO: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public String[] obtenerDatosIngreso(int numero) {
//        String[] datos = new String[3]; // Array de 3 elementos: placa, vehículo, servicio
//         sSQL = "SELECT placa, tipovehiculo, tiposervicio FROM ingreso WHERE numero = ? AND estado = 'Activo'";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setInt(1, numero);
//            try (ResultSet rs = pst.executeQuery()) {
//                if (rs.next()) {
//                    datos[0] = rs.getString("placa");
//                    datos[1] = rs.getString("tipovehiculo");
//                    datos[2] = rs.getString("tiposervicio");
//                } else {
//                    JOptionPane.showMessageDialog(null, "No se encontraron datos para el número: " + numero);
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al obtener los datos: " + e.getMessage());
//        }
//
//        return datos;
//    }
//
//    public DefaultTableModel mostrarIngreso(String buscar) {
//        DefaultTableModel modelo;
//
//        String[] titulos = {"ID INGRESO", "Turno", "N turno", "Empleado",
//            "placa", "Fecha Ingreso", "Tipo Vehiculo", "Tipo Servicio", "Cliente",
//            "Zona", "Observaciones", "Estado"};
//
//        String[] registro = new String[12];
//
//        totalregistros = 0;
//        modelo = new DefaultTableModel(null, titulos);
//
//        sSQL = "SELECT * FROM ingreso WHERE placa LIKE ? AND estado = 'Activo' ORDER BY idingreso desc";
//
//        try {
//            PreparedStatement pst = cn.prepareStatement(sSQL);
//            pst.setString(1, "%" + buscar + "%");
//            ResultSet rs = pst.executeQuery();
//
//            while (rs.next()) {
//                 registro[0] = rs.getString("idingreso");
//                registro[1] = rs.getString("turno");
//                registro[2] = rs.getString("numeroturno");
//                registro[3] = rs.getString("empleado");
//                registro[4] = rs.getString("placa");
//                registro[5] = rs.getString("fechaingreso");
//                registro[6] = rs.getString("tipovehiculo");
//                registro[7] = rs.getString("tiposervicio");
//                registro[8] = rs.getString("cliente");
//                registro[9] = rs.getString("zona");
//                registro[10] = rs.getString("observaciones");
//                registro[11] = rs.getString("estado");
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
//
//    }
//
//    public boolean verificarPlacaActiva(String placa) {
//
//        sSQL = "SELECT COUNT(*) FROM ingreso WHERE placa = ? AND estado = 'Activo'";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setString(1, placa);
//            ResultSet rs = pst.executeQuery();
//            if (rs.next()) {
//                return rs.getInt(1) > 0; // Si hay al menos un registro con estado "ACTIVO", la placa ya está en el parqueadero
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al verificar la placa: " + e.getMessage());
//        }
//
//        return false; // Si no hay registros activos, la placa no está en el parqueadero
//    }

}
