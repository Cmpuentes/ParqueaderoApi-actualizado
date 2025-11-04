
package Diseños;

import Presentacion.JpanelAction;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author alexn
 */
public class TableActionCellRender extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                   boolean hasFocus, int row, int column) {
        // Obtiene el componente de celda predeterminado
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Aquí puedes personalizar el componente antes de retornarlo
        JpanelAction Action = new JpanelAction();
        if(isSelected == false && row % 2 == 0){
            Action.setBackground(Color.WHITE);
            
        }else {
            Action.setBackground(component.getBackground());
        }
       

        return Action; // Retorna el componente modificado
    }
}

