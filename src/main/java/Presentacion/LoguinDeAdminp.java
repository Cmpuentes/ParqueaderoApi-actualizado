package Presentacion;

import Datos.EmpleadoResponseDTO;
import Logica.ApiEmpleadoService;
import Logica.EmpleadoService;
import LogicaP.AplicarIcono;
import java.awt.Color;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public final class LoguinDeAdminp extends javax.swing.JFrame {

    int xMouse, yMouse;

    public LoguinDeAdminp() {
        initComponents();
        AplicarIcono.icono(this, "/Imagenes/GN_Grande.png");
        setLocationRelativeTo(null);
        this.setTitle("ACCESO A ADMIN");
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        txtusuario = new javax.swing.JTextField();
        usuarioLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        txtpassword = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        btnRegistrar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        exitBtn = new javax.swing.JPanel();
        exitTxt = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setMinimumSize(new java.awt.Dimension(50, 10));
        jSeparator2.setPreferredSize(new java.awt.Dimension(50, 10));
        jPanel2.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, 197, 20));

        txtusuario.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        txtusuario.setBorder(null);
        txtusuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtusuarioMousePressed(evt);
            }
        });
        txtusuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtusuarioActionPerformed(evt);
            }
        });
        jPanel2.add(txtusuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, 197, 30));

        usuarioLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        usuarioLabel.setText("Usuario:");
        jPanel2.add(usuarioLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, -1, -1));

        passwordLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        passwordLabel.setText("Password:");
        jPanel2.add(passwordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, -1, -1));

        txtpassword.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtpassword.setBorder(null);
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
        jPanel2.add(txtpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 160, 30));

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 290, 160, 20));

        btnRegistrar.setBackground(new java.awt.Color(153, 255, 255));
        btnRegistrar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnRegistrar.setText("Ingresar");
        btnRegistrar.setBorder(null);
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRegistrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        jPanel2.add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 330, 90, 40));

        jLabel3.setFont(new java.awt.Font("Segoe UI Historic", 1, 24)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/perfil-del-usuario.png"))); // NOI18N
        jLabel3.setText("INICIAR SESIÓN");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, -1, -1));

        header.setBackground(new java.awt.Color(255, 255, 255));
        header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                headerMouseDragged(evt);
            }
        });
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                headerMousePressed(evt);
            }
        });

        exitBtn.setBackground(new java.awt.Color(255, 255, 255));

        exitTxt.setFont(new java.awt.Font("Roboto Light", 0, 24)); // NOI18N
        exitTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        exitTxt.setText("X");
        exitTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        exitTxt.setPreferredSize(new java.awt.Dimension(40, 40));
        exitTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitTxtMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitTxtMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout exitBtnLayout = new javax.swing.GroupLayout(exitBtn);
        exitBtn.setLayout(exitBtnLayout);
        exitBtnLayout.setHorizontalGroup(
            exitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(exitTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
        );
        exitBtnLayout.setVerticalGroup(
            exitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(exitTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addGap(0, 309, Short.MAX_VALUE)
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(exitBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Gradiente.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-3, -4, 330, 390));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtusuarioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtusuarioMousePressed
        if (txtusuario.getText().equals("Ingrese su nombre de usuario")) {
            txtusuario.setText("");
            txtusuario.setForeground(Color.black);
        }
        if (String.valueOf(txtpassword.getPassword()).isEmpty()) {
            txtpassword.setText("********");
            txtpassword.setForeground(Color.gray);
        }
    }//GEN-LAST:event_txtusuarioMousePressed

    private void txtusuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtusuarioActionPerformed
        // TODO add your handling code here:
        txtusuario.transferFocus();
    }//GEN-LAST:event_txtusuarioActionPerformed

    private void txtpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpasswordActionPerformed
        txtpassword.transferFocus();
    }//GEN-LAST:event_txtpasswordActionPerformed

    //MIGRADA Y PROBANDO FUNCIONAMIENTO
    private void txtpasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpasswordKeyPressed
//        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            try {
//                ApiEmpleadoService func = new ApiEmpleadoService();
//
//                // Obtener el usuario y contraseña ingresados
//                String login = txtusuario.getText();
//                String password = txtpassword.getText();
//
//                // Intentar realizar el login
//                EmpleadoResponseDTO empleado = func.login(login, password);
//
//                if (empleado != null) {
////            System.out.println("Login exitoso. Datos del empleado:");
////            System.out.println(empleado);
//
//                    // Crear y mostrar el formulario principal
//                    Javanzadop form = new Javanzadop();
//                    form.toFront();
//                    form.setVisible(true);
//
//                    // Establecer los valores en Jmenuprin
//                    Javanzadop.lblacceso.setText(empleado.getAcceso());
//
//                    // Verificar si el acceso es para un empleado
//                    if (!Javanzadop.lblacceso.getText().equals("Administrador")) {
//                        JOptionPane.showMessageDialog(rootPane, "Acceso Denegado", "Acceso al Sistema", JOptionPane.ERROR_MESSAGE);
//                        // Si el acceso no es para un General, puedes desactivar el menú u realizar alguna otra acción
//                        form.setVisible(false);
//                    } else {
//                        this.dispose();
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(rootPane, "Credenciales incorrectas o el usuario no está activo.", "Error de Login", JOptionPane.ERROR_MESSAGE);
//                }
//            } catch (HeadlessException e) {
//                System.out.println("ERROR DE LOGIN");
//                JOptionPane.showMessageDialog(rootPane, "Ocurrió un error durante el login.", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//
//        }
    }//GEN-LAST:event_txtpasswordKeyPressed

    //MIGRADA Y PROBANDO FUNCIONAMIENTO
    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed

//        System.out.println("Entrando a btnIngresarActionPerformed...");  // 👈 aquí
        EmpleadoService func = new EmpleadoService();

        // Obtener el usuario y contraseña ingresados
        String login = txtusuario.getText();
        String password = txtpassword.getText();

        // Intentar realizar el login
        boolean esAdmin = func.validarLoginAdministrador(login, password);

        if (esAdmin) {
            System.out.println("Acceso concedido. Bienvenido Administrador.");
            // Crear y mostrar el formulario principal
            Javanzadop form = new Javanzadop();
            form.toFront();
            form.setVisible(true);
            this.dispose();

        } else {
            JOptionPane.showMessageDialog(rootPane, "Credenciales incorrectas o no tienes acceso de administrador.",
                    "Error de Login", JOptionPane.ERROR_MESSAGE);
        }
//        try {
//            
//        } catch (HeadlessException e) { // 👈 para ver la excepción real
//            // 👈 para ver la excepción real
////            JOptionPane.showMessageDialog(rootPane,
////                    "Ocurrió un error durante el login: " + e.getMessage(),
////                    "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void exitTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseClicked
        dispose();
    }//GEN-LAST:event_exitTxtMouseClicked

    private void exitTxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseEntered
        exitBtn.setBackground(Color.red);
        exitTxt.setForeground(Color.white);
    }//GEN-LAST:event_exitTxtMouseEntered

    private void exitTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseExited
        exitBtn.setBackground(Color.white);
        exitTxt.setForeground(Color.black);
    }//GEN-LAST:event_exitTxtMouseExited

    private void headerMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_headerMouseDragged

    private void headerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_headerMousePressed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoguinDeAdminp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new LoguinDeAdminp().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JPanel exitBtn;
    private javax.swing.JLabel exitTxt;
    private javax.swing.JPanel header;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField txtpassword;
    private javax.swing.JTextField txtusuario;
    private javax.swing.JLabel usuarioLabel;
    // End of variables declaration//GEN-END:variables
}
