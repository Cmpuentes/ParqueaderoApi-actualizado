
package Diseños;

/**
 *
 * @author alexn
 */
public interface TablecActionEvent {
    
    public void onEdit(int row);
    
    public void onDelete(int row);
    
    public void onView(int row);
}
