package LogicaP;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Fempleadop {



//    public DefaultTableModel mostrarvistap(String buscar) {
//        DefaultTableModel modelo;
//        String[] titulos = {"idempleado", "Nombres", "Apellidos", "Tipodocumento", "Documento", "Teléfono", "Dirección", "Email", "pais", "ciudad", "Acceso", "Login", "Password", "Estado", "eps", "arl"};
//        totalregistros = 0;
//        modelo = new DefaultTableModel(null, titulos);
//
//        sSQL = "SELECT * FROM empleado WHERE documento LIKE ? ORDER BY idempleado";
//
//        try (PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setString(1, "%" + buscar + "%"); // Aplicar el filtro correctamente
//
//            try (ResultSet rs = pst.executeQuery()) {
//                while (rs.next()) {
//                    // Crear un nuevo array en cada iteración para evitar referencias duplicadas
//                    String[] registro = new String[16];
//
//                    registro[0] = rs.getString("idempleado");
//                    registro[1] = rs.getString("nombres");
//                    registro[2] = rs.getString("apellidos");
//                    registro[3] = rs.getString("tipodocumento");
//                    registro[4] = rs.getString("documento");
//                    registro[5] = rs.getString("telefono");
//                    registro[6] = rs.getString("direccion");
//                    registro[7] = rs.getString("email");
//                    registro[8] = rs.getString("pais");
//                    registro[9] = rs.getString("ciudad");
//                    registro[10] = rs.getString("acceso");
//                    registro[11] = rs.getString("login");
//                    registro[12] = rs.getString("password");
//                    registro[13] = rs.getString("estado");
//                    registro[14] = rs.getString("eps");
//                    registro[15] = rs.getString("arl");
//
//                    totalregistros++;
//                    modelo.addRow(registro);
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error al obtener datos: " + e.getMessage());
//        }
//
//        return modelo;
//    }


}
