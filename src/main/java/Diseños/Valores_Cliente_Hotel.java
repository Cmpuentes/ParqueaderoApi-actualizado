
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
public class Valores_Cliente_Hotel extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;
    
    @Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    if (value != null) {
        try {
            double monto = Integer.parseInt(value.toString().trim()); // Convertir a número

            if (!isSelected) { // Evita que el color se pierda al seleccionar la fila
                if (monto < 0) {
                    cell.setBackground(new Color(255, 99, 71)); // Rojo si es negativo
                    cell.setForeground(Color.WHITE);
                } else {
                    cell.setBackground(new Color(144, 238, 144)); // Verde si es positivo
                    cell.setForeground(Color.BLACK);
                }
            } else {
                cell.setBackground(Color.CYAN); // Color cuando está seleccionada
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
