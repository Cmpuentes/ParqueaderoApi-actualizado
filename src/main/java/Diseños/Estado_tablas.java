package Diseños;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class Estado_tablas extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            String estado = value.toString().trim(); // Elimina espacios extra

            if (!isSelected) { // Evita que el color se pierda al seleccionar la fila
                switch (estado) {
                    case "Activo" -> {
                        cell.setBackground(new Color(144,238,144));
                        cell.setForeground(Color.BLACK);
                    }
                    case "Finalizado" -> {
                        cell.setBackground(new Color(255,99,71));
                        cell.setForeground(Color.WHITE);
                    }
                    default -> {
                        cell.setBackground(Color.WHITE);
                        cell.setForeground(Color.BLACK);
                    }
                }
            } else {
                cell.setBackground(Color.CYAN); // Color cuando está seleccionada
                cell.setForeground(Color.BLACK);
            }
        }
        return cell;
    }

    

    public static void aplicarRenderizador(JTable tabla, int columna) {
        tabla.getColumnModel().getColumn(columna).setCellRenderer(new Estado_tablas());
        tabla.repaint(); // Repintar la tabla para reflejar los cambios
    }
}
