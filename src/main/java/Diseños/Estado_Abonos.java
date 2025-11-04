
package Diseños;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author alexn
 */


public class Estado_Abonos extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            try {
                int estado = Integer.parseInt(value.toString().trim()); // Convertir a número
                
                if (!isSelected) { // Evita que el color se pierda al seleccionar la fila
                    if (estado < 150000) {
                        cell.setBackground(new Color(255,99,71)); // Rojo cuando es menor a 150000
                        cell.setForeground(Color.WHITE);
                    } else if (estado >= 150000 && estado < 500000) {
                        cell.setBackground(Color.YELLOW); // Amarillo cuando está entre 150000 y 500000
                        cell.setForeground(Color.BLACK);
                    } else {
                        cell.setBackground(new Color(144,238,144)); // Verde cuando es mayor o igual a 500000
                        cell.setForeground(Color.BLACK);
                    }
                } else {
                    cell.setBackground(new Color(255,255,102)); // Color cuando está seleccionada
                    cell.setForeground(Color.BLACK);
                }

            } catch (NumberFormatException e) {
                cell.setBackground(Color.WHITE); // Si no es un número, fondo blanco
                cell.setForeground(Color.BLACK);
            }
        } else {
            cell.setBackground(Color.WHITE);
            cell.setForeground(Color.BLACK);
        }

        return cell;
    }
}

    

  
    


