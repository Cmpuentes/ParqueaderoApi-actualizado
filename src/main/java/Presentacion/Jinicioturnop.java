package Presentacion;

import DatosP.LoginResponse;
import DatosP.SessionData;
import DatosP.SessionManager;
import DatosP.SessionStorage;
import LogicaP.AplicarIcono;
import LogicaP.LoginService;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Jinicioturnop extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    private Jmenuprin parent;

    public Jinicioturnop(Jmenuprin parent) {
        this.parent = parent;

        initComponents();

        AplicarIcono.icono(this, "/Imagenes/GN_Grande.png");
        setLocationRelativeTo(null);
        this.setTitle("INICIO TURNO");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fechacbop();
    }

    private String accion = "guardar";

    //FUNCIÓN QUE COLOCA LOS ITEMS EN EL CBOTURNOS Y LOS CONCATENABA CON LA FECHA.
    //POR AHORA LOS DEJAMOS SIN LA CONCATENACIÓN.
    private void fechacbop() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaActual = LocalDate.now().format(formatter);
        cboturnos.addItem("Seleccionar");//SE LE CONCATENA LA VARIABLE fechaActual
        cboturnos.addItem("Turno 1");
        cboturnos.addItem("Turno 2");
        cboturnos.addItem("Turno 3");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtusuario = new javax.swing.JTextField();
        txtpassword = new javax.swing.JPasswordField();
        cboturnos = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ingresoTurno = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        gradiente = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Password:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, -1, 20));

        txtusuario.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        txtusuario.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtusuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtusuarioActionPerformed(evt);
            }
        });
        jPanel2.add(txtusuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, 160, 30));

        txtpassword.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        txtpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpasswordActionPerformed(evt);
            }
        });
        txtpassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpasswordKeyPressed(evt);
            }
        });
        jPanel2.add(txtpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, 160, 30));

        jPanel2.add(cboturnos, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 160, 30));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setText("Usuario:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, -1, -1));

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText("Turno:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, -1, -1));

        ingresoTurno.setBackground(new java.awt.Color(51, 255, 255));
        ingresoTurno.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        ingresoTurno.setText("Ingresar");
        ingresoTurno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingresoTurnoActionPerformed(evt);
            }
        });
        jPanel2.add(ingresoTurno, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 160, 40));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel8.setText("BIENVENIDO");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 120, 40));

        gradiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Gradiente.png"))); // NOI18N
        jPanel2.add(gradiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 350));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtusuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtusuarioActionPerformed
        txtusuario.transferFocus();
    }//GEN-LAST:event_txtusuarioActionPerformed

    private void txtpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpasswordActionPerformed
        txtpassword.transferFocus();
    }//GEN-LAST:event_txtpasswordActionPerformed

    private void txtpasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpasswordKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (cboturnos.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "DEBE SELECCIONAR UN TURNO", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int selecturno = cboturnos.getSelectedIndex();
            String turno = cboturnos.getItemAt(selecturno);
            String login = txtusuario.getText();
            String password = new String(txtpassword.getPassword()); // si es JPasswordField

            try {
                LoginService loginService = new LoginService();
                LoginResponse response = loginService.login(turno, login, password);

                if (response.isSuccess()) {
                    Jmenuprin.sesionIniciadaP = true;
                    this.dispose();

                    // Guardar la sesión global
                    SessionData sessionData = new SessionData(
                            response.getToken(),
                            response.getNombreCompleto(),
                            response.getFecha_inicio(),
                            response.getTurno(),
                            response.getNumero_turno(),
                            false // turnoFinalizado
                    );
                    SessionManager.getInstance().setSessionData(sessionData);
                    SessionStorage.saveSession(sessionData); // opcional: persistencia en archivo

                    if (parent != null) {
                        parent.actualizarLabels(sessionData); // ← actualiza el que ya está abierto
                    }
                    this.dispose(); // cierras solo el login

                } else {
                    JOptionPane.showMessageDialog(rootPane, "Acceso Denegado", "Error de Login", JOptionPane.ERROR_MESSAGE);
                }
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(rootPane, "Ocurrió un error durante el login.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(Jinicioturnop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }//GEN-LAST:event_txtpasswordKeyPressed

    //BOTÓN DONDE SE HACE EL INICIO DE SESIÓN Y TURNO
    private void ingresoTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingresoTurnoActionPerformed

        if (cboturnos.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "DEBE SELECCIONAR UN TURNO", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selecturno = cboturnos.getSelectedIndex();
        String turno = cboturnos.getItemAt(selecturno);
        String login = txtusuario.getText();
        String password = new String(txtpassword.getPassword()); // si es JPasswordField

        try {

            LoginService loginService = new LoginService();
            LoginResponse response = loginService.login(turno, login, password);

            if (response.isSuccess()) {
                Jmenuprin.sesionIniciadaP = true;
                this.dispose();

                // Guardar la sesión global
                SessionData sessionData = new SessionData(
                        response.getToken(),
                        response.getNombreCompleto(),
                        response.getFecha_inicio(),
                        response.getTurno(),
                        response.getNumero_turno(),
                        false // turnoFinalizado
                );
                SessionManager.getInstance().setSessionData(sessionData);
                SessionStorage.saveSession(sessionData); // opcional: persistencia en archivo

                if (parent != null) {
                    parent.actualizarLabels(sessionData); // ← actualiza el que ya está abierto
                }
                this.dispose(); // cierras solo el login

            } else {
                JOptionPane.showMessageDialog(rootPane, response.getMessage(), "Error de Login", JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(rootPane, "Ocurrió un error durante el login.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(Jinicioturnop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ingresoTurnoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Jinicioturnop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Jinicioturnop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Jinicioturnop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Jinicioturnop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                // Para pruebas, creamos un Jmenuprin falso
                Jmenuprin dummy = new Jmenuprin();
                Jinicioturnop login = new Jinicioturnop(dummy);
                login.setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(Jinicioturnop.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboturnos;
    private javax.swing.JLabel gradiente;
    private javax.swing.JButton ingresoTurno;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField txtpassword;
    private javax.swing.JTextField txtusuario;
    // End of variables declaration//GEN-END:variables
}
