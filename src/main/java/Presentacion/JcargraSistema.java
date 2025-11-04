package Presentacion;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class JcargraSistema extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    public JcargraSistema() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Systema Luci");
        setIconImage(new ImageIcon(getClass().getResource("/Imagenes/GN_Grande.png")).getImage());
        UIManager.put("nimbusOrange", new Color(38, 139, 210));
        progressbar();

    }

    private void progressbar() {
        // Establecer el color de la barra de progreso una vez (fuera del Timer)
        // Cambia el color de la barra
        BarProgreso.setBackground(Color.LIGHT_GRAY); // Cambia el color de fondo (opcional)
        BarProgreso.setStringPainted(false); // Asegúrate de que el texto se muestre en la barra

        // Configurar el Timer para actualizar la barra de progreso
        Timer mTimer;
        mTimer = new Timer(70, (ActionEvent e) -> {
            int currentValue = BarProgreso.getValue();
            
            // Aumentar el valor de la barra de progreso
            if (currentValue < 100) {
                BarProgreso.setValue(currentValue + 1);
            } else {
                // Detener el timer cuando llegue al 100%
                ((Timer) e.getSource()).stop();
                
                // Abrir el nuevo JFrame (Ejemplo: Jmenuhotel)
                Jmenuprin menu = null;
                try {
                    menu = new Jmenuprin(); // Reemplaza con el JFrame que deseas abrir
                } catch (Exception ex) {
                    Logger.getLogger(JcargraSistema.class.getName()).log(Level.SEVERE, null, ex);
                }
                menu.setVisible(true);
                
                // Cerrar la ventana actual
                dispose();
            }
            
            // Mostrar el porcentaje en un JLabel
            lblporcentage.setText(BarProgreso.getValue() + "%");
            
            // Mostrar mensajes en diferentes porcentajes
            switch (currentValue) {
                case 30 -> lblcarga.setText("Cargando módulos...");
                case 60 -> lblcarga.setText("Iniciando servicios...");
                case 90 -> lblcarga.setText("Finalizando carga...");
                default -> {
                }
            }
        });

        // Iniciar el timer
        mTimer.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelbagraun = new javax.swing.JPanel();
        lblcarga = new javax.swing.JLabel();
        lblporcentage = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblbagraunimagen = new javax.swing.JLabel();
        BarProgreso = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelbagraun.setBackground(new java.awt.Color(0, 255, 255));
        panelbagraun.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblcarga.setBackground(new java.awt.Color(0, 255, 0));
        lblcarga.setForeground(new java.awt.Color(0, 255, 0));
        lblcarga.setText("Cargando...");
        panelbagraun.add(lblcarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, -1, 20));

        lblporcentage.setBackground(new java.awt.Color(0, 255, 0));
        lblporcentage.setForeground(new java.awt.Color(0, 255, 0));
        lblporcentage.setText("0 %");
        panelbagraun.add(lblporcentage, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 140, 40, 20));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/GN_Horizontal.png"))); // NOI18N
        panelbagraun.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 170));

        lblbagraunimagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Gradiente.png"))); // NOI18N
        lblbagraunimagen.setText("jLabel1");
        panelbagraun.add(lblbagraunimagen, new org.netbeans.lib.awtextra.AbsoluteConstraints(-1, -2, 400, 160));

        BarProgreso.setBackground(new java.awt.Color(255, 255, 255));
        BarProgreso.setBorder(null);
        panelbagraun.add(BarProgreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 400, 20));

        getContentPane().add(panelbagraun, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
      
         try {
           UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new JcargraSistema().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar BarProgreso;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblbagraunimagen;
    private javax.swing.JLabel lblcarga;
    private javax.swing.JLabel lblporcentage;
    private javax.swing.JPanel panelbagraun;
    // End of variables declaration//GEN-END:variables
}
