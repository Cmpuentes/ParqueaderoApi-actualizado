package LogicaP;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Fbloqueos {

//
//    public boolean verificarBloqueo(String codigo) {
//        boolean bloqueado = false;
//        sSQL = "SELECT codigo FROM bloqueos WHERE codigo = ?";
//
//        try ( PreparedStatement pst = cn.prepareStatement(sSQL)) {
//            pst.setString(1, codigo);  // Establecer el código a buscar
//            try ( ResultSet rs = pst.executeQuery()) {
//                if (rs.next()) {
//                    bloqueado = true;  // Si se encuentra el código, significa que está bloqueado
//                }
//            }
//        } catch (SQLException e) {
//            // Agrega un mensaje de error para facilitar la depuración
//            
//        }
//        return bloqueado;
//    }

}
