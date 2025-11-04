
package Presentacion;

import Diseños.TablecActionEvent;
import java.awt.event.ActionEvent;

/**
 *
 * @author alexn
 */
public class JpanelAction extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    
    public JpanelAction() {
        initComponents();
    }
 
    public void initEvent(TablecActionEvent event, int row){
        btneditar.addActionListener((ActionEvent ae) -> {
            event.onEdit(row);
        });
             btneliminar.addActionListener((ActionEvent ae) -> {
                 event.onDelete(row);
        });
                  btnver.addActionListener((ActionEvent ae) -> {
                      event.onView(row);
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnver = new Diseños.Diseño_Botones_Salida_Jtable();
        btneditar = new Diseños.Diseño_Botones_Salida_Jtable();
        btneliminar = new Diseños.Diseño_Botones_Salida_Jtable();

        btnver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ojo.png"))); // NOI18N

        btneditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lapis.png"))); // NOI18N

        btneliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btneditar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnver, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btneditar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnver, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Diseños.Diseño_Botones_Salida_Jtable btneditar;
    private Diseños.Diseño_Botones_Salida_Jtable btneliminar;
    private Diseños.Diseño_Botones_Salida_Jtable btnver;
    // End of variables declaration//GEN-END:variables

    
}
