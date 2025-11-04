package Diseños;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author JEFE CIBERSEGURIDA
 */
public class Estado_resevas extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        // Llama al padre para configurar fuente, texto, selección, etc.
        JLabel cell = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            String estado = value.toString().trim();

            if (!isSelected) {
                switch (estado) {
                    case "Reservada" ->
                        cell.setBackground(new Color(255, 255, 102));
                    case "Ingresada" ->
                        cell.setBackground(new Color(144, 238, 144));
                    case "Cancelada" ->
                        cell.setBackground(new Color(255, 99, 71));
                    case "Caducada" ->
                        cell.setBackground(new Color(0, 204, 255));
                    default ->
                        cell.setBackground(Color.WHITE);
                }
                cell.setForeground(Color.BLACK);
            } else {
                cell.setBackground(Color.CYAN);
                cell.setForeground(Color.BLACK);
            }
        }
        return cell;
    }

    public static void aplicarRenderizadorReserva(JTable tabla, int columna) {
        tabla.getColumnModel()
                .getColumn(columna)
                .setCellRenderer(new Estado_resevas());
    }

}
