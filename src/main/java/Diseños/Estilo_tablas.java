
package Diseños;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;


public class Estilo_tablas {
    
     public static void configurarTabla(JTable tabla) {
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setRowHeight(25);
        tabla.setRowMargin(5);

        // Configurar encabezado con un renderer personalizado
        JTableHeader header = tabla.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setBackground(new Color(135, 206, 235));
                cell.setForeground(Color.WHITE);
                cell.setFont(new Font("SansSerif", Font.BOLD, 14));
                return cell;
            }
        });
    }

    public static void ocultarColumnas(JTable tabla, int[] columnas) {
        for (int columna : columnas) {
            tabla.getColumnModel().getColumn(columna).setMaxWidth(0);
            tabla.getColumnModel().getColumn(columna).setMinWidth(0);
            tabla.getColumnModel().getColumn(columna).setPreferredWidth(0);
        }
    }
    
}
