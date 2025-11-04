
package Diseños;

import Presentacion.JpanelAction;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author alexn
 */
public class TableActionCellEditor extends DefaultCellEditor{

    private static final long serialVersionUID = 1L;
    
    private final TablecActionEvent event;
    
    public TableActionCellEditor(TablecActionEvent event){
        super (new JCheckBox());
        this.event = event;
    }
    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int row, int column){
        
        JpanelAction action = new JpanelAction();
        action.initEvent(event, row);
        action.setBackground(jtable.getSelectionBackground());
        return action;
    }
}
